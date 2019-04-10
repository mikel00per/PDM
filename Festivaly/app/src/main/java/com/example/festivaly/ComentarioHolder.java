package com.example.festivaly;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ComentarioHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public ImageView imgPerfil;
    public Button botonRespuesta, botonFavorito, botonMensajePrivado;
    public TextView contenido, fechaComentario, nombreComentario, userComentario;

    public ComentarioHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        imgPerfil = itemView.findViewById(R.id.imgPerfilComentario);
        botonRespuesta = itemView.findViewById(R.id.botonRespuesta);
        botonFavorito = itemView.findViewById(R.id.botonFavorito);
        botonMensajePrivado = itemView.findViewById(R.id.botonMensajePrivado);
        contenido = itemView.findViewById(R.id.contenidoComentario);
        fechaComentario = itemView.findViewById(R.id.fechaComentario);
        nombreComentario = itemView.findViewById(R.id.nombreComentario);
        userComentario = itemView.findViewById(R.id.usuarioComentario);
    }

    public void setNombe(String name){
        nombreComentario.setText(name);
    }

    public void setUser(String username){
        userComentario.setText(username);
    }

    public void setFecha(String fecha){
        fechaComentario.setText(fecha);
    }



    public void setImgPerfil(String img){
        Picasso
                .get()
                .load(img)
                .transform(new CropCircleTransformation())
                .into(imgPerfil);

    }

    public void setContenido(String contenido){
        this.contenido.setText(contenido);
    }




}
