package com.example.festivaly.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.festivaly.Anuncio.Anuncio;
import com.example.festivaly.Constantes;
import com.example.festivaly.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class CrearAnuncioFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDataBase;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://festivaly-599a7.appspot.com");

    EditText tituloAnuncio, descripcionAnuncio;
    ImageView imgAnuncio;
    Button buscarImagen, publicarAnuncio;
    View divider;


    private Uri filePath;
    private Uri fileCloud;

    String urlImagen;
    Boolean imgSeleecionada =false;

    File path;

    private int num_festival;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(Constantes.TAG_CREAR_ANUNCIO);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = this.getArguments();
        View v = null;

        if (b != null){
            v = inflater.inflate(R.layout.fragment_crear_anuncio, container, false);
            num_festival = b.getInt("num_festival");
            firebaseAuth = FirebaseAuth.getInstance();
            mDataBase = FirebaseDatabase.getInstance().getReference();

            tituloAnuncio = v.findViewById(R.id.titulo_anuncio);
            descripcionAnuncio = v.findViewById(R.id.descripcion_anuncio);
            buscarImagen = v.findViewById(R.id.abrir_foto_anuncio);
            imgAnuncio = v.findViewById(R.id.imagen_selecionada);
            publicarAnuncio = v.findViewById(R.id.publicar_anuncio);
            divider = v.findViewById(R.id.divider3);
            divider.setVisibility(View.GONE);

            urlImagen = "";

            buscarImagen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!checkIfAlreadyhavePermission()) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 1);
                    }
                }
            });

            publicarAnuncio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subirImagen();
                    if (!imgSeleecionada){
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
                        String idUUID = UUID.randomUUID().toString();
                        final String idFecha = simpleDateFormat.format(new Date());
                        final String idImagen = idFecha.concat(" - " + idUUID);

                        Anuncio nuevoAnuncio = new Anuncio(
                                idImagen,
                                firebaseAuth.getCurrentUser().getUid(),
                                tituloAnuncio.getText().toString(),
                                idFecha,
                                "",
                                descripcionAnuncio.getText().toString()
                        );

                        mDataBase
                                .child("festival_" + num_festival)
                                .child("anuncios")
                                .child(nuevoAnuncio.getId())
                                .setValue(nuevoAnuncio).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        });
                    }
                }
            });
        }

        // Inflate the layout for this fragment
        return v;
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
                } else {
                    Toast.makeText(getActivity(), "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    filePath = data.getData();

                    imgAnuncio = getActivity().findViewById(R.id.imagen_selecionada);

                    Picasso
                            .get()
                            .load(data.getData().toString())
                            .into(imgAnuncio);

                    divider.setVisibility(View.VISIBLE);
                    imgAnuncio.setVisibility(View.VISIBLE);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(getActivity(), "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Función que sube la imagen guarda en el pathFile al servidor. Una vez subido guardamos el
     * nuevo objeto URI web (esto último no es necesario pues podemos buscar la foto facilmente en
     * la BD.
     */
    private void subirImagen() {
        if(filePath != null){
            imgSeleecionada = true;
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy - HH:mm:ss");
            String idUUID = UUID.randomUUID().toString();
            final String idFecha = simpleDateFormat.format(new Date());
            final String idImagen = idFecha.concat(" - " + idUUID);

            StorageReference ref = storageRef.child("imagenes-anuncios/"+ idImagen);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_SHORT).show();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    fileCloud = uri;
                                    urlImagen = fileCloud.toString();

                                    Anuncio nuevoAnuncio = new Anuncio(
                                            idImagen,
                                            firebaseAuth.getCurrentUser().getUid(),
                                            tituloAnuncio.getText().toString(),
                                            idFecha,
                                            urlImagen,
                                            descripcionAnuncio.getText().toString()
                                    );

                                    mDataBase
                                            .child("festival_" + num_festival)
                                            .child("anuncios")
                                            .child(nuevoAnuncio.getId())
                                            .setValue(nuevoAnuncio).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            getActivity().getSupportFragmentManager().popBackStack();
                                        }
                                    });
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int) progress + "%");
                        }
                    });

        }else{
            imgSeleecionada =false;
        }

    }
}