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
 * Representa un objeto especial en el mapa narrativo
 */
public class Objeto {
    private String nombre;
    private String tipo;
    private String lugarAsociado; // Nombre del lugar donde está (puede ser null)
    private int x; // Coordenada X si no está asociado a un lugar
    private int y; // Coordenada Y si no está asociado a un lugar
    private boolean ubicacionExplicita; // True si se especificó con coordenadas (x,y)
    
    // Constructor para objetos ubicados en un lugar específico
    public Objeto(String nombre, String tipo, String lugarAsociado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.lugarAsociado = lugarAsociado;
        this.ubicacionExplicita = false;
        this.x = -1;
        this.y = -1;
    }
    
    // Constructor para objetos con ubicación explícita en coordenadas
    public Objeto(String nombre, String tipo, int x, int y) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.lugarAsociado = null;
        this.ubicacionExplicita = true;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Obtiene la forma (shape) para Graphviz según el tipo de objeto
     */
    public String obtenerForma() {
        switch (tipo.toLowerCase()) {
            case "tesoro": return "box3d";
            case "llave": return "pentagon";
            case "arma": return "diamond";
            case "objeto mágico": return "component";
            case "poción": return "cylinder";
            case "trampa": return "hexagon";
            case "libro": return "note";
            case "herramienta": return "folder";
            case "bandera": return "tab";
            case "gema": return "egg";
            default: return "box";
        }
    }
    
    /**
     * Obtiene el color de relleno para Graphviz según el tipo de objeto
     */
    public String obtenerColor() {
        switch (tipo.toLowerCase()) {
            case "tesoro": return "gold";
            case "llave": return "lightsteelblue";
            case "arma": return "orangered";
            case "objeto mágico": return "violet";
            case "poción": return "plum";
            case "trampa": return "crimson";
            case "libro": return "navajowhite";
            case "herramienta": return "darkkhaki";
            case "bandera": return "white";
            case "gema": return "deepskyblue";
            default: return "white";
        }
    }
    
    /**
     * Obtiene el emoji Unicode asociado al tipo de objeto
     */
 
public String obtenerEmoji() {
    switch (tipo.toLowerCase()) {
        case "tesoro": return "🎁"; // Gift box
        case "llave": return "🔑"; // Key
        case "arma": return "🗡️"; // Dagger
        case "objeto mágico": return "✨"; // Sparkles
        case "poción": return "⚗️"; // Alembic
        case "trampa": return "💣"; // Bomb
        case "libro": return "📕"; // Closed book
        case "herramienta": return "🛠️"; // Hammer and wrench
        case "bandera": return "🚩"; // Triangular flag
        case "gema": return "💎"; // Gem stone
        default: return "";
    }
}
    
    // Getters
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getLugarAsociado() { return lugarAsociado; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isUbicacionExplicita() { return ubicacionExplicita; }
    
    @Override
    public String toString() {
        if (ubicacionExplicita) {
            return "Objeto{nombre='" + nombre + "', tipo='" + tipo + "', x=" + x + ", y=" + y + '}';
        } else {
            return "Objeto{nombre='" + nombre + "', tipo='" + tipo + "', lugarAsociado='" + lugarAsociado + "'}";
        }
    }
    
}