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
    private ErrorType errorType; // Nuevo campo para distinguir tipos de errores
    
    
    public enum ErrorType {
        NONE,
        LEXICAL,
        SYNTACTIC
    }
    
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
        this.errorType = ErrorType.NONE; // Por defecto, no es un error
    }
    
    /**
     * Constructor para un token de error.
     * 
     * @param lexeme Lexema (texto) del token
     * @param line Línea donde se encontró el token
     * @param column Columna donde se encontró el token
     * @param errorType Tipo de error (léxico o sintáctico)
     */
    public static Token createError(String lexeme, int line, int column, ErrorType errorType) {
        Token errorToken = new Token(TokenType.ERROR, lexeme, line, column);
        errorToken.errorType = errorType;
        return errorToken;
    }
    
    /**
     * Constructor para un token de error léxico (para mantener compatibilidad).
     * 
     * @param lexeme Lexema (texto) del token
     * @param line Línea donde se encontró el token
     * @param column Columna donde se encontró el token
     */
    public static Token createError(String lexeme, int line, int column) {
        return createError(lexeme, line, column, ErrorType.LEXICAL);
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
    
    public ErrorType getErrorType() {
        return errorType;
    }
    
    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }
    
    @Override
    public String toString() {
        return String.format("Token{type=%s, lexeme='%s', line=%d, column=%d, errorType=%s}", 
                type, lexeme, line, column, errorType);
    }
}