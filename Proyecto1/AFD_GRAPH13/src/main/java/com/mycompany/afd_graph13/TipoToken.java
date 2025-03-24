/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph13;

public enum TipoToken {
    // Delimitadores
    LLAVE_ABRE("{"),
    LLAVE_CIERRA("}"),
    CORCHETE_ABRE("["),
    CORCHETE_CIERRA("]"),
    PARENTESIS_ABRE("("),
    PARENTESIS_CIERRA(")"),
    COMA(","),
    DOS_PUNTOS(":"),
    IGUAL("="),
    FLECHA("->"),
    
    // Literales
    IDENTIFICADOR("IDENTIFICADOR"),
    CADENA("CADENA"),
    SIMBOLO("SIMBOLO"),
    
    // Palabras clave
    DESCRIPCION("descripcion"),
    ESTADOS("estados"),
    ALFABETO("alfabeto"),
    INICIAL("inicial"),
    FINALES("finales"),
    TRANSICIONES("transiciones"),
    
    // Otros
    COMENTARIO("COMENTARIO"),
    ESPACIO("ESPACIO"),
    ERROR("ERROR"),
    EOF("EOF");
    
    private final String representacion;
    
    TipoToken(String representacion) {
        this.representacion = representacion;
    }
    
    public String getRepresentacion() {
        return representacion;
    }
    
    public static TipoToken fromString(String text) {
        for (TipoToken tipo : TipoToken.values()) {
            if (tipo.representacion.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        return null;
    }
}