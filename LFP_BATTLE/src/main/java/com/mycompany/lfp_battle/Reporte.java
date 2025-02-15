package com.mycompany.lfp_battle;

import com.mycompany.lfp_battle.Personaje;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Reporte {
    public static void generarReporteAtaque(List<Personaje> personajes, String ruta) throws IOException {
        Collections.sort(personajes, Comparator.comparingInt(Personaje::getAtaque).reversed());
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("<html><body><h1>Top 5 Ataque</h1><ul>");
            for (int i = 0; i < Math.min(5, personajes.size()); i++) {
                writer.write("<li>" + personajes.get(i).getNombre() + " - " + personajes.get(i).getAtaque() + "</li>");
            }
            writer.write("</ul></body></html>");
        }
    }

    public static void generarReporteDefensa(List<Personaje> personajes, String ruta) throws IOException {
        Collections.sort(personajes, Comparator.comparingInt(Personaje::getDefensa).reversed());
        try (FileWriter writer = new FileWriter(ruta)) {
            writer.write("<html><body><h1>Top 5 Defensa</h1><ul>");
            for (int i = 0; i < Math.min(5, personajes.size()); i++) {
                writer.write("<li>" + personajes.get(i).getNombre() + " - " + personajes.get(i).getDefensa() + "</li>");
            }
            writer.write("</ul></body></html>");
        }
    }
}