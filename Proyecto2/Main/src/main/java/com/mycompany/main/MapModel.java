package com.mycompany.main;

import java.util.*;

public class MapModel {
    private String name;
    private List<Place> places;
    private List<Connection> connections;
    private List<MapObject> objects;
    
    public MapModel(
            String name) {
        this.name = name;
        this.places = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.objects = new ArrayList<>();
    }
    
    // Clases internas para elementos del mapa
    public static class Place {
        private String name;
        private String type;
        private int x, y;
        
        public Place(String name, String type, int x, int y) {
            this.name = name;
            this.type = type;
            this.x = x;
            this.y = y;
        }
        
        public String getName() { return name; }
        public String getType() { return type; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    public static class Connection {
        private String from;
        private String to;
        private String type;
        
        public Connection(String from, String to, String type) {
            this.from = from;
            this.to = to;
            this.type = type;
        }
        
        public String getFrom() { return from; }
        public String getTo() { return to; }
        public String getType() { return type; }
    }
    
    public static class MapObject {
        private String name;
        private String type;
        private String location; // Nombre del lugar o coordenadas
        private boolean isCoordinate;
        private int x, y;
        
        // Constructor para objetos en lugares específicos
        public MapObject(String name, String type, String location) {
            this.name = name;
            this.type = type;
            this.location = location;
            this.isCoordinate = false;
        }
        
        // Constructor para objetos en coordenadas
        public MapObject(String name, String type, int x, int y) {
            this.name = name;
            this.type = type;
            this.x = x;
            this.y = y;
            this.isCoordinate = true;
            this.location = "(" + x + "," + y + ")";
        }
        
        public String getName() { return name; }
        public String getType() { return type; }
        public String getLocation() { return location; }
        public boolean isCoordinate() { return isCoordinate; }
        public int getX() { return x; }
        public int getY() { return y; }
    }
    
    // Métodos para añadir elementos
    public void addPlace(String name, String type, int x, int y) {
        places.add(new Place(name, type, x, y));
    }
    
    public void addConnection(String from, String to, String type) {
        connections.add(new Connection(from, to, type));
    }
    
    public void addObject(String name, String type, String location) {
        objects.add(new MapObject(name, type, location));
    }
    
    public void addObject(String name, String type, int x, int y) {
        objects.add(new MapObject(name, type, x, y));
    }
    
    // Getters
    public String getName() { return name; }
    public List<Place> getPlaces() { return places; }
    public List<Connection> getConnections() { return connections; }
    public List<MapObject> getObjects() { return objects; }
}