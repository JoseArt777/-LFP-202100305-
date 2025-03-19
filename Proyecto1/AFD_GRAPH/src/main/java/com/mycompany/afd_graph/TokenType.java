/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */

public enum TokenType {
    // Palabras reservadas
    DESCRIPTION,    // descripcion
    STATES,         // estados
    ALPHABET,       // alfabeto
    INITIAL,        // inicial
    FINALS,         // finales
    TRANSITIONS,    // transiciones
    
    // Símbolos
    LBRACE,         // {
    RBRACE,         // }
    LBRACKET,       // [
    RBRACKET,       // ]
    LPAREN,         // (
    RPAREN,         // )
    COLON,          // :
    COMMA,          // ,
    EQUALS,         // =
    ARROW,          // ->
    
    // Otros
    IDENTIFIER,     // e.g., S0, S1, AFD1
    STRING          // e.g., "Este autómata reconoce cadenas numéricas."
}