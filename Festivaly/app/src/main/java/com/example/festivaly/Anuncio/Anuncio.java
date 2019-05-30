package com.example.festivaly.Anuncio;

public class Anuncio {
    String id, admin, titulo, fecha, img, contenido;

    public Anuncio() {
    }

    public Anuncio(String id, String admin, String titulo, String fecha, String img, String contenido) {
        this.id = id;
        this.admin = admin;
        this.titulo = titulo;
        this.fecha = fecha;
        this.img = img;
        this.contenido = contenido;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
