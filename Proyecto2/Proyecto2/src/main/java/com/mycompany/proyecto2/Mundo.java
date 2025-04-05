/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un mundo o escenario con lugares, conexiones y objetos
 */
public class Mundo {
    private String nombre;
    private Map<String, Lugar> lugares;
    private List<Conexion> conexiones;
    private List<Objeto> objetos;
    
    public Mundo(String nombre) {
        this.nombre = nombre;
        this.lugares = new HashMap<>();
        this.conexiones = new ArrayList<>();
        this.objetos = new ArrayList<>();
    }
    
    public void agregarLugar(Lugar lugar) {
        this.lugares.put(lugar.getNombre(), lugar);
    }
    
    public void agregarConexion(Conexion conexion) {
        this.conexiones.add(conexion);
    }
    
    public void agregarObjeto(Objeto objeto) {
        this.objetos.add(objeto);
    }
    
    public Lugar buscarLugar(String nombre) {
        return lugares.get(nombre);
    }
    
    // Getters
    public String getNombre() { return nombre; }
    public Map<String, Lugar> getLugares() { return lugares; }
    public List<Conexion> getConexiones() { return conexiones; }
    public List<Objeto> getObjetos() { return objetos; }
    
    @Override
    public String toString() {
        return "Mundo{nombre='" + nombre + "', lugares=" + lugares.size() + 
               ", conexiones=" + conexiones.size() + ", objetos=" + objetos.size() + '}';
    }
}