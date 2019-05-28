package com.example.notys.Notas;

import java.io.Serializable;

public class Nota implements Serializable {
    private String id, contenido,color;
    private Boolean oculto;

    public Nota() {
    }

    public Nota(String id, String contenido, String color, Boolean oculto) {
        this.id = id;
        this.contenido = contenido;
        this.color = color;
        this.oculto = oculto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getOculto() {
        return oculto;
    }

    public void setOculto(Boolean oculto) {
        this.oculto = oculto;
    }
}
