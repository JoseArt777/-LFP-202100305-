/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

/**
 * Representa un lugar en el mapa narrativo
 */
public class Lugar {
    private String nombre;
    private String tipo;
    private int x;
    private int y;
    
    public Lugar(String nombre, String tipo, int x, int y) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Obtiene la forma (shape) para Graphviz según el tipo de lugar
     */
    public String obtenerForma() {
        switch (tipo.toLowerCase()) {
            case "playa": return "ellipse";
            case "cueva": return "box";
            case "templo": return "octagon";
            case "jungla": return "parallelogram";
            case "montaña": return "triangle";
            case "pueblo": return "house";
            case "isla": return "invtriangle";
            case "río": return "hexagon";
            case "volcán": return "doublecircle";
            case "pantano": return "trapezium";
            default: return "ellipse";
        }
    }
    
    /**
     * Obtiene el color de relleno para Graphviz según el tipo de lugar
     */
    public String obtenerColor() {
        switch (tipo.toLowerCase()) {
            case "playa": return "lightblue";
            case "cueva": return "gray";
            case "templo": return "gold";
            case "jungla": return "forestgreen";
            case "montaña": return "sienna";
            case "pueblo": return "burlywood";
            case "isla": return "lightgoldenrod";
            case "río": return "deepskyblue";
            case "volcán": return "orangered";
            case "pantano": return "darkseagreen";
            default: return "white";
        }
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getX() { return x; }
    public int getY() { return y; }
    
    @Override
    public String toString() {
        return "Lugar{nombre='" + nombre + "', tipo='" + tipo + "', x=" + x + ", y=" + y + '}';
    }
}