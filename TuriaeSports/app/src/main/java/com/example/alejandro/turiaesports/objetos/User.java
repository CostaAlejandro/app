package com.example.alejandro.turiaesports.objetos;

/**
 * Created by acosta on 27/03/2018.
 */

public class User {
    private String dorsal;
    private String Posicion;

    public User() {

    }

    public User(String dorsal, String Posicion) {
        this.dorsal = dorsal;
        this.Posicion = Posicion;
    }

    public String getDorsal() {
        return dorsal;
    }

    public void setDorsal(String dorsal) {
        this.dorsal = dorsal;
    }

    public String getPosicion() {
        return Posicion;
    }

    public void setPosicion(String Posicion) {
        this.Posicion = Posicion;
    }

    @Override
    public String toString() {
        return "User{" +
                "dorsal= '" + dorsal +'\'' +
                ", posicion= '" + Posicion + '\'' +
                '}';
    }
}
