package com.mycompany.lfp_battle;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author iosea
 */
import com.mycompany.lfp_battle.Personaje;
import java.util.ArrayList;
import java.util.List;

public class Torneo {
    public static Personaje jugarRonda(Personaje jugador1, Personaje jugador2) {
        while (jugador1.estaVivo() && jugador2.estaVivo()) {
            int danoJ1 = jugador1.getAtaque() - jugador2.getDefensa();
            if (danoJ1 > 0) {
                jugador2.recibirDano(danoJ1);
                System.out.println(jugador1.getNombre() + " ataca a " + jugador2.getNombre() + " causando " + danoJ1 + " de daño.");
            } else {
                System.out.println(jugador1.getNombre() + " no pudo causar daño.");
            }

            if (!jugador2.estaVivo()) break;

            int danoJ2 = jugador2.getAtaque() - jugador1.getDefensa();
            if (danoJ2 > 0) {
                jugador1.recibirDano(danoJ2);
                System.out.println(jugador2.getNombre() + " ataca a " + jugador1.getNombre() + " causando " + danoJ2 + " de daño.");
            } else {
                System.out.println(jugador2.getNombre() + " no pudo causar daño.");
            }
        }

        return jugador1.estaVivo() ? jugador1 : jugador2;
    }

    public static Personaje jugarTorneo(List<Personaje> personajes) {
        while (personajes.size() > 1) {
            List<Personaje> sobrevivientes = new ArrayList<>();
            for (int i = 0; i < personajes.size(); i += 2) {
                if (i + 1 < personajes.size()) {
                    Personaje ganador = jugarRonda(personajes.get(i), personajes.get(i + 1));
                    sobrevivientes.add(ganador);
                } else {
                    sobrevivientes.add(personajes.get(i)); // Avanza automáticamente
                }
            }
            personajes = sobrevivientes;
        }
        return personajes.get(0);
    }
}