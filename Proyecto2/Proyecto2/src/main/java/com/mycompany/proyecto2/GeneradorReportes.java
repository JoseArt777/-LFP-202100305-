/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Generador de reportes HTML para tokens y errores
 */
public class GeneradorReportes {
    private static final String DIRECTORIO_REPORTES = "reportes";
    
    public GeneradorReportes() {
        // Crear directorio de reportes si no existe
        try {
            Files.createDirectories(Paths.get(DIRECTORIO_REPORTES));
        } catch (IOException e) {
            System.err.println("Error al crear directorio de reportes: " + e.getMessage());
        }
    }
    
    /**
     * Genera los reportes de tokens y errores
     * @param tokens Lista de tokens
     * @param errores Lista de errores
     * @return true si se generaron los reportes correctamente, false en caso contrario
     */
    public boolean generarReportes(List<Token> tokens, List<ErrorCompilacion> errores) {
        boolean tokenReportOk = generarReporteTokens(tokens);
        boolean errorReportOk = generarReporteErrores(errores);
        
        return tokenReportOk && errorReportOk;
    }
    
    /**
     * Genera el reporte HTML de tokens
     * @param tokens Lista de tokens
     * @return true si se generó correctamente, false en caso contrario
     */
    private boolean generarReporteTokens(List<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            return false;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORIO_REPORTES + "/tokens.html"))) {
            // Cabecera HTML
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Reporte de Tokens</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("        h1 { color: #333; text-align: center; }\n");
            writer.write("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            writer.write("        th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }\n");
            writer.write("        th { background-color: #4CAF50; color: white; }\n");
            writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write("        tr:hover { background-color: #ddd; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("    <h1>Reporte de Tokens</h1>\n");
            writer.write("    <table>\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Token</th>\n");
            writer.write("            <th>Lexema</th>\n");
            writer.write("            <th>Línea</th>\n");
            writer.write("            <th>Columna</th>\n");
            writer.write("        </tr>\n");
            
            // Contenido de la tabla
            for (Token token : tokens) {
                if (token.getTipo() == Token.Tipo.FIN_ARCHIVO) {
                    continue; // No incluir el token de fin de archivo
                }
                
                writer.write("        <tr>\n");
                writer.write("            <td>" + token.getNombreTipo() + "</td>\n");
                writer.write("            <td>" + token.getLexema() + "</td>\n");
                writer.write("            <td>" + token.getLinea() + "</td>\n");
                writer.write("            <td>" + token.getColumna() + "</td>\n");
                writer.write("        </tr>\n");
            }
            
            // Pie HTML
            writer.write("    </table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al generar reporte de tokens: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Genera el reporte HTML de errores
     * @param errores Lista de errores
     * @return true si se generó correctamente, false en caso contrario
     */
    private boolean generarReporteErrores(List<ErrorCompilacion> errores) {
        // Generar reporte sin errores si la lista está vacía
        if (errores == null || errores.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORIO_REPORTES + "/errores.html"))) {
                // Cabecera HTML
                writer.write("<!DOCTYPE html>\n");
                writer.write("<html lang=\"es\">\n");
                writer.write("<head>\n");
                writer.write("    <meta charset=\"UTF-8\">\n");
                writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
                writer.write("    <title>Reporte de Errores</title>\n");
                writer.write("    <style>\n");
                writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
                writer.write("        h1 { color: #333; text-align: center; }\n");
                writer.write("        p { text-align: center; color: green; font-weight: bold; }\n");
                writer.write("    </style>\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                writer.write("    <h1>Reporte de Errores</h1>\n");
                writer.write("    <p>No se detectaron errores en el análisis.</p>\n");
                writer.write("</body>\n");
                writer.write("</html>");
                
                return true;
            } catch (IOException e) {
                System.err.println("Error al generar reporte de errores vacío: " + e.getMessage());
                return false;
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DIRECTORIO_REPORTES + "/errores.html"))) {
            // Cabecera HTML
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html lang=\"es\">\n");
            writer.write("<head>\n");
            writer.write("    <meta charset=\"UTF-8\">\n");
            writer.write("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            writer.write("    <title>Reporte de Errores</title>\n");
            writer.write("    <style>\n");
            writer.write("        body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("        h1 { color: #333; text-align: center; }\n");
            writer.write("        table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            writer.write("        th, td { padding: 10px; text-align: left; border: 1px solid #ddd; }\n");
            writer.write("        th { background-color: #f44336; color: white; }\n");
            writer.write("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
            writer.write("        tr:hover { background-color: #ddd; }\n");
            writer.write("    </style>\n");
            writer.write("</head>\n");
            writer.write("<body>\n");
            writer.write("    <h1>Reporte de Errores</h1>\n");
            writer.write("    <table>\n");
            writer.write("        <tr>\n");
            writer.write("            <th>Error</th>\n");
            writer.write("            <th>Descripción</th>\n");
            writer.write("            <th>Lexema</th>\n");
            writer.write("            <th>Línea</th>\n");
            writer.write("            <th>Columna</th>\n");
            writer.write("        </tr>\n");
            
            // Contenido de la tabla
            for (ErrorCompilacion error : errores) {
                writer.write("        <tr>\n");
                writer.write("            <td>" + error.getNombreTipo() + "</td>\n");
                writer.write("            <td>" + error.getDescripcion() + "</td>\n");
                writer.write("            <td>" + error.getLexema() + "</td>\n");
                writer.write("            <td>" + error.getLinea() + "</td>\n");
                writer.write("            <td>" + error.getColumna() + "</td>\n");
                writer.write("        </tr>\n");
            }
            
            // Pie HTML
            writer.write("    </table>\n");
            writer.write("</body>\n");
            writer.write("</html>");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al generar reporte de errores: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Abre el reporte especificado en el navegador predeterminado
     * @param tipoReporte "tokens" o "errores"
     * @return true si se abrió correctamente, false en caso contrario
     */
    public boolean abrirReporte(String tipoReporte) {
        String rutaReporte = DIRECTORIO_REPORTES + "/" + tipoReporte + ".html";
        File archivoReporte = new File(rutaReporte);
        
        if (!archivoReporte.exists()) {
            return false;
        }
        
        try {
            // Abrir el archivo en el navegador predeterminado
            Desktop.getDesktop().browse(archivoReporte.toURI());
            return true;
        } catch (IOException e) {
            System.err.println("Error al abrir reporte: " + e.getMessage());
            return false;
        }
    }
}