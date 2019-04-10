package com.example.festivaly;

import java.util.HashMap;

public class Comentario {
    private String id, usuario, contenido, fecha;
    private HashMap<String,String> respuestas = new HashMap<>();

    public Comentario() {
    }

    public Comentario(String id, String usuario, String contenido, String fecha, HashMap<String, String> respuestas) {
        this.id = id;
        this.usuario = usuario;
        this.contenido = contenido;
        this.fecha = fecha;
        this.respuestas = respuestas;
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

    public HashMap<String, String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(HashMap<String, String> respuestas) {
        this.respuestas = respuestas;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id='" + id + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fecha='" + fecha + '\'' +
                ", respuestas=" + respuestas +
                '}';
    }
}
