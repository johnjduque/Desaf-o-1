package com.JuegoLaGuayabita.App;

import com.JuegoLaGuayabita.domain.Juego;
import com.JuegoLaGuayabita.domain.Jugador;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class AppJuego {

    public static final int JUGAR = 0;
    public static final int VERINSTRUCCIONES = 1;

    public static void main(String[] args) {

        Icon icon = new ImageIcon(AppJuego.class.getResource("guayaba-300x300.jpg"));
        Icon dado1 = new ImageIcon(AppJuego.class.getResource("dado-1-138329.png"));
        Icon dado2 = new ImageIcon(AppJuego.class.getResource("dado-2-138325.png"));
        Icon dado3 = new ImageIcon(AppJuego.class.getResource("dado-3-138321.png"));
        Icon dado4 = new ImageIcon(AppJuego.class.getResource("dado-4-138319.png"));
        Icon dado5 = new ImageIcon(AppJuego.class.getResource("dado 5"));
        Icon dado6 = new ImageIcon(AppJuego.class.getResource("dado 6.png"));

        int opcionElegida = JOptionPane.showOptionDialog(null, "Bienvenido al juego de la Guayabita\n\n¿Qué deseas realizar?", "Guayabita", 0,
                0, icon, Arrays.asList("Jugar", "Ver instrucciones").toArray(), "Jugar");

        switch (opcionElegida) {

            case VERINSTRUCCIONES: {
                Juego laGuayabita = new Juego();
                JOptionPane.showMessageDialog(null, laGuayabita.mostrarInstrucciones(), "Instrucciones", 0, icon);
            }

            case JUGAR: {
                short numeroJugadores = Short.parseShort(JOptionPane.showInputDialog(null, "¿Cuántos van a jugar?", "La Guayabita", JOptionPane.QUESTION_MESSAGE));
                Juego laGuayabita = new Juego(numeroJugadores);
                int poteInicial = Short.parseShort(JOptionPane.showInputDialog(null, "¿Cuánto es la apuesta inicial?", "La Guayabita", JOptionPane.QUESTION_MESSAGE));
                laGuayabita.aumentarValorPote((poteInicial * numeroJugadores));
                int i = 1;
                while (numeroJugadores > Jugador.getNroJugadoresCreados()) {
                    String nombre = JOptionPane.showInputDialog(null, "Nombre Jugador " + i, "La Guayabita", JOptionPane.QUESTION_MESSAGE);
                    int monto = Integer.parseInt(JOptionPane.showInputDialog(null, "¿Con Cuánto dinero vas a jugar?", "La Guayabita", JOptionPane.QUESTION_MESSAGE));
                    if (monto < poteInicial) {
                        int monto1 = Integer.parseInt(JOptionPane.showInputDialog(null, "Su cantidad de dinero debe ser mayor o igual a: " + poteInicial, "La Guayabita", JOptionPane.WARNING_MESSAGE));
                        monto = monto1;
                    }
                    laGuayabita.agregarJugadores(Jugador.crearJugador(nombre, monto));
                    i++;
                }
                laGuayabita.getJugadores().forEach(jugador -> {
                    jugador.apostarDinero(poteInicial);
                });

                while (laGuayabita.getPote() > 0 && laGuayabita.getJugadores().stream().mapToInt(j -> j.getDineroActual()).sum() > 0){
                    int l = 0;
                    while (l <= laGuayabita.getJugadores().size()) {
                        Jugador j = laGuayabita.buscarJugador((short) (l + 1));
                        if (j == null){
                            break;
                        }
                        if (j.getDineroActual() > 0){
                            int lanzar = JOptionPane.showConfirmDialog(null, j.getNombre() + ", el pote actual es de $" + laGuayabita.getPote() + " ¿Desea lanzar el dado?", "La Guayabita", 0, 0, icon);
                            if (lanzar == 1) {
                                JOptionPane.showMessageDialog(null, "El juego terminó, gracias a todos por jugar", "Fin del juego", 0, icon);
                                laGuayabita.getJugadores().forEach(jugador -> {
                                    JOptionPane.showMessageDialog(null, "El juego termino, gracias por participar\nEl pote queda en: "+ laGuayabita.getPote()+ "\n" + jugador.getNombre() + " su saldo quedo en: " + jugador.getDineroActual(), "La Guayabita", 0, icon);
                                });
                                return;
                            } else {
                                byte dado = j.lanzarDado();
                                if (dado == 1 || dado == 6){
                                    if (dado == 1){
                                        JOptionPane.showMessageDialog(null, "Cede el turno\nTiene una multa por $100", "La Guayabita", 0, dado1);
                                        j.apostarDinero(Juego.getMultaPote());
                                        laGuayabita.aumentarValorPote(Juego.getMultaPote());
                                        l += 1;
                                        continue;
                                    }
                                    else if (dado == 6){
                                        JOptionPane.showMessageDialog(null, "Cede el turno\nTiene una multa por $100", "La Guayabita", 0, dado6);
                                        j.apostarDinero(Juego.getMultaPote());
                                        laGuayabita.aumentarValorPote(Juego.getMultaPote());
                                        l += 1;
                                        continue;
                                    }

                                }
                                else{
                                    if (dado == 2){
                                        byte dadoActual = dado;
                                        int apostar = JOptionPane.showConfirmDialog(null, j.getNombre() + ", este es su dado\n¿Desea hacer una apuesta?\nSu saldo es de: $" + j.getDineroActual(), "La Guayabita", 0, 0, dado2);
                                        if (apostar == 0){
                                            int monto = Integer.parseInt(JOptionPane.showInputDialog(null, j.getNombre()+ " su saldo es de: $"+ j.getDineroActual() + "\nEl acumulado del pote es: $"+ laGuayabita.getPote() , "¿Cuánto dinero vas a apostar?", JOptionPane.QUESTION_MESSAGE));
                                            if (monto > laGuayabita.getPote() || monto > j.getDineroActual()){
                                                do {
                                                    int monto1 = Integer.parseInt(JOptionPane.showInputDialog(null, "Su apuesta debe ser menor o igual a: $" + laGuayabita.getPote() + "\nRecuerde que su saldo es de: $"+j.getDineroActual() + "\nRecuerde no apostar un valor mayor a su saldo actual", "La Guayabita", JOptionPane.WARNING_MESSAGE));
                                                    monto = monto1;
                                                }while (monto > laGuayabita.getPote() || monto > j.getDineroActual());
                                            }
                                            byte dadoApuesta = j.lanzarDado();
                                            if (dadoActual < dadoApuesta){
                                                j.ganarDinero(monto);
                                                laGuayabita.disminuirValorPote(monto);
                                                if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 5){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado6);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                            else{
                                                j.apostarDinero(monto);
                                                laGuayabita.aumentarValorPote(monto);
                                                if (dadoApuesta == 1){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado1);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 2){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado2);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                        }
                                        else {
                                            l += 1;
                                            continue;
                                        }
                                    }
                                    else if (dado == 3){
                                        byte dadoActual = dado;
                                        int apostar = JOptionPane.showConfirmDialog(null, j.getNombre() + ", este es su dado\n¿Desea hacer una apuesta?\nSu saldo es de: $" + j.getDineroActual(), "La Guayabita", 0, 0, dado3);
                                        if (apostar == 0){
                                            int monto = Integer.parseInt(JOptionPane.showInputDialog(null, j.getNombre()+ " su saldo es de: $"+ j.getDineroActual() + "\nEl acumulado del pote es: $"+ laGuayabita.getPote() , "¿Cuánto dinero vas a apostar?", JOptionPane.QUESTION_MESSAGE));
                                            if (monto > laGuayabita.getPote() || monto > j.getDineroActual()){
                                                do {
                                                    int monto1 = Integer.parseInt(JOptionPane.showInputDialog(null, "Su apuesta debe ser menor o igual a: $" + laGuayabita.getPote() + "\nRecuerde que su saldo es de: $"+j.getDineroActual() + "\nRecuerde no apostar un valor mayor a su saldo actual", "La Guayabita", JOptionPane.WARNING_MESSAGE));
                                                    monto = monto1;
                                                }while (monto > laGuayabita.getPote() || monto > j.getDineroActual());
                                            }
                                            byte dadoApuesta = j.lanzarDado();
                                            if (dadoActual < dadoApuesta){
                                                j.ganarDinero(monto);
                                                laGuayabita.disminuirValorPote(monto);
                                                if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 5){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado6);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                            else{
                                                j.apostarDinero(monto);
                                                laGuayabita.aumentarValorPote(monto);
                                                if (dadoApuesta == 1){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado1);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 2){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado2);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                        }
                                        else {
                                            l += 1;
                                            continue;
                                        }
                                    }
                                    else if (dado == 4){
                                        byte dadoActual = dado;
                                        int apostar = JOptionPane.showConfirmDialog(null, j.getNombre() + ", este es su dado\n¿Desea hacer una apuesta?\nSu saldo es de: $" + j.getDineroActual(), "La Guayabita", 0, 0, dado4);
                                        if (apostar == 0){
                                            int monto = Integer.parseInt(JOptionPane.showInputDialog(null, j.getNombre()+ " su saldo es de: $"+ j.getDineroActual() + "\nEl acumulado del pote es: $"+ laGuayabita.getPote() , "¿Cuánto dinero vas a apostar?", JOptionPane.QUESTION_MESSAGE));
                                            if (monto > laGuayabita.getPote() || monto > j.getDineroActual()){
                                                do {
                                                    int monto1 = Integer.parseInt(JOptionPane.showInputDialog(null, "Su apuesta debe ser menor o igual a: $" + laGuayabita.getPote() + "\nRecuerde que su saldo es de: $"+j.getDineroActual() + "\nRecuerde no apostar un valor mayor a su saldo actual", "La Guayabita", JOptionPane.WARNING_MESSAGE));
                                                    monto = monto1;
                                                }while (monto > laGuayabita.getPote() || monto > j.getDineroActual());
                                            }
                                            byte dadoApuesta = j.lanzarDado();
                                            if (dadoActual < dadoApuesta){
                                                j.ganarDinero(monto);
                                                laGuayabita.disminuirValorPote(monto);
                                                if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 5){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado6);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                            else{
                                                j.apostarDinero(monto);
                                                laGuayabita.aumentarValorPote(monto);
                                                if (dadoApuesta == 1){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado1);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 2){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado2);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                        }
                                        else {
                                            l += 1;
                                            continue;
                                        }
                                    }
                                    else if (dado == 5){
                                        byte dadoActual = dado;
                                        int apostar = JOptionPane.showConfirmDialog(null, j.getNombre() + ", este es su dado\n¿Desea hacer una apuesta?\nSu saldo es de: $" + j.getDineroActual(), "La Guayabita", 0, 0, dado5);
                                        if (apostar == 0){
                                            int monto = Integer.parseInt(JOptionPane.showInputDialog(null, j.getNombre()+ " su saldo es de: $"+ j.getDineroActual() + "\nEl acumulado del pote es: $"+ laGuayabita.getPote() , "¿Cuánto dinero vas a apostar?", JOptionPane.QUESTION_MESSAGE));
                                            if (monto > laGuayabita.getPote() || monto > j.getDineroActual()){
                                                do {
                                                    int monto1 = Integer.parseInt(JOptionPane.showInputDialog(null, "Su apuesta debe ser menor o igual a: $" + laGuayabita.getPote() + "\nRecuerde que su saldo es de: $"+j.getDineroActual() + "\nRecuerde no apostar un valor mayor a su saldo actual", "La Guayabita", JOptionPane.WARNING_MESSAGE));
                                                    monto = monto1;
                                                }while (monto > laGuayabita.getPote() || monto > j.getDineroActual());
                                            }
                                            byte dadoApuesta = j.lanzarDado();
                                            if (dadoActual < dadoApuesta){
                                                j.ganarDinero(monto);
                                                laGuayabita.disminuirValorPote(monto);
                                                if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 5){
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Felicidades acaba de ganar la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado6);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                            else{
                                                j.apostarDinero(monto);
                                                laGuayabita.aumentarValorPote(monto);
                                                if (dadoApuesta == 1){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado1);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 2){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado2);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 3){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado3);
                                                    l += 1;
                                                    continue;
                                                }
                                                else if (dadoApuesta == 4){
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado4);
                                                    l += 1;
                                                    continue;
                                                }
                                                else {
                                                    JOptionPane.showMessageDialog(null, "Que pesar acaba de perder la apuesta\nSu saldo actual es de: $"+ j.getDineroActual(), "La Guayabita", 0, dado5);
                                                    l += 1;
                                                    continue;
                                                }
                                            }
                                        }
                                        else {
                                            l += 1;
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            l += 1;
                            continue;
                        }
                    }
                }
                laGuayabita.getJugadores().forEach(jugador -> {
                    JOptionPane.showMessageDialog(null, "El juego termino, gracias por participar\nEl pote queda en: "+ laGuayabita.getPote()+ "\n" + jugador.getNombre() + " su saldo quedo en: $" + jugador.getDineroActual(), "La Guayabita", 0, icon);
                });


            }
        }
    }
}