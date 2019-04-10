package com.example.festivaly;

public class Usuario {
    private String id, correo, nombre, usuario, ubicacion, desripcion, sexo, orientacion;
    private String imagenPerfil;



    public Usuario(){}

    public Usuario(String id, String correo, String nombre, String usuario, String ubicacion, String desripcion, String sexo, String orientacion, String imagenPerfil) {
        this.id = id;
        this.correo = correo;
        this.nombre = nombre;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
        this.desripcion = desripcion;
        this.sexo = sexo;
        this.orientacion = orientacion;
        this.imagenPerfil = imagenPerfil;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDesripcion() {
        return desripcion;
    }

    public void setDesripcion(String desripcion) {
        this.desripcion = desripcion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }



}
