package com.example.festivaly.Comentarios;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.festivaly.R;
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
    public ImageView imgPerfil, imgContenido;
    public Button botonFavorito, botonPeticionAmistad;
    public TextView contenido, fechaComentario, nombreComentario, userComentario;

    public ComentarioHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        imgPerfil = itemView.findViewById(R.id.imgPerfilComentario);
        botonFavorito = itemView.findViewById(R.id.botonFavorito);
        botonPeticionAmistad = itemView.findViewById(R.id.botonPetecionAmistad);
        imgContenido = itemView.findViewById(R.id.imagenComentario);
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

    public void setImgContenido(String url){
        if (!url.isEmpty()){
            imgContenido.setVisibility(View.VISIBLE);
            Picasso
                    .get()
                    .load(url)
                    .into(imgContenido);
        }
    }

    public void setBotonPeticionAmistad(boolean esPeticion){
        if (esPeticion){
            botonPeticionAmistad.setVisibility(View.VISIBLE);
        }else{
            botonPeticionAmistad.setVisibility(View.GONE);
        }
    }

}
