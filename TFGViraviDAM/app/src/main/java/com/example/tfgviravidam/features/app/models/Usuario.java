package com.example.tfgviravidam.features.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Usuario  implements Parcelable {

    private String nombreUsuario, nombre, telefono, fechaNacimiento, correo, contrasenya, fotoPerfil,userKey;
    private ArrayList<String> eventosApuntado, eventosCreados,seguidores, seguidos;

    public Usuario(String nombreUsuario, String fotoPerfil, String userKey) {
        this.nombreUsuario = nombreUsuario;
        this.fotoPerfil = fotoPerfil;
        this.userKey = userKey;
    }

    public Usuario(String nombreUsuario, String nombre, String telefono, String fechaNacimiento, String correo, String contrasenya, String fotoPerfil, String userKey, ArrayList<String> eventosApuntado, ArrayList<String> eventosCreados, ArrayList<String> seguidores, ArrayList<String> seguidos) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.fotoPerfil = fotoPerfil;
        this.userKey = userKey;
        this.eventosApuntado = eventosApuntado;
        this.eventosCreados = eventosCreados;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
    }

    public Usuario(String contrasenya, String correo, ArrayList<String> eventosApuntados, ArrayList<String> eventosCreados, String fechaNacimiento, String fotoPerfil, String nombre, String nombreUsuario, ArrayList<String> seguidores, ArrayList<String> seguidos, String telefono) {
        this.nombreUsuario = nombreUsuario;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.fotoPerfil = fotoPerfil;
        this.eventosApuntado = eventosApuntados;
        this.eventosCreados = eventosCreados;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
    }

    protected Usuario(Parcel in) {
        nombreUsuario = in.readString();
        nombre = in.readString();
        telefono = in.readString();
        fechaNacimiento = in.readString();
        correo = in.readString();
        contrasenya = in.readString();
        fotoPerfil = in.readString();
        userKey = in.readString();
        eventosApuntado = in.createStringArrayList();
        eventosCreados = in.createStringArrayList();
        seguidores = in.createStringArrayList();
        seguidos = in.createStringArrayList();
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public ArrayList<String> getEventosApuntado() {
        return eventosApuntado;
    }

    public void setEventosApuntado(ArrayList<String> eventosApuntado) {
        this.eventosApuntado = eventosApuntado;
    }

    public ArrayList<String> getEventosCreados() {
        return eventosCreados;
    }

    public void setEventosCreados(ArrayList<String> eventosCreados) {
        this.eventosCreados = eventosCreados;
    }

    public ArrayList<String> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(ArrayList<String> seguidores) {
        this.seguidores = seguidores;
    }

    public ArrayList<String> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(ArrayList<String> seguidos) {
        this.seguidos = seguidos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasenya='" + contrasenya + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", userKey='" + userKey + '\'' +
                ", eventosApuntado=" + eventosApuntado +
                ", eventosCreados=" + eventosCreados +
                ", seguidores=" + seguidores +
                ", seguidos=" + seguidos +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nombreUsuario);
        parcel.writeString(nombre);
        parcel.writeString(telefono);
        parcel.writeString(fechaNacimiento);
        parcel.writeString(correo);
        parcel.writeString(contrasenya);
        parcel.writeString(fotoPerfil);
        parcel.writeString(userKey);
        parcel.writeStringList(eventosApuntado);
        parcel.writeStringList(eventosCreados);
        parcel.writeStringList(seguidores);
        parcel.writeStringList(seguidos);
    }
}
