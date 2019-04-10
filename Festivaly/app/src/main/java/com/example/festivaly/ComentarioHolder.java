package com.example.festivaly;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ComentarioHolder extends RecyclerView.ViewHolder {
    public LinearLayout root;
    public ImageView imgPerfil;
    public Button botonRespuesta, botonFavorito, botonMensajePrivado;
    public TextView contenido;

    public ComentarioHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        imgPerfil = itemView.findViewById(R.id.imgPerfilComentario);
        botonRespuesta = itemView.findViewById(R.id.botonRespuesta);
        botonFavorito = itemView.findViewById(R.id.botonFavorito);
        botonMensajePrivado = itemView.findViewById(R.id.botonMensajePrivado);
        contenido = itemView.findViewById(R.id.contenidoComentario);
    }

    public void setImgPerfil(String name){
        // TODO: Poner imag al tweet
    }

    public void setContenido(String contenido){
        this.contenido.setText(contenido);
    }


}
