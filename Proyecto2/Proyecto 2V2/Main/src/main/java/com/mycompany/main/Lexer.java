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
import java.util.HashMap;
import java.util.Map;

/**
 * Analizador léxico para el lenguaje de mapas narrativos.
 */
public class Lexer {
    private String input;
    private int position;
    private int line;
    private int column;
    private char currentChar;
    
    private ArrayList<Token> tokens;
    private ArrayList<Token> errors;
    
    private static final Map<String, TokenType> KEYWORDS;
    
    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("world", TokenType.WORLD);
        KEYWORDS.put("place", TokenType.PLACE);
        KEYWORDS.put("connect", TokenType.CONNECT);
        KEYWORDS.put("object", TokenType.OBJECT);
        KEYWORDS.put("at", TokenType.AT);
        KEYWORDS.put("to", TokenType.TO);
        KEYWORDS.put("with", TokenType.WITH);
    }
    
    /**
     * Constructor por defecto.
     */
    public Lexer() {
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
    }
    
    /**
     * Analiza el texto de entrada y genera los tokens.
     * 
     * @param input Texto de entrada a analizar
     */
    public void analyze(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
        
        // Si hay entrada, tomar el primer carácter
        if (!input.isEmpty()) {
            currentChar = input.charAt(0);
        } else {
            currentChar = '\0'; // Carácter nulo para indicar fin de entrada
        }
        
        // Procesar todos los tokens
        Token token;
        do {
            token = getNextToken();
            if (token.getType() != TokenType.ERROR) {
                tokens.add(token);
            } else {
                errors.add(token);
            }
        } while (token.getType() != TokenType.EOF);
    }
    
    /**
     * Obtiene el siguiente token de la entrada.
     * 
     * @return El siguiente token
     */
    private Token getNextToken() {
        // Ignorar espacios en blanco
        skipWhitespace();
        
        // Verificar fin de archivo
        if (currentChar == '\0') {
            return new Token(TokenType.EOF, "", line, column);
        }
        
        // Identificar el tipo de token
        
        // Identificadores y palabras clave
        if (Character.isLetter(currentChar) || currentChar == '_') {
            return scanIdentifier();
        }
        
        // Números
        if (Character.isDigit(currentChar)) {
            return scanNumber();
        }
        
        // Cadenas de texto
        if (currentChar == '"') {
            return scanString();
        }
        
        // Símbolos
        switch (currentChar) {
            case '{':
                return consumeToken(TokenType.LBRACE);
            case '}':
                return consumeToken(TokenType.RBRACE);
            case '(':
                return consumeToken(TokenType.LPAREN);
            case ')':
                return consumeToken(TokenType.RPAREN);
            case ',':
                return consumeToken(TokenType.COMMA);
            case ':':
                return consumeToken(TokenType.COLON);
        }
        
        // Si no es ninguno de los anteriores, es un error
        Token errorToken = Token.createError(String.valueOf(currentChar), line, column);
        advance();
        return errorToken;
    }
    
    /**
     * Avanza a la siguiente posición en la entrada.
     */
    private void advance() {
        position++;
        column++;
        
        if (position >= input.length()) {
            currentChar = '\0'; // Fin de entrada
        } else {
            currentChar = input.charAt(position);
            
            // Actualizar contadores de línea y columna
            if (currentChar == '\n') {
                line++;
                column = 1;
            }
        }
    }
    
    /**
     * Salta los espacios en blanco en la entrada.
     */
    private void skipWhitespace() {
        while (currentChar != '\0' && Character.isWhitespace(currentChar)) {
            advance();
        }
    }
    
    /**
     * Reconoce un identificador o palabra clave.
     * 
     * @return Token de identificador o palabra clave
     */
    private Token scanIdentifier() {
        int startColumn = column;
        StringBuilder lexeme = new StringBuilder();
        
        while (currentChar != '\0' && (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
            lexeme.append(currentChar);
            advance();
        }
        
        String identifierStr = lexeme.toString();
        TokenType type = KEYWORDS.getOrDefault(identifierStr, TokenType.IDENTIFIER);
        
        return new Token(type, identifierStr, line, startColumn);
    }
    
    /**
     * Reconoce un número.
     * 
     * @return Token de número
     */
    private Token scanNumber() {
        int startColumn = column;
        StringBuilder lexeme = new StringBuilder();
        
        while (currentChar != '\0' && Character.isDigit(currentChar)) {
            lexeme.append(currentChar);
            advance();
        }
        
        return new Token(TokenType.NUMBER, lexeme.toString(), line, startColumn);
    }
    
    /**
     * Reconoce una cadena de texto.
     * 
     * @return Token de cadena
     */
    private Token scanString() {
        int startColumn = column;
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(currentChar); // Incluir la comilla inicial
        advance();
        
        while (currentChar != '\0' && currentChar != '"') {
            lexeme.append(currentChar);
            advance();
        }
        
        if (currentChar == '"') {
            lexeme.append(currentChar); // Incluir la comilla final
            advance();
            return new Token(TokenType.STRING, lexeme.toString(), line, startColumn);
        } else {
            // Error: cadena no cerrada
            return Token.createError(lexeme.toString(), line, startColumn);
        }
    }
    
    /**
     * Consume un token simple (de un solo carácter).
     * 
     * @param type Tipo de token a consumir
     * @return Token consumido
     */
    private Token consumeToken(TokenType type) {
        Token token = new Token(type, String.valueOf(currentChar), line, column);
        advance();
        return token;
    }
    
    /**
     * Obtiene la lista de tokens reconocidos.
     * 
     * @return Lista de tokens
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }
    
    /**
     * Obtiene la lista de errores léxicos.
     * 
     * @return Lista de errores
     */
    public ArrayList<Token> getErrors() {
        return errors;
    }
}