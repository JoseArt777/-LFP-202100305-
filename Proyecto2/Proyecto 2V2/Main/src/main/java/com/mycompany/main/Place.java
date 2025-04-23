/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author iosea
 */

/**
 * Clase que representa un lugar en el mapa.
 */
public class Place {
    private String name;
    private String type;
    private int x;
    private int y;
    
    /**
     * Constructor de un lugar.
     * 
     * @param name Nombre del lugar
     * @param type Tipo de lugar (playa, cueva, etc.)
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public Place(String name, String type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    /**
     * Obtiene la forma (shape) de Graphviz según el tipo de lugar.
     * 
     * @return Forma para Graphviz
     */
    public String getShape() {
    switch (type.toLowerCase()) {
        case "playa": return "ellipse";
        case "cueva": return "box";
        case "templo": return "octagon";
        case "jungla": return "parallelogram";
        case "montaña": 
        case "montana": return "triangle";
        case "pueblo": return "house";
        case "isla": return "invtriangle";
        case "río": 
        case "rio": return "hexagon";
        case "volcán": 
        case "volcan": return "doublecircle";
        case "pantano": return "trapezium";
        default: return "ellipse"; // Forma predeterminada
    }
}
    
    /**
     * Obtiene el color de relleno según el tipo de lugar.
     * 
     * @return Color para Graphviz
     */
    public String getFillColor() {
    switch (type.toLowerCase()) {
        case "playa": return "lightblue";
        case "cueva": return "gray";
        case "templo": return "gold";
        case "jungla": return "forestgreen";
        case "montaña": 
        case "montana": return "sienna";
        case "pueblo": return "burlywood";
        case "isla": return "lightgoldenrod";
        case "río": 
        case "rio": return "deepskyblue";
        case "volcán": 
        case "volcan": return "orangered";
        case "pantano": return "darkseagreen";
        default: return "white"; // Color predeterminada
    }
}
    
    @Override
    public String toString() {
        return name;
    }
}