package com.example.tfgviravidam.features.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Evento implements Parcelable {

    private String nombre, descripcion, dayInicio, monthInicio, yearInicio, dayFin, monthFin, yearFin, usuarioCreador, ciudad, categoria, imagen;
    private ArrayList<String> usuariosApuntados;

    private String fechaInicio = dayInicio+"-"+monthInicio+"-"+yearInicio;
    private String fechaFin = dayFin+"-"+monthFin+"-"+yearFin;

    protected Evento(Parcel in) {
        nombre = in.readString();
        descripcion = in.readString();
        fechaInicio = in.readString();
        fechaFin = in.readString();
        usuarioCreador = in.readString();
        ciudad = in.readString();
        categoria = in.readString();
        imagen = in.readString();
        usuariosApuntados = in.createStringArrayList();
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    public Evento() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDayInicio() {
        return dayInicio;
    }

    public void setDayInicio(String dayInicio) {
        this.dayInicio = dayInicio;
    }

    public String getMonthInicio() {
        return monthInicio;
    }

    public void setMonthInicio(String monthInicio) {
        this.monthInicio = monthInicio;
    }

    public String getYearInicio() {
        return yearInicio;
    }

    public void setYearInicio(String yearInicio) {
        this.yearInicio = yearInicio;
    }

    public String getDayFin() {
        return dayFin;
    }

    public void setDayFin(String dayFin) {
        this.dayFin = dayFin;
    }

    public String getMonthFin() {
        return monthFin;
    }

    public void setMonthFin(String monthFin) {
        this.monthFin = monthFin;
    }

    public String getYearFin() {
        return yearFin;
    }

    public void setYearFin(String yearFin) {
        this.yearFin = yearFin;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public ArrayList<String> getUsuariosApuntados() {
        return usuariosApuntados;
    }

    public void setUsuariosApuntados(ArrayList<String> usuariosApuntados) {
        this.usuariosApuntados = usuariosApuntados;
    }

    public Evento(String nombre, String descripcion, String fechaInicio, String fechaFin, String usuarioCreador, String ciudad, String categoria, String imagen, ArrayList<String> usuariosApuntados) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuarioCreador = usuarioCreador;
        this.ciudad = ciudad;
        this.categoria = categoria;
        this.imagen = imagen;
        this.usuariosApuntados = usuariosApuntados;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaInicio='" + fechaInicio + '\'' +
                ", fechaFin='" + fechaFin + '\'' +
                ", usuarioCreador='" + usuarioCreador + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", categoria='" + categoria + '\'' +
                ", imagen='" + imagen + '\'' +
                ", usuariosApuntados=" + usuariosApuntados +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(descripcion);
        parcel.writeString(fechaInicio);
        parcel.writeString(fechaFin);
        parcel.writeString(usuarioCreador);
        parcel.writeString(ciudad);
        parcel.writeString(categoria);
        parcel.writeString(imagen);
        parcel.writeStringList(usuariosApuntados);
    }
}
