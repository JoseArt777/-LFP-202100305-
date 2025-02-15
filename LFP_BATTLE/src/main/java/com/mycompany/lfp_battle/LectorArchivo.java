package com.mycompany.lfp_battle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivo {
    public static List<Personaje> cargarPersonajes(String rutaArchivo) throws IOException {
        List<Personaje> personajes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int numeroLinea = 0; // Para identificar la línea problemática
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                // Ignorar líneas vacías o comentarios
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Dividir la línea por comas
                String[] datos = linea.split("\\|");
                if (datos.length == 4) {
                    try {
                        String nombre = datos[0].trim();
                        int salud = Integer.parseInt(datos[1].trim());
                        int ataque = Integer.parseInt(datos[2].trim());
                        int defensa = Integer.parseInt(datos[3].trim());

                        // Crear el objeto Personaje
                        Personaje personaje = new Personaje(nombre, salud, ataque, defensa);
                        personajes.add(personaje);

                        // Mensaje de depuración
                        System.out.println("Linea " + numeroLinea + ": Cargado -> " + personaje);
                    } catch (NumberFormatException e) {
                        System.err.println("Error en la línea " + numeroLinea + ": Los valores numéricos no son válidos.");
                    }
                } else {
                    System.err.println("Error en la línea " + numeroLinea + ": La línea no tiene 4 valores.");
                }
            }
                    
        }
        return personajes;
    }
}