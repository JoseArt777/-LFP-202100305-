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

/**
 * Generador de reportes HTML para tokens y errores.
 */
public class ReportGenerator {
    
    /**
     * Genera un reporte HTML con la lista de tokens encontrados.
     * 
     * @param tokens Lista de tokens
     * @return Contenido HTML del reporte
     */
    public String generateTokensReport(ArrayList<Token> tokens) {
        StringBuilder html = new StringBuilder();
        
        // Encabezado HTML
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("  <title>Reporte de Tokens</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("    h1 { color: #333; text-align: center; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
        html.append("    th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }\n");
        html.append("    th { background-color: #4CAF50; color: white; }\n");
        html.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    tr:hover { background-color: #ddd; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h1>Reporte de Tokens</h1>\n");
        
        // Tabla de tokens
        html.append("  <table>\n");
        html.append("    <tr>\n");
        html.append("      <th>Token</th>\n");
        html.append("      <th>Lexema</th>\n");
        html.append("      <th>Línea</th>\n");
        html.append("      <th>Columna</th>\n");
        html.append("    </tr>\n");
        
        // Filas de la tabla
        for (Token token : tokens) {
            html.append("    <tr>\n");
            html.append("      <td>").append(token.getType().getValue()).append("</td>\n");
            html.append("      <td>").append(escapeHtml(token.getLexeme())).append("</td>\n");
            html.append("      <td>").append(token.getLine()).append("</td>\n");
            html.append("      <td>").append(token.getColumn()).append("</td>\n");
            html.append("    </tr>\n");
        }
        
        html.append("  </table>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Genera un reporte HTML con la lista de errores encontrados.
     * 
     * @param errors Lista de errores (tokens marcados como error)
     * @return Contenido HTML del reporte
     */
    public String generateErrorsReport(ArrayList<Token> errors) {
        StringBuilder html = new StringBuilder();
        
        // Encabezado HTML
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("  <title>Reporte de Errores</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
        html.append("    h1 { color: #333; text-align: center; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
        html.append("    th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }\n");
        html.append("    th { background-color: #f44336; color: white; }\n");
        html.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    tr:hover { background-color: #ddd; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h1>Reporte de Errores</h1>\n");
        
        // Tabla de errores
        html.append("  <table>\n");
        html.append("    <tr>\n");
        html.append("      <th>Error</th>\n");
        html.append("      <th>Línea</th>\n");
        html.append("      <th>Columna</th>\n");
        html.append("    </tr>\n");
        
        // Filas de la tabla
        for (Token error : errors) {
            html.append("    <tr>\n");
            html.append("      <td>").append(escapeHtml(error.getLexeme())).append("</td>\n");
            html.append("      <td>").append(error.getLine()).append("</td>\n");
            html.append("      <td>").append(error.getColumn()).append("</td>\n");
            html.append("    </tr>\n");
        }
        
        html.append("  </table>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    /**
     * Escapa los caracteres especiales HTML.
     * 
     * @param input Texto a escapar
     * @return Texto escapado
     */
    private String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}