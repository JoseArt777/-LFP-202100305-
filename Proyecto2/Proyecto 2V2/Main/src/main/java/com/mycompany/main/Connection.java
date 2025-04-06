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
 * Clase que representa una conexión entre dos lugares en el mapa.
 */
public class Connection {
    private String source;
    private String target;
    private String type;
    
    /**
     * Constructor de una conexión.
     * 
     * @param source Nombre del lugar de origen
     * @param target Nombre del lugar de destino
     * @param type Tipo de conexión (camino, puente, etc.)
     */
    public Connection(String source, String target, String type) {
        this.source = source;
        this.target = target;
        this.type = type;
    }
    
    // Getters
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public String getType() {
        return type;
    }
    
    /**
     * Obtiene el estilo de línea según el tipo de conexión.
     * 
     * @return Estilo de línea para Graphviz
     */
    public String getLineStyle() {
        switch (type.toLowerCase()) {
            case "camino": return "solid";
            case "puente": return "dotted";
            case "sendero": return "dashed";
            case "carretera": return "solid";
            case "nado": return "dashed";
            case "lancha": return "solid";
            case "teleférico": return "dotted";
            default: return "solid"; // Estilo predeterminado
        }
    }
    
    /**
     * Obtiene el color de la línea según el tipo de conexión.
     * 
     * @return Color para Graphviz
     */
    public String getLineColor() {
        switch (type.toLowerCase()) {
            case "camino": return "black";
            case "puente": return "gray";
            case "sendero": return "saddlebrown";
            case "carretera": return "darkgray";
            case "nado": return "deepskyblue";
            case "lancha": return "blue";
            case "teleférico": return "purple";
            default: return "black"; // Color predeterminado
        }
    }
    
    @Override
    public String toString() {
        return source + " -> " + target + " [" + type + "]";
    }
}