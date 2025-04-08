/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;


/**
 * Clase que representa un token a nivel de carácter individual para propósitos de reporte.
 */
public class CharacterToken {
    private char character;
    private String type; // Clasificación básica: LETRA, DIGITO, SIMBOLO, ESPACIO
    private int line;
    private int column;
    
    /**
     * Constructor para un token de carácter.
     * 
     * @param character El carácter del token
     * @param type Tipo o clasificación del carácter
     * @param line Línea donde se encontró
     * @param column Columna donde se encontró
     */
    public CharacterToken(char character, String type, int line, int column) {
        this.character = character;
        this.type = type;
        this.line = line;
        this.column = column;
    }
    
    // Getters
    public char getCharacter() {
        return character;
    }
    
    public String getType() {
        return type;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
}