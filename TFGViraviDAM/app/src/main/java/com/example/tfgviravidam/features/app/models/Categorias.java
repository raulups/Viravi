package com.example.tfgviravidam.features.app.models;

public class Categorias {
    String nombre;
    String pic;

    String color;


    public Categorias(String nombre, String pic, String color) {
        this.nombre = nombre;
        this.pic = pic;
        this.color = color;

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}

