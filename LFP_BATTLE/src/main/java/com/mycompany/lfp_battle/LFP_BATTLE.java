package com.mycompany.lfp_battle;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LFP_BATTLE {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Personaje> personajes = new ArrayList<>();
        boolean salir = false;

        while (!salir) {
            System.out.println("========== MENU PRINCIPAL =========");
            System.out.println("1. Cargar archivo");
            System.out.println("2. Comenzar el juego");
            System.out.println("3. Reporte de mayor ataque");
            System.out.println("4. Reporte de mayor defensa");
            System.out.println("5. About Developer");
            System.out.println("6. Salir");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese la ruta del archivo:");
                    String ruta = scanner.nextLine();
                    try {
                        personajes = LectorArchivo.cargarPersonajes(ruta);
                        System.out.println("Archivo cargado correctamente.");
                    } catch (IOException e) {
                        System.out.println("Error al cargar el archivo.");
                    }
                    break;
                case 2:
                    // Lógica para jugar
                    break;
                case 3:
                    // Generar reporte de ataque
                    break;
                case 4:
                    // Generar reporte de defensa
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