package com.mycompany.afd_graph;
/**
 * Clase que representa un estado en el autómata.
 * Cada estado tiene un nombre y una posición (x, y) para la visualización.
 * También tiene flags para indicar si es un estado inicial o de aceptación.
 * Se sobrescriben los métodos equals y hashCode para asegurar que los estados se comparen correctamente.
 * El método toString se sobrescribe para devolver el nombre del estado.
 * 
 * @autor iosea
 */
public class State {
    private final String name;
    private int posX;
    private int posY;
    private boolean initial;
    private boolean accepting;
    
    public State(String name) {
        this.name = name;
        // Posición inicial aleatoria para la visualización
        this.posX = (int) (Math.random() * 400);
        this.posY = (int) (Math.random() * 300);
        this.initial = false;
        this.accepting = false;
    }
    
    public String getName() {
        return name;
    }
    
    public int getPosX() {
        return posX;
    }
    
    public void setPosX(int posX) {
        this.posX = posX;
    }
    
    public int getPosY() {
        return posY;
    }
    
    public void setPosY(int posY) {
        this.posY = posY;
    }
    
    public boolean isInitial() {
        return initial;
    }
    
    public void setInitial(boolean initial) {
        this.initial = initial;
    }
    
    public boolean isAccepting() {
        return accepting;
    }
    
    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        State state = (State) obj;
        return name.equals(state.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return "State{" +
                "name='" + name + '\'' +
                ", posX=" + posX +
                ", posY=" + posY +
                ", initial=" + initial +
                ", accepting=" + accepting +
                '}';
    }
}