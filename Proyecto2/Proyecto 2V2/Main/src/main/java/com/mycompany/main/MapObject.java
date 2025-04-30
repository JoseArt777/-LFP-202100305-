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
 * Clase que representa un objeto en el mapa.
 */
public class MapObject {
    private String name;
    private String type;
    private String placeId; // ID del lugar
    private int x; // Coordenada X 
    private int y; // Coordenada Y
    private boolean isAtPlace; 
    
    /**
     * Constructor para un objeto en un lugar específico.
     * 
     * @param name Nombre del objeto
     * @param type Tipo de objeto
     * @param placeId ID del lugar donde se encuentra
     */
    public MapObject(String name, String type, String placeId) {
        this.name = name;
        this.type = type;
        this.placeId = placeId;
        this.isAtPlace = true;
    }
    
    /**
     * Constructor para un objeto en coordenadas específicas.
     * 
     * @param name Nombre del objeto
     * @param type Tipo de objeto
     * @param x Coordenada X
     * @param y Coordenada Y
     */
    public MapObject(String name, String type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
        this.isAtPlace = false;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getType() {
        return type;
    }
    
    public String getPlaceId() {
        return placeId;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public boolean isAtPlace() {
        return isAtPlace;
    }
    
    /**
     * Obtiene la forma (shape) de Graphviz según el tipo de objeto.
     * 
     * @return Forma para Graphviz
     */
    public String getShape() {
    switch (type.toLowerCase()) {
        case "tesoro": return "box3d";
        case "llave": return "pentagon";
        case "arma": return "diamond";
        case "objeto-mágico": 
        case "objeto-magico": return "component";
        case "poción": 
        case "pocion": return "cylinder";
        case "trampa": return "hexagon";
        case "libro": return "note";
        case "herramienta": return "folder";
        case "bandera": return "tab";
        case "gema": return "egg";
        default: return "box";
    }
}
    
    /**
     * Obtiene el color de relleno según el tipo de objeto.
     * 
     * @return Color para Graphviz
     */
    public String getFillColor() {
    switch (type.toLowerCase()) {
        case "tesoro": return "gold";
        case "llave": return "lightsteelblue";
        case "arma": return "orangered";
        case "objeto-mágico": 
        case "objeto-magico": return "violet";
        case "poción": 
        case "pocion": return "plum";
        case "trampa": return "crimson";
        case "libro": return "navajowhite";
        case "herramienta": return "darkkhaki";
        case "bandera": return "white";
        case "gema": return "deepskyblue";
        default: return "white"; // Color predeterminado
    }
}
    
    
    public String getEmoji() {
    switch (type.toLowerCase()) {
        case "tesoro": return "\uD83C\uDF81"; // 🎁
        case "llave": return "\uD83D\uDD11"; // 🔑
        case "arma": return "\uD83D\uDDE1\uFE0F"; // 🗡️
        case "objeto-mágico": 
        case "objeto-magico": return "\u2728"; // ✨
        case "poción": 
        case "pocion": return "\u2697\uFE0F"; // ⚗️
        case "trampa": return "\uD83D\uDCA3"; // 💣
        case "libro": return "\uD83D\uDCD5"; // 📕
        case "herramienta": return "\uD83D\uDEE0\uFE0F"; // 🛠️
        case "bandera": return "\uD83D\uDEA9"; // 🚩
        case "gema": return "\uD83D\uDC8E"; // 💎
        default: return "";
    }
}
    
    @Override
    public String toString() {
        if (isAtPlace) {
            return name + " en " + placeId;
        } else {
            return name + " en (" + x + "," + y + ")";
        }
    }
}