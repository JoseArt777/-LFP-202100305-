/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author iosea
 */

import java.util.ArrayList;

/**
 * Clase que representa un mundo o escenario en el mapa narrativo.
 */
public class World {
    private String name;
    private ArrayList<Place> places;
    private ArrayList<Connection> connections;
    private ArrayList<MapObject> objects;
    
    /**
     * Constructor de un mundo.
     * 
     * @param name Nombre del mundo
     */
    public World(String name) {
        this.name = name;
        this.places = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.objects = new ArrayList<>();
    }
    
    /**
     * Agrega un lugar al mundo.
     * 
     * @param place Lugar a agregar
     */
    public void addPlace(Place place) {
        places.add(place);
    }
    
    /**
     * Agrega una conexión al mundo.
     * 
     * @param connection Conexión a agregar
     */
    public void addConnection(Connection connection) {
        connections.add(connection);
    }
    
    /**
     * Agrega un objeto al mundo.
     * 
     * @param object Objeto a agregar
     */
    public void addObject(MapObject object) {
        objects.add(object);
    }
    
    /**
     * Busca un lugar por su nombre.
     * 
     * @param name Nombre del lugar a buscar
     * @return El lugar si existe, null en caso contrario
     */
    public Place findPlaceByName(String name) {
        for (Place place : places) {
            if (place.getName().equals(name)) {
                return place;
            }
        }
        return null;
    }
    
    /**
     * Busca un lugar por su posición.
     * 
     * @param x Coordenada X
     * @param y Coordenada Y
     * @return El lugar si existe, null en caso contrario
     */
    public Place findPlaceByPosition(int x, int y) {
        for (Place place : places) {
            if (place.getX() == x && place.getY() == y) {
                return place;
            }
        }
        return null;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public ArrayList<Place> getPlaces() {
        return places;
    }
    
    public ArrayList<Connection> getConnections() {
        return connections;
    }
    
    public ArrayList<MapObject> getObjects() {
        return objects;
    }
}