/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author iosea
 */

/**
 * Clase que representa un token reconocido por el analizador léxico.
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private int line;
    private int column;
    
    /**
     * Constructor para un token.
     * 
     * @param type Tipo de token
     * @param lexeme Lexema (texto) del token
     * @param line Línea donde se encontró el token
     * @param column Columna donde se encontró el token
     */
    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }
    
    /**
     * Constructor para un token de error.
     * 
     * @param lexeme Lexema (texto) del token
     * @param line Línea donde se encontró el token
     * @param column Columna donde se encontró el token
     */
    public static Token createError(String lexeme, int line, int column) {
        return new Token(TokenType.ERROR, lexeme, line, column);
    }
    
    // Getters
    public TokenType getType() {
        return type;
    }
    
    public String getLexeme() {
        return lexeme;
    }
    
    public int getLine() {
        return line;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public String toString() {
        return String.format("Token{type=%s, lexeme='%s', line=%d, column=%d}", 
                type, lexeme, line, column);
    }
}