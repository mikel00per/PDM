package com.example.notys.Tareas;

import java.io.Serializable;

public class Tarea implements Serializable {
    private String id, titulo, descripcion, fechaInicio, fechFin, hora;

    public Tarea() {
    }

    public Tarea(String id, String titulo, String descripcion, String fechaInicio, String fechFin, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechFin = fechFin;
        this.hora = hora;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechFin() {
        return fechFin;
    }

    public void setFechFin(String fechFin) {
        this.fechFin = fechFin;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
