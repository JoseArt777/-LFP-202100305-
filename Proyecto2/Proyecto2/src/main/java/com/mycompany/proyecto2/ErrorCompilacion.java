/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;


/**
 * Representa un error encontrado durante el análisis léxico o sintáctico
 */
public class ErrorCompilacion {
    
    public enum Tipo {
        LEXICO,
        SINTACTICO
    }
    
    private Tipo tipo;
    private String descripcion;
    private String lexema;
    private int linea;
    private int columna;
    
    /**
     * Constructor de Error
     * @param tipo Tipo de error (léxico o sintáctico)
     * @param descripcion Descripción del error
     * @param lexema Lexema o símbolo que causó el error
     * @param linea Línea donde se encontró el error
     * @param columna Columna donde se encontró el error
     */
    public ErrorCompilacion(Tipo tipo, String descripcion, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }
    
    /**
     * Retorna el nombre del tipo de error para el reporte
     * @return Nombre del tipo de error
     */
    public String getNombreTipo() {
        return tipo == Tipo.LEXICO ? "Error Léxico" : "Error Sintáctico";
    }
    
    // Getters
    public Tipo getTipo() { return tipo; }
    public String getDescripcion() { return descripcion; }
    public String getLexema() { return lexema; }
    public int getLinea() { return linea; }
    public int getColumna() { return columna; }
    
    @Override
    public String toString() {
        return "Error{tipo=" + tipo + ", descripcion='" + descripcion + "', lexema='" + lexema + 
               "', linea=" + linea + ", columna=" + columna + '}';
    }
}