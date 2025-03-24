/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph13;

/**
 *
 * @author iosea
 */

import java.util.ArrayList;
import java.util.HashMap;



public class Automata {
    private final String nombre;
    private String descripcion;
    private final ArrayList<String> estados = new ArrayList<>();
    private final ArrayList<String> alfabeto = new ArrayList<>();
    private String estadoInicial;
    private final ArrayList<String> estadosFinales = new ArrayList<>();
    private final HashMap<String, HashMap<String, String>> transiciones = new HashMap<>();

    public Automata(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<String> getEstados() {
        return estados;
    }

    public ArrayList<String> getAlfabeto() {
        return alfabeto;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public ArrayList<String> getEstadosFinales() {
        return estadosFinales;
    }

    public HashMap<String, HashMap<String, String>> getTransiciones() {
        return transiciones;
    }

    public void agregarTransicion(String origen, String simbolo, String destino) {
        if (!transiciones.containsKey(origen)) {
            transiciones.put(origen, new HashMap<>());
        }
        transiciones.get(origen).put(simbolo, destino);
    }
}
