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
 * Controlador para la fase de análisis de archivos
 */
public class AnalizadorArchivo {
    private List<Token> tokens;
    private List<ErrorCompilacion> errores;
    private List<Mundo> mundos;
    
    public AnalizadorArchivo() {
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.mundos = new ArrayList<>();
    }
    
    /**
     * Analiza el texto de entrada y genera la estructura de mundos
     * @param texto Texto a analizar
     * @return Lista de mundos encontrados
     */
    public List<Mundo> analizar(String texto) {
        // Limpiar resultados anteriores
        tokens.clear();
        errores.clear();
        mundos.clear();
        
        // Análisis léxico
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(texto);
        tokens = analizadorLexico.analizar();
        errores.addAll(analizadorLexico.getErrores());
        
        // Si no hay errores léxicos críticos, continuar con el análisis sintáctico
        if (analizadorLexico.getErrores().isEmpty() || !hayErroresCriticos(analizadorLexico.getErrores())) {
            AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico(tokens);
            mundos = analizadorSintactico.analizar();
            errores.addAll(analizadorSintactico.getErrores());
        }
        
        return mundos;
    }
    
    /**
     * Verifica si hay errores léxicos críticos que impidan el análisis sintáctico
     * @param erroresLexicos Lista de errores léxicos
     * @return true si hay errores críticos, false en caso contrario
     */
    private boolean hayErroresCriticos(List<ErrorCompilacion> erroresLexicos) {
        // Aquí puedes implementar la lógica para determinar qué errores son críticos
        // Por simplicidad, consideramos que más de 5 errores léxicos es crítico
        return erroresLexicos.size() > 5;
    }
    
    // Getters
    public List<Token> getTokens() {
        return tokens;
    }
    
    public List<ErrorCompilacion> getErrores() {
        return errores;
    }
    
    public List<Mundo> getMundos() {
        return mundos;
    }
}