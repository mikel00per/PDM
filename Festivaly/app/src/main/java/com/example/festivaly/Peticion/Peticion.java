package com.example.festivaly.Peticion;


public class Peticion {
    private String id, from, to, estado,idComentarioPeticion;

    public Peticion() {
    }

    public Peticion(String id, String from, String to, String estado, String idComentarioPeticion) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.estado = estado;
        this.idComentarioPeticion = idComentarioPeticion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIdComentarioPeticion() {
        return idComentarioPeticion;
    }

    public void setIdComentarioPeticion(String idComentarioPeticion) {
        this.idComentarioPeticion = idComentarioPeticion;
    }

    @Override
    public String toString() {
        return "Peticion{" +
                "id='" + id + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", estado='" + estado + '\'' +
                ", idComentarioPeticion='" + idComentarioPeticion + '\'' +
                '}';
    }
}
