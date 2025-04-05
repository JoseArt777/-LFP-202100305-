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
 * Representa una conexión entre dos lugares en el mapa narrativo
 */
public class Conexion {
    private String lugarOrigen;
    private String lugarDestino;
    private String tipoCamino;
    
    public Conexion(String lugarOrigen, String lugarDestino, String tipoCamino) {
        this.lugarOrigen = lugarOrigen;
        this.lugarDestino = lugarDestino;
        this.tipoCamino = tipoCamino;
    }
    
    /**
     * Obtiene el estilo de línea para Graphviz según el tipo de camino
     */
    public String obtenerEstiloLinea() {
        switch (tipoCamino.toLowerCase()) {
            case "camino": return "solid";
            case "puente": return "dotted";
            case "sendero": return "dashed";
            case "carretera": return "solid";
            case "nado": return "dashed";
            case "lancha": return "solid";
            case "teleférico": return "dotted";
            default: return "solid";
        }
    }
    
    /**
     * Obtiene el color de línea para Graphviz según el tipo de camino
     */
    public String obtenerColorLinea() {
        switch (tipoCamino.toLowerCase()) {
            case "camino": return "black";
            case "puente": return "gray";
            case "sendero": return "saddlebrown";
            case "carretera": return "darkgray";
            case "nado": return "deepskyblue";
            case "lancha": return "blue";
            case "teleférico": return "purple";
            default: return "black";
        }
    }
    
    // Getters
    public String getLugarOrigen() { return lugarOrigen; }
    public String getLugarDestino() { return lugarDestino; }
    public String getTipoCamino() { return tipoCamino; }
    
    @Override
    public String toString() {
        return "Conexion{lugarOrigen='" + lugarOrigen + "', lugarDestino='" + lugarDestino + 
               "', tipoCamino='" + tipoCamino + "'}";
    }
}