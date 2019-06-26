package com.example.festivaly.Peticion;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.festivaly.Comentarios.Comentario;
import com.example.festivaly.R;
import com.example.festivaly.Usuario.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class PeticionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public Context context;

    ImageView imgPerfilPeticion;
    TextView contenidoPeticion, nombrePeticion, userPeticion;
    Button aceptar,rechazar;
    Boolean to_me;
    LinearLayout root;

    String idComentrioPeticion;
    String idUsuario;

    private DatabaseReference mDataBase;

    public PeticionHolder(View itemView, Context context) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        this.context = context;
        this.imgPerfilPeticion = itemView.findViewById(R.id.imgPerfilPeticion);
        this.contenidoPeticion = itemView.findViewById(R.id.motivoPeticion);
        this.nombrePeticion = itemView.findViewById(R.id.nombrePeticion);
        this.userPeticion = itemView.findViewById(R.id.usuarioPeticion);
        this.aceptar = itemView.findViewById(R.id.botonAceptarPeticion);
        this.rechazar = itemView.findViewById(R.id.botonRechazarPeticon);
        mDataBase = FirebaseDatabase.getInstance().getReference();
        Log.d("contructor holder", "fin");

        aceptar.setOnClickListener(this);
        rechazar.setOnClickListener(this);
    }

    // TODO GETERS AND SETTERS

    public void setTo_me(Boolean b, String idUser){
        this.to_me = b;
        this.idUsuario = idUser;
        Log.d("setTome", "ejecutado");
    }

    public void setIdComentrioPeticion(String id){
        this.idComentrioPeticion = id;
    }

    public void setElementosUsuario(String idUSuario){
        if (!this.to_me){
            this.aceptar.setVisibility(View.GONE);
        }

        mDataBase.child("users")
                .child(idUSuario)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        nombrePeticion.setText(dataSnapshot.getValue(Usuario.class).getNombre());
                        userPeticion.setText(dataSnapshot.getValue(Usuario.class).getUsuario());
                        setImagenPerfilPeticion(dataSnapshot.getValue(Usuario.class).getImagenPerfil());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Log.d("elementos usuario()", "fin");
    }

    private void setImagenPerfilPeticion(String imagenPerfil) {
        Picasso
                .get()
                .load(imagenPerfil)
                .transform(new CropCircleTransformation())
                .into(imgPerfilPeticion);
    }

    public void setContenidoPeticion(String idComentarioPeticion){
        mDataBase.child("festival_1")
                .child("comentarios")
                .child(idComentarioPeticion)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        contenidoPeticion.setText(dataSnapshot.getValue(Comentario.class).getContenido());
                        Log.d("setContenidoPeticion","onDataChance");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("setContenidoPeticion","onCandelled");
                    }
                });
    }

    public void setName(String prueba) {
        nombrePeticion.setText(prueba);
    }

    public void setUser(String prueba) {
        userPeticion.setText(prueba);
    }

    public void setContenido(String prueba) {
        contenidoPeticion.setText(prueba);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonAceptarPeticion:
                aceptarPetcion();
            break;

            case R.id.botonRechazarPeticon:
                eliminarAntiguaPeticion();
            break;
        }
    }

    private void aceptarPetcion() {

        // Añado contaactos
        aniadirContactos();
        // Borrar peticion de to_me y from_me del otro user
        eliminarAntiguaPeticion();
    }

    private void eliminarAntiguaPeticion() {
        Map<String,Object> b;
        if (to_me){
            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);

            Log.d("eliminarAntiguPeticion",idComentrioPeticion);
            mDataBase
                    .child("peticiones")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("nuevas")
                    .updateChildren(b);

            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);
            mDataBase
                    .child("peticiones")
                    .child(idUsuario)
                    .child("from_me")
                    .updateChildren(b);

            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);
            mDataBase
                    .child("peticiones")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("to_me")
                    .updateChildren(b);
        }else {
            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);

            mDataBase
                    .child("peticiones")
                    .child(idUsuario)
                    .child("nuevas")
                    .updateChildren(b);

            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);
            mDataBase
                    .child("peticiones")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("from_me")
                    .updateChildren(b);

            b = new HashMap<String, Object>();
            b.put(idComentrioPeticion, null);
            mDataBase
                    .child("peticiones")
                    .child(idUsuario)
                    .child("to_me")
                    .updateChildren(b);
        }
    }

    private void aniadirContactos() {
        Map<String,Boolean> b = new HashMap<String, Boolean>();
        b.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
        mDataBase
                .child("users")
                .child(idUsuario)
                .child("contactos")
                .setValue(b);

        b = new HashMap<String, Boolean>();
        b.put(idUsuario,true);

        mDataBase
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("contactos")
                .setValue(b);
    }

    void eliminarPeticion(){

    }




}
