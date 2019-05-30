package com.example.festivaly.Comentarios;

import java.util.HashMap;

public class Comentario {
    private String id, usuario, contenido, fecha, img;
    private Boolean es_peticion;

    public Comentario() {
    }

    public Comentario(String id, String usuario, String contenido, String fecha, String img, Boolean es_peticion) {
        this.id = id;
        this.usuario = usuario;
        this.contenido = contenido;
        this.fecha = fecha;
        this.img = img;
        this.es_peticion = es_peticion;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Boolean getEs_peticion() {
        return es_peticion;
    }

    public void setEs_peticion(Boolean es_peticion) {
        this.es_peticion = es_peticion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    @Override
    public String toString() {
        return "Comentario{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fecha='" + fecha + '\'' +
                '}';
    }
}
