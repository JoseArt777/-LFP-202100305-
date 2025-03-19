package com.mycompany.afd_graph;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author iosea
 */

import java.util.ArrayList;
import java.util.List;

public class LexicalAnalyzer {
    private final String input;
    private int position;
    private int line;
    private int column;
    private final List<Token> tokens;
    private final List<LexicalError> errors;

    public LexicalAnalyzer(String input) {
        this.input = input;
        this.position = 0;
        this.line = 1;
        this.column = 1;
        this.tokens = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public void analyze() {
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            
            // Ignorar espacios en blanco y saltos de línea, pero llevar cuenta de ellos
            if (Character.isWhitespace(currentChar)) {
                if (currentChar == '\n') {
                    line++;
                    column = 1;
                } else {
                    column++;
                }
                position++;
                continue;
            }
            
            // Reconocer tokens según el estado actual del analizador léxico
            switch (currentChar) {
                case '{':
                    addToken(TokenType.LBRACE, "{");
                    break;
                case '}':
                    addToken(TokenType.RBRACE, "}");
                    break;
                case '[':
                    addToken(TokenType.LBRACKET, "[");
                    break;
                case ']':
                    addToken(TokenType.RBRACKET, "]");
                    break;
                case '(':
                    addToken(TokenType.LPAREN, "(");
                    break;
                case ')':
                    addToken(TokenType.RPAREN, ")");
                    break;
                case ':':
                    addToken(TokenType.COLON, ":");
                    break;
                case ',':
                    addToken(TokenType.COMMA, ",");
                    break;
                case '=':
                    addToken(TokenType.EQUALS, "=");
                    break;
                case '-':
                    if (position + 1 < input.length() && input.charAt(position + 1) == '>') {
                        addToken(TokenType.ARROW, "->");
                        position++;
                    } else {
                        addLexicalError("Carácter no esperado: " + currentChar);
                    }
                    break;
                case '"':
                    readString();
                    break;
                default:
                    if (Character.isLetter(currentChar)) {
                        readIdentifier();
                    } else {
                        addLexicalError("Carácter no esperado: " + currentChar);
                    }
                    continue; // Saltar la actualización de posición
            }
            
            position++;
            column++;
        }
    }
    
    private void readString() {
        int startPos = position;
        int startLine = line;
        int startCol = column;
        position++; // Saltar las comillas iniciales
        column++;
        
        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        boolean closed = false;
        
        while (position < input.length()) {
            char current = input.charAt(position);
            
            if (current == '\n') {
                line++;
                column = 1;
            } else {
                column++;
            }
            
            if (escaped) {
                escaped = false;
                sb.append(current);
            } else if (current == '\\') {
                escaped = true;
            } else if (current == '"') {
                closed = true;
                position++;
                break;
            } else {
                sb.append(current);
            }
            
            position++;
        }
        
        if (closed) {
            tokens.add(new Token(TokenType.STRING, sb.toString(), startLine, startCol));
        } else {
            addLexicalError("Cadena no cerrada", startLine, startCol);
        }
    }
    
    private void readIdentifier() {
        int startPos = position;
        int startLine = line;
        int startCol = column;
        
        StringBuilder sb = new StringBuilder();
        
        while (position < input.length() && 
               (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            sb.append(input.charAt(position));
            position++;
            column++;
        }
        
        String identifier = sb.toString();
        
        // Verificar si es una palabra reservada
        switch (identifier) {
            case "descripcion":
                tokens.add(new Token(TokenType.DESCRIPTION, identifier, startLine, startCol));
                break;
            case "estados":
                tokens.add(new Token(TokenType.STATES, identifier, startLine, startCol));
                break;
            case "alfabeto":
                tokens.add(new Token(TokenType.ALPHABET, identifier, startLine, startCol));
                break;
            case "inicial":
                tokens.add(new Token(TokenType.INITIAL, identifier, startLine, startCol));
                break;
            case "finales":
                tokens.add(new Token(TokenType.FINALS, identifier, startLine, startCol));
                break;
            case "transiciones":
                tokens.add(new Token(TokenType.TRANSITIONS, identifier, startLine, startCol));
                break;
            default:
                tokens.add(new Token(TokenType.IDENTIFIER, identifier, startLine, startCol));
        }
    }
    
    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line, column));
    }
    
    private void addLexicalError(String message) {
        addLexicalError(message, line, column);
    }
    
    private void addLexicalError(String message, int line, int column) {
        errors.add(new LexicalError(message, line, column));
    }
    
    public List<Token> getTokens() {
        return tokens;
    }
    
    public List<LexicalError> getErrors() {
        return errors;
    }
}