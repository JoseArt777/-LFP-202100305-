/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 * Clase que representa un estado en el autómata.
 * Cada estado tiene un nombre y una posición (x, y) para la visualización.
 * Se sobrescriben los métodos equals y hashCode para asegurar que los estados se comparen correctamente.
 * El método toString se sobrescribe para devolver el nombre del estado.
 * 
 * @autor iosea
 */
public class State {
    private final String name;
    private int posX;
    private int posY;

    public State(String name) {
        this.name = name;
        // Posición inicial aleatoria para la visualización
        this.posX = (int) (Math.random() * 400);
        this.posY = (int) (Math.random() * 300);
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
                '}';
    }
}