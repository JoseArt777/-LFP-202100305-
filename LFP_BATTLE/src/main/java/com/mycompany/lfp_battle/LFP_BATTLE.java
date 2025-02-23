package com.mycompany.lfp_battle;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LFP_BATTLE {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Personaje> personajes = new ArrayList<>();
        boolean salir = false;
           String rutaAtaque = "output/reporte_ataque.html";
        String rutaDefensa = "output/reporte_defensa.html";

        while (!salir) {
            System.out.println("========== MENU PRINCIPAL =========");
            System.out.println("1. Cargar archivo");
            System.out.println("2. Comenzar el juego");
            System.out.println("3. Reporte de mayor ataque");
            System.out.println("4. Reporte de mayor defensa");
            System.out.println("5. About Developer");
            System.out.println("6. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpia el buffer

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese la ruta del archivo:");
                    String ruta = scanner.nextLine();
                    try {
                    personajes = LectorArchivo.cargarPersonajes(ruta);
                     if (!personajes.isEmpty()) {
                        System.out.println("Archivo cargado correctamente.");
                        String rutaHTML = "output/personajes.html"; // Ruta donde se guardará el html
                        LectorArchivo.generarHTML(personajes, rutaHTML);
                        System.out.println("Archivo HTML generado en: " + new File(rutaHTML).getAbsolutePath());
                    } else {
                        System.out.println("No se encontraron personajes en el archivo.");
                        }
                        } catch (IOException e) {
                          System.out.println("Error al cargar el archivo.");
                        }
                        break;

                case 2:
                    if (personajes.isEmpty()) {
                        System.out.println("No hay personajes cargados. Por favor, cargue un archivo primero.");
                    } else {
                        Personaje campeon = Torneo.jugarTorneo(personajes);
                        System.out.println("¡El campeón es: " + campeon.getNombre() + "!");
                    }                    
                    break;
                case 3:
                    // Generar reporte de mayor ataque
                    if (personajes.isEmpty()) {
                        System.out.println("No hay personajes cargados. Por favor, cargue un archivo primero.");
                    } else {
                        try {
                            Reporte.generarReporteAtaque(personajes, rutaAtaque);
                            System.out.println("Reporte de mayor ataque generado en: " + rutaAtaque);
                        } catch (IOException e) {
                            System.out.println("Error al generar el reporte de ataque.");
                        }
                    }
                    break;

                case 4:
                 // Generar reporte de mayor defensa
                    if (personajes.isEmpty()) {
                        System.out.println("No hay personajes cargados. Por favor, cargue un archivo primero.");
                    } else {
                        try {
                            Reporte.generarReporteDefensa(personajes, rutaDefensa);
                            System.out.println("Reporte de mayor defensa generado correctamente en: " + rutaDefensa);
                        } catch (IOException e) {
                            System.out.println("Error al generar el reporte de defensa.");
                        }
                    }
                    break;
                case 5:
                    System.out.println("===================================");
                    System.out.println("   INFORMACION DE DESARROLLADOR");
                    System.out.println("===================================");
                    System.out.println("Nombre: Jose Alexander Lopez Lopez");
                    System.out.println("Carnet: 202100305");
                    System.out.println("===================================");
                    break;
                case 6:
                    salir = true;
                    System.out.println("Saliendo...");
                    break;
                    
                default:
                    System.out.println("Opcion invalida.");
            }
        }
        scanner.close();
    }
}