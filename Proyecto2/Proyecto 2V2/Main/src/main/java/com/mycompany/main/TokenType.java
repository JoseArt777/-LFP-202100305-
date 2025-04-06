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
 * Enumeración de los tipos de tokens reconocidos por el analizador léxico.
 */
public enum TokenType {
    // Palabras reservadas
    WORLD("world"),
    PLACE("place"),
    CONNECT("connect"),
    OBJECT("object"),
    AT("at"),
    TO("to"),
    WITH("with"),
    
    // Símbolos
    LBRACE("{"),
    RBRACE("}"),
    LPAREN("("),
    RPAREN(")"),
    COMMA(","),
    COLON(":"),
    
    // Literales
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),
    
    // Fin de archivo
    EOF("EOF"),
    
    // Error
    ERROR("error");
    
    private final String value;
    
    TokenType(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}