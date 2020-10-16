package com.JuegoLaGuayabita.domain;

public class Jugador {

    private static int nroJugadoresCreados = 0;
    private int codigo;
    private String nombre;
    private int dineroActual;

    public Jugador() {}

    public Jugador(String nombre, int dineroActual) {
        this.nombre = nombre;
        this.dineroActual = dineroActual;
        this.codigo = nroJugadoresCreados + 1;

        Jugador.nroJugadoresCreados++;
    }

    public static Jugador crearJugador(String nombre, int dineroActual) {
        if (nroJugadoresCreados < Juego.getNumeroJugadores()) {
            return new Jugador(nombre, dineroActual);
        }
        else {
            return null;
        }
    }

    public byte lanzarDado(){
        byte dado = (byte) (1 + (Math.random() * 6));
        return dado;
    }

    public void apostarDinero (int valor){
        this.dineroActual -= valor;
    }

    public void ganarDinero (int valor){
        this.dineroActual += valor;
    }

    public static int getNroJugadoresCreados() {
        return nroJugadoresCreados;
    }

    public int getDineroActual() {
        return dineroActual;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}
