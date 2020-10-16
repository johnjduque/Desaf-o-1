package com.JuegoLaGuayabita.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Juego {

    private static short numeroJugadores = 0;
    private static int multaPote = 100;
    private short numJugadores;
    private List<Jugador> jugadores;
    private int pote = 0;

    public Juego() {}

    public Juego(short numJugadores) {
        this.numJugadores = numJugadores;
        this.jugadores = new ArrayList<>();
        Juego.numeroJugadores += numJugadores;
    }

    public void agregarJugadores(Jugador j){
        if (this.jugadores.size() < numeroJugadores){
            this.jugadores.add(j);
        }
    }

    public String mostrarInstrucciones(){
        String instrucciones = "Bienvenido al juego la Guayabita, el juego tiene las siguientes instrucciones:\n1. Debe ingresar el número de jugadores y el valor mínimo de apuesta inicial\n2. Debe ingresar el nombre y el monto de dinero de cada jugador\n3. Debe lanzar el dado, en caso de elegir la opción -No- el juego termina\n4. Sí el jugador saca 1 o 6 entonces pierde la posibilidad de apostar y por ende cede el turno al siguiente jugador.\n" +
                "Sí por el contrario saca un número del 2 al 5 tiene la posibilidad de apostar por el pote que hay en juego. Si elige que no quiere apostar cede su turno, pero sí quiere hacerlo, debe ingresar el monto por el que desea apostar y luego tirar nuevamente el dado\n5. El jugador puede apostar por la totalidad del pote o por una parte (por ejemplo, si el pote es de $2000 el jugador puede apostar $2000 o un valor inferior)\n6. Sí el jugador saca un número mayor al que sacó en la tirada anterior entonces se lleva el dinero del pote (o la parte que apostó). Si por el contrario el jugador saca un número igual o inferior entonces tendrá que entregar lo que apostó al pote y así este irá aumentando\n7. Después de esto al siguiente jugador se le pregunta si desea lanzar el dado y comienza su flujo nuevamente\n8. Cuando a un jugador se le acabe el dinero para apostar será eliminado. El juego termina cuando ya no quede dinero en el pote o los jugadores queden sin dinero";
        return instrucciones;
    }

    public Jugador buscarJugador (short codigo){
        return this.jugadores.stream()
                .filter(jugador -> {
                    return jugador.getCodigo()==(codigo);
                })
                .findFirst()
                .orElse(null);
    }

    public int aumentarValorPote (int valor){
        pote += valor;
        return pote;
    }

    public int disminuirValorPote (int valor){
        pote -= valor;
        return pote;
    }

    public static short getNumeroJugadores() {
        return numeroJugadores;
    }

    public static int getMultaPote() {
        return multaPote;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public int getPote() {
        return pote;
    }

    public short getNumJugadores() {
        return numJugadores;
    }
}
