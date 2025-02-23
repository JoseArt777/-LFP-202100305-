package com.mycompany.lfp_battle;

import java.util.ArrayList;
import java.util.List;

public class Torneo {
    public static Personaje jugarRonda(Personaje jugador1, Personaje jugador2) {
        System.out.println("=== COMIENZA EL ENFRENTAMIENTO ===");
        System.out.println(jugador1.getNombre() + " vs " + jugador2.getNombre());

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

        Personaje ganador = jugador1.estaVivo() ? jugador1 : jugador2;
        System.out.println("=== FIN DEL ENFRENTAMIENTO ===");
        System.out.println("El ganador es: " + ganador.getNombre());
        return ganador;
    }

    public static Personaje jugarTorneo(List<Personaje> personajes) {
        int ronda = 1; // Contador para numerar las rondas

        while (personajes.size() > 1) {
            System.out.println("\n=== RONDA " + ronda + " ===");

            List<Personaje> sobrevivientes = new ArrayList<>();
            for (int i = 0; i < personajes.size(); i += 2) {
                if (i + 1 < personajes.size()) {
                    System.out.println("Enfrentamiento entre " + personajes.get(i).getNombre() + " y " + personajes.get(i + 1).getNombre());
                    Personaje ganador = jugarRonda(personajes.get(i), personajes.get(i + 1));
                    sobrevivientes.add(ganador);
                } else {
                    System.out.println(personajes.get(i).getNombre() + " avanza automáticamente a la siguiente ronda.");
                    sobrevivientes.add(personajes.get(i)); // Avanza automáticamente
                }
            }

            System.out.println("=== SOBREVIVIENTES DE LA RONDA " + ronda + " ===");
            for (Personaje sobreviviente : sobrevivientes) {
                System.out.println("- " + sobreviviente.getNombre());
            }

            personajes = sobrevivientes;
            ronda++;
        }

        Personaje campeon = personajes.get(0);
        System.out.println("\n=== CAMPEÓN DEL TORNEO ===");
        System.out.println("¡El campeón es: " + campeon.getNombre() + "!");
        return campeon;
    }
}