package com.mycompany.main;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReportGenerator {
    // Método para generar reporte de tokens
    public static void generateTokenReport(List<Token> tokens, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Escribir el encabezado HTML
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n");
            writer.write("<title>Token Report</title>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("<style>\n");
            writer.write("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("table { border-collapse: collapse; width: 100%; }\n");
            writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("th { background-color: #4CAF50; color: white; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            writer.write("<h1>Token Report</h1>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>Token</th><th>Lexeme</th><th>Line</th><th>Column</th></tr>\n");

            // Escribir cada token en una fila de la tabla
            for (Token token : tokens) {
                writer.write(String.format("<tr><td>%s</td><td>%s</td><td>%d</td><td>%d</td></tr>\n", 
                    token.getType(), 
                    escapeHtml(token.getLexeme()), 
                    token.getLine(), 
                    token.getColumn()));
            }

            // Cerrar tabla y documento HTML
            writer.write("</table>\n");
            writer.write("</body>\n</html>");
        }
    }

    // Método para generar reporte de errores
    public static void generateErrorReport(List<String> lexicalErrors, List<String> syntaxErrors, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            // Escribir el encabezado HTML
            writer.write("<!DOCTYPE html>\n");
            writer.write("<html>\n<head>\n");
            writer.write("<title>Error Report</title>\n");
            writer.write("<meta charset=\"UTF-8\">\n");
            writer.write("<style>\n");
            writer.write("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            writer.write("table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }\n");
            writer.write("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            writer.write("th { background-color: #f44336; color: white; }\n");
            writer.write(".lexical { background-color: #ffcdd2; }\n");
            writer.write(".syntax { background-color: #ffebee; }\n");
            writer.write("</style>\n");
            writer.write("</head>\n<body>\n");
            
            // Sección de errores léxicos
            writer.write("<h1>Comprehensive Error Report</h1>\n");
            writer.write("<h2>Lexical Errors</h2>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>No.</th><th>Error</th><th>Line</th><th>Column</th></tr>\n");
            
            for (int i = 0; i < lexicalErrors.size(); i++) {
                String[] parts = lexicalErrors.get(i).split("\\|");
                if (parts.length >= 3) {
                    writer.write(String.format("<tr class=\"lexical\"><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>\n", 
                        i + 1, 
                        escapeHtml(parts[0]), 
                        parts[1], 
                        parts[2]));
                }
            }
            
            if (lexicalErrors.isEmpty()) {
                writer.write("<tr><td colspan=\"4\">No lexical errors found.</td></tr>\n");
            }
            
            writer.write("</table>\n");
            
            // Sección de errores sintácticos
            writer.write("<h2>Syntax Errors</h2>\n");
            writer.write("<table>\n");
            writer.write("<tr><th>No.</th><th>Error</th><th>Line</th><th>Column</th></tr>\n");
            
            for (int i = 0; i < syntaxErrors.size(); i++) {
                writer.write(String.format("<tr class=\"syntax\"><td>%d</td><td>%s</td><td></td><td></td></tr>\n", 
                    i + 1, 
                    escapeHtml(syntaxErrors.get(i))));
            }
            
            if (syntaxErrors.isEmpty()) {
                writer.write("<tr><td colspan=\"4\">No syntax errors found.</td></tr>\n");
            }
            
            writer.write("</table>\n");
            
            // Resumen de errores
            writer.write(String.format("<p>Total Lexical Errors: %d</p>\n", lexicalErrors.size()));
            writer.write(String.format("<p>Total Syntax Errors: %d</p>\n", syntaxErrors.size()));
            
            writer.write("</body>\n</html>");
        }
    }

    // Método de utilidad para escapar caracteres HTML
    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}