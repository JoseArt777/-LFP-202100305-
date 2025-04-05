/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;


/**
 * Representa un token reconocido por el analizador léxico
 */
public class Token {
    // Definición de tipos de tokens
    public enum Tipo {
        PALABRA_RESERVADA_WORLD,
        PALABRA_RESERVADA_PLACE,
        PALABRA_RESERVADA_CONNECT,
        PALABRA_RESERVADA_OBJECT,
        PALABRA_RESERVADA_AT,
        PALABRA_RESERVADA_TO,
        PALABRA_RESERVADA_WITH,
        CADENA,
        IDENTIFICADOR,
        DOS_PUNTOS,
        LLAVE_APERTURA,
        LLAVE_CIERRE,
        PARENTESIS_APERTURA,
        PARENTESIS_CIERRE,
        COMA,
        NUMERO,
        FIN_ARCHIVO
    }
    
    private Tipo tipo;
    private String lexema;
    private int linea;
    private int columna;
    
    public Token(Tipo tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }
    
    public String getNombreTipo() {
        switch (tipo) {
            case PALABRA_RESERVADA_WORLD: return "Palabra Reservada (world)";
            case PALABRA_RESERVADA_PLACE: return "Palabra Reservada (place)";
            case PALABRA_RESERVADA_CONNECT: return "Palabra Reservada (connect)";
            case PALABRA_RESERVADA_OBJECT: return "Palabra Reservada (object)";
            case PALABRA_RESERVADA_AT: return "Palabra Reservada (at)";
            case PALABRA_RESERVADA_TO: return "Palabra Reservada (to)";
            case PALABRA_RESERVADA_WITH: return "Palabra Reservada (with)";
            case CADENA: return "Cadena";
            case IDENTIFICADOR: return "Identificador";
            case DOS_PUNTOS: return "Dos Puntos";
            case LLAVE_APERTURA: return "Llave Apertura";
            case LLAVE_CIERRE: return "Llave Cierre";
            case PARENTESIS_APERTURA: return "Paréntesis Apertura";
            case PARENTESIS_CIERRE: return "Paréntesis Cierre";
            case COMA: return "Coma";
            case NUMERO: return "Número";
            case FIN_ARCHIVO: return "Fin de Archivo";
            default: return "Desconocido";
        }
    }
    
    // Getters
    public Tipo getTipo() { return tipo; }
    public String getLexema() { return lexema; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
    
    @Override
    public String toString() {
        return "Token{tipo=" + tipo + ", lexema='" + lexema + "', linea=" + linea + ", columna=" + columna + '}';
    }
}