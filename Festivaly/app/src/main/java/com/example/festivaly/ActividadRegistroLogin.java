package com.example.festivaly;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.festivaly.Usuario.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;


public class ActividadRegistroLogin extends AppCompatActivity implements View.OnClickListener{

    // register xml
    EditText email,password;
    // login xml
    EditText emailLogin, passwordLogin;
    // datos usuario xml
    EditText nombre, ubicacion, usuario, descripcion;

    ImageView img;

    // Objeto uri para la imagen perfil:
    private Uri filePath;
    private Uri fileCloud;


    Button botonRegistrar,botonInicioSesion, botonLogin, botonVolver, botonSiguiente, botonBuscarFoto;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://festivaly-599a7.appspot.com");

    private DatabaseReference mDataBase;
    private Usuario usuario_actual;

    private static int RESULT_LOAD_IMAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
        setTitle(Constantes.TAG_REGISTRO);

        FirebaseApp.initializeApp(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        botonRegistrar = (Button) findViewById(R.id.botonRegistro);
        botonRegistrar.setOnClickListener(this);

        botonInicioSesion = (Button) findViewById(R.id.botonInicioSesion);
        botonInicioSesion.setOnClickListener(this);



        firebaseAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference();

    }

    /**
     * Función que se ejecuta cuando se abre otra vez la app, si hemos iniciado sesión
     * entra directamente a la actividad principal
     * TODO: PROBLEMA CON ESTO, COMPROBAR QUE AUN NO SE HA ACABADO EL REGISTRO SINO ENTRA EN CONFLICO
     * CUANDO SALE DE BUSCAR IMAGEN Y PETA
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            Intent intent = new Intent(this,ActividadPrincipal.class);
            startActivity(intent);
        }
    }

    /**
     * Función que hace de escucha para toda la actividad de inicio y registro de sesión.
     * @param v vista actual
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()){
            case R.id.botonRegistro:
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                // Comprobamos todos los campos

                if(TextUtils.isEmpty(emailText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordText)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(passwordText.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                Log.d("Email:", emailText);
                Log.d("Pass:", passwordText);

                // Creamos el usuario en la BD y se inicia sesión automaticamente
                firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    // Una vez iniciada sesión cargamos el nuevo layout y escuchamos los nuevos campos
                                    if(firebaseAuth.getCurrentUser()!=null){
                                        Toast.makeText(getApplicationContext(),"Susscess",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                setContentView(R.layout.register_datos_usuario);

                nombre = (EditText) findViewById(R.id.nombre);
                nombre.setOnClickListener(this);

                ubicacion = (EditText) findViewById(R.id.ubicacion);
                ubicacion.setOnClickListener(this);

                usuario = (EditText) findViewById(R.id.usuario);
                usuario.setOnClickListener(this);

                descripcion = (EditText) findViewById(R.id.descripcion);
                descripcion.setOnClickListener(this);

                botonSiguiente = (Button) findViewById(R.id.botonSiguiente);
                botonSiguiente.setOnClickListener(this);

                botonBuscarFoto = (Button) findViewById(R.id.botonBuscarImagen);
                botonBuscarFoto.setOnClickListener(this);
            break;

            // Botón que nos lleva a la pantalla de inicio de sesión
            case R.id.botonInicioSesion:
                setContentView(R.layout.login);
                getActionBar().setTitle(Constantes.TAG_INICIO_DESION);

                botonLogin = (Button) findViewById(R.id.botonLogin);
                botonLogin.setOnClickListener(this);

                botonVolver = (Button) findViewById(R.id.botonVolver);
                botonVolver.setOnClickListener(this);

                emailLogin = (EditText) findViewById(R.id.emailLogin);
                passwordLogin = (EditText) findViewById(R.id.passwordLogin);

            break;

            // Botón que inicia sesión
            case R.id.botonLogin:
                getActionBar().setTitle(Constantes.TAG_INICIO_DESION);
                String emailTLogin = emailLogin.getText().toString();
                String passwordTLogin = passwordLogin.getText().toString();

                // Comprobamos los campos
                if(TextUtils.isEmpty(emailTLogin)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordTLogin)){
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }

                if(passwordTLogin.length()<6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                }

                Log.d("Email:", emailTLogin);
                Log.d("Pass:", passwordTLogin);

                // iniciamos sesión
                firebaseAuth.signInWithEmailAndPassword(emailTLogin, passwordTLogin)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("ERROR: ", "signInWithEmail:success");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    startActivity(new Intent(getApplicationContext(), ActividadPrincipal.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("ERROR: ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(ActividadRegistroLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                // TODO: buscar en la BD el usuario y bajarse el objeto de su información
                // iniciamos la actividad inicial


            break;
            // Boton para volver al registro
            case R.id.botonVolver:
                getActionBar().setTitle(Constantes.TAG_REGISTRO);
                email = (EditText) findViewById(R.id.email);
                password = (EditText) findViewById(R.id.password);

                botonRegistrar = (Button) findViewById(R.id.botonRegistro);
                botonRegistrar.setOnClickListener(this);

                botonInicioSesion = (Button) findViewById(R.id.botonInicioSesion);
                botonInicioSesion.setOnClickListener(this);
            break;

            // Boton para buscar la imagen deseada
            case R.id.botonBuscarImagen:
                if (!checkIfAlreadyhavePermission()) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }

            break;

            //Boton para salir del registro
            // TODO: chequear sexo y orientación
            case R.id.botonSiguiente:
                // Subimos la imagen del perfil
                subirImagen();
                break;
        }

    }

    /**
     * Función que sube la imagen guarda en el pathFile al servidor. Una vez subido guardamos el
     * nuevo objeto URI web (esto último no es necesario pues podemos buscar la foto facilmente en
     * la BD.
     */
    private void subirImagen() {
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Subiendo...");
            progressDialog.show();

            StorageReference ref = storageRef.child("imagenes-perfil/"+ firebaseAuth.getCurrentUser().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ActividadRegistroLogin.this,"Uploaded",Toast.LENGTH_SHORT).show();
                            Log.d("     Path:    ", taskSnapshot.getMetadata().getPath());
                            Log.d("     Name:    ", taskSnapshot.getMetadata().getName());
                            Log.d("     user::    ", firebaseAuth.getCurrentUser().getUid());
                            Log.d("     bucket::    ", taskSnapshot.getMetadata().getBucket());
                            Log.d("     pathURI::    ", taskSnapshot.getUploadSessionUri().getPath());
                            Log.d("     pathURI2::    ", taskSnapshot.getMetadata().getReference().getPath());

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    fileCloud = uri;

                                    // Creamos un objeto de tipo usuario para tenerlo en BD
                                    Usuario nuevoUsuario = new Usuario(
                                            firebaseAuth.getCurrentUser().getUid(),
                                            firebaseAuth.getCurrentUser().getEmail(),
                                            nombre.getText().toString(),
                                            usuario.getText().toString(),
                                            ubicacion.getText().toString(),
                                            descripcion.getText().toString(),
                                            "masculino",
                                            "ninguna",
                                            false,
                                            fileCloud.toString()
                                    );

                                    usuario_actual = nuevoUsuario;
                                    // Lo metemos en la base de datos
                                    mDataBase.child("users").child(nuevoUsuario.getId()).setValue(nuevoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // Iniciamos la actividad inicial
                                            Intent intent = new Intent(getApplicationContext(),ActividadPrincipal.class);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ActividadRegistroLogin.this,"Failed",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int) progress + "%");
                        }
                    });

        }

    }

    /**
     * Comprueba si se tienen permisos sobre la lectura
     * @return
     */
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Función para para resolver la llamada de los persmisos,
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                } else {
                    Toast.makeText(ActividadRegistroLogin.this, "Please give your permission.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    filePath = data.getData();

                    img = findViewById(R.id.imagenPerfilRegistro);
                    Picasso
                            .get()
                            .load(data.getData().toString())
                            .transform(new CropCircleTransformation())
                            .into(img);

                    img.setVisibility(View.VISIBLE);
                    botonBuscarFoto.setVisibility(View.GONE);
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

