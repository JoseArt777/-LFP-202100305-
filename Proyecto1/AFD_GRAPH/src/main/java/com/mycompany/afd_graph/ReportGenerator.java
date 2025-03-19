/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

import java.util.List;

/**
 *
 * @author iosea
 */


public class ReportGenerator {
    
    public static String generateTokenReport(LexicalAnalyzer analyzer) {
        if (analyzer == null) {
            return "No hay datos para generar reporte.";
        }
        
        List<Token> tokens = analyzer.getTokens();
        StringBuilder report = new StringBuilder();
        
        report.append("REPORTE DE TOKENS\n");
        report.append("=================\n\n");
        report.append(String.format("%-10s %-20s %-30s %-8s %-8s\n", 
                "No.", "Tipo", "Lexema", "Línea", "Columna"));
        report.append("-------------------------------------------------------------------------\n");
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            report.append(String.format("%-10d %-20s %-30s %-8d %-8d\n", 
                    i + 1, 
                    token.getType(), 
                    token.getValue(), 
                    token.getLine(), 
                    token.getColumn()));
        }
        
        return report.toString();
    }
    
    public static String generateErrorReport(LexicalAnalyzer analyzer) {
        if (analyzer == null) {
            return "No hay datos para generar reporte.";
        }
        
        List<LexicalError> errors = analyzer.getErrors();
        StringBuilder report = new StringBuilder();
        
        report.append("REPORTE DE ERRORES LÉXICOS\n");
        report.append("==========================\n\n");
        
        if (errors.isEmpty()) {
            report.append("No se encontraron errores léxicos en el análisis.\n");
        } else {
            report.append(String.format("%-10s %-50s %-8s %-8s\n", 
                    "No.", "Descripción", "Línea", "Columna"));
            report.append("-------------------------------------------------------------------------\n");
            
            for (int i = 0; i < errors.size(); i++) {
                LexicalError error = errors.get(i);
                report.append(String.format("%-10d %-50s %-8d %-8d\n", 
                        i + 1, 
                        error.getMessage(), 
                        error.getLine(), 
                        error.getColumn()));
            }
        }
        
        return report.toString();
    }
}