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
import java.util.List;

/**
 * Analizador léxico para el lenguaje de mapas narrativos
 */
public class AnalizadorLexico {
    private String entrada;
    private int posicion;
    private int linea;
    private int columna;
    private List<Token> tokens;
    private List<ErrorCompilacion> errores;
    private char caracterActual;
    
    public AnalizadorLexico(String entrada) {
        this.entrada = entrada;
        this.posicion = 0;
        this.linea = 1;
        this.columna = 1;
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
        
        if (!entrada.isEmpty()) {
            this.caracterActual = entrada.charAt(0);
        } else {
            this.caracterActual = '\0'; // Carácter nulo si la entrada está vacía
        }
    }
    
    /**
     * Avanza al siguiente carácter de la entrada
     */
    private void avanzar() {
        posicion++;
        columna++;
        
        if (posicion >= entrada.length()) {
            caracterActual = '\0'; // Fin de la entrada
        } else {
            caracterActual = entrada.charAt(posicion);
            
            // Manejo de saltos de línea
            if (caracterActual == '\n') {
                linea++;
                columna = 1;
            }
        }
    }
    
    /**
     * Obtiene el siguiente carácter sin avanzar
     */
    private char verSiguiente() {
        if (posicion + 1 >= entrada.length()) {
            return '\0';
        }
        return entrada.charAt(posicion + 1);
    }
    
    /**
     * Ignora espacios en blanco, tabulaciones y saltos de línea
     */
    private void ignorarEspacios() {
        while (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n' || caracterActual == '\r') {
            avanzar();
        }
    }
    
    /**
     * Analiza la entrada completa y genera la lista de tokens
     */
    public List<Token> analizar() {
        tokens.clear();
        errores.clear();
        posicion = 0;
        linea = 1;
        columna = 1;
        
        if (!entrada.isEmpty()) {
            caracterActual = entrada.charAt(0);
        }
        
        // Análisis léxico
        while (caracterActual != '\0') {
            ignorarEspacios();
            
            if (caracterActual == '\0') {
                break;
            }
            
            // Identificadores y palabras reservadas
            if (Character.isLetter(caracterActual)) {
                analizarIdentificador();
            }
            // Números
            else if (Character.isDigit(caracterActual)) {
                analizarNumero();
            }
            // Cadenas entre comillas
            else if (caracterActual == '"') {
                analizarCadena();
            }
            // Símbolos especiales
            else {
                switch (caracterActual) {
                    case '{':
                        tokens.add(new Token(Token.Tipo.LLAVE_APERTURA, "{", linea, columna));
                        avanzar();
                        break;
                    case '}':
                        tokens.add(new Token(Token.Tipo.LLAVE_CIERRE, "}", linea, columna));
                        avanzar();
                        break;
                    case '(':
                        tokens.add(new Token(Token.Tipo.PARENTESIS_APERTURA, "(", linea, columna));
                        avanzar();
                        break;
                    case ')':
                        tokens.add(new Token(Token.Tipo.PARENTESIS_CIERRE, ")", linea, columna));
                        avanzar();
                        break;
                    case ':':
                        tokens.add(new Token(Token.Tipo.DOS_PUNTOS, ":", linea, columna));
                        avanzar();
                        break;
                    case ',':
                        tokens.add(new Token(Token.Tipo.COMA, ",", linea, columna));
                        avanzar();
                        break;
                    default:
                        // Carácter no reconocido - Error léxico
                        errores.add(new ErrorCompilacion(ErrorCompilacion.Tipo.LEXICO, 
                                "Carácter no reconocido", String.valueOf(caracterActual), linea, columna));
                        avanzar();
                        break;
                }
            }
        }
        
        // Añadir token de fin de archivo
        tokens.add(new Token(Token.Tipo.FIN_ARCHIVO, "EOF", linea, columna));
        
        return tokens;
    }
    
    /**
     * Analiza un identificador o palabra reservada
     */
    private void analizarIdentificador() {
        StringBuilder lexema = new StringBuilder();
        int inicioColumna = columna;
        
        // Recopilar caracteres válidos para identificador (letras, dígitos, _)
        while (Character.isLetterOrDigit(caracterActual) || caracterActual == '_') {
            lexema.append(caracterActual);
            avanzar();
        }
        
        String palabra = lexema.toString();
        
        // Verificar si es una palabra reservada
        switch (palabra) {
            case "world":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_WORLD, palabra, linea, inicioColumna));
                break;
            case "place":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_PLACE, palabra, linea, inicioColumna));
                break;
            case "connect":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_CONNECT, palabra, linea, inicioColumna));
                break;
            case "object":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_OBJECT, palabra, linea, inicioColumna));
                break;
            case "at":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_AT, palabra, linea, inicioColumna));
                break;
            case "to":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_TO, palabra, linea, inicioColumna));
                break;
            case "with":
                tokens.add(new Token(Token.Tipo.PALABRA_RESERVADA_WITH, palabra, linea, inicioColumna));
                break;
            default:
                // Es un identificador
                tokens.add(new Token(Token.Tipo.IDENTIFICADOR, palabra, linea, inicioColumna));
                break;
        }
    }
    
    /**
     * Analiza un número entero
     */
    private void analizarNumero() {
        StringBuilder lexema = new StringBuilder();
        int inicioColumna = columna;
        
        // Recopilar dígitos
        while (Character.isDigit(caracterActual)) {
            lexema.append(caracterActual);
            avanzar();
        }
        
        tokens.add(new Token(Token.Tipo.NUMERO, lexema.toString(), linea, inicioColumna));
    }
    
    /**
     * Analiza una cadena entre comillas
     */
    private void analizarCadena() {
        StringBuilder lexema = new StringBuilder();
        int inicioColumna = columna;
        
        // Incluir la comilla inicial
        lexema.append(caracterActual);
        avanzar();
        
        // Recopilar todos los caracteres hasta encontrar otra comilla o fin de archivo
        while (caracterActual != '"' && caracterActual != '\0' && caracterActual != '\n') {
            lexema.append(caracterActual);
            avanzar();
        }
        
        if (caracterActual == '"') {
            // Incluir la comilla final
            lexema.append(caracterActual);
            avanzar();
            tokens.add(new Token(Token.Tipo.CADENA, lexema.toString(), linea, inicioColumna));
        } else {
            // Error: cadena no cerrada
            errores.add(new ErrorCompilacion(ErrorCompilacion.Tipo.LEXICO, 
                    "Cadena no cerrada", lexema.toString(), linea, inicioColumna));
        }
    }
    
    // Getters
    public List<Token> getTokens() {
        return tokens;
    }
    
    public List<ErrorCompilacion> getErrores() {
        return errores;
    }
}