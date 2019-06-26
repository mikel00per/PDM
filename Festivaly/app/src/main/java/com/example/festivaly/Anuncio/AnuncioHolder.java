package com.example.festivaly.Anuncio;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.festivaly.R;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AnuncioHolder extends RecyclerView.ViewHolder {

    public LinearLayout root;
    public ImageView imgAdmin,imgContenido;
    public ImageButton botonFavorito;
    public TextView titulo, fecha, contenido;

    public AnuncioHolder(View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.comentario);
        imgAdmin = itemView.findViewById(R.id.admin_img);
        titulo = itemView.findViewById(R.id.titulo_anuncio);
        fecha = itemView.findViewById(R.id.fecha_anuncio);
        imgContenido = itemView.findViewById(R.id.img_contenido);
        contenido = itemView.findViewById(R.id.contenido_anuncio);
        botonFavorito = itemView.findViewById(R.id.tablon_boton);
    }

    public void setImgAdmin(String img){
        if (img != "")
            Picasso
                    .get()
                    .load(img)
                    .transform(new CropCircleTransformation())
                    .into(imgAdmin);
    }

    public void setTitulo(String t){
        titulo.setText(t);
    }

    public void setFecha(String f){
        fecha.setText(f);
    }

    public void setImgContenido(String img){
        Log.d("setImg",img);

        if (!img.isEmpty()){
            imgContenido.setVisibility(View.VISIBLE);
            Picasso
                    .get()
                    .load(img)
                    .into(imgContenido);
        }else{
            imgContenido.setVisibility(View.GONE);
        }
    }

    public void setContenido(String c){
        this.contenido.setText(c);
    }

}