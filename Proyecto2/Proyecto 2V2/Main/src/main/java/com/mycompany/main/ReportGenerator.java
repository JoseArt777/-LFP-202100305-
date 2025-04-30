package com.mycompany.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generador de reportes HTML para tokens y errores.
 */
public class ReportGenerator {
    
    /**
     * Genera un reporte HTML con la lista de tokens encontrados.
     * 
     * @param tokens Lista de tokens
     * @param charTokens Lista de tokens a nivel de carácter
     * @return Contenido HTML del reporte
     */
    public String generateTokensReport(ArrayList<Token> tokens, ArrayList<CharacterToken> charTokens) {
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
        html.append("    h1, h2 { color: #333; text-align: center; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
        html.append("    th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }\n");
        html.append("    th { background-color: #4CAF50; color: white; }\n");
        html.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    tr:hover { background-color: #ddd; }\n");
        html.append("    .token-count { text-align: center; margin-top: 20px; font-weight: bold; }\n");
        html.append("    .char-tokens th { background-color: #2196F3; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h1>Reporte de Tokens</h1>\n");
        
        // Resumen de tokens
        Map<TokenType, Long> tokenCounts = tokens.stream()
                .collect(Collectors.groupingBy(Token::getType, Collectors.counting()));
        
        html.append("  <div class=\"token-count\">Total de tokens: ").append(tokens.size()).append("</div>\n");
        html.append("  <table style=\"width: 50%; margin: 20px auto;\">\n");
        html.append("    <tr>\n");
        html.append("      <th>Tipo de Token</th>\n");
        html.append("      <th>Cantidad</th>\n");
        html.append("    </tr>\n");
        
        tokenCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparing(TokenType::name)))
                .forEach(entry -> {
                    html.append("    <tr>\n");
                    html.append("      <td>").append(entry.getKey().getValue()).append("</td>\n");
                    html.append("      <td>").append(entry.getValue()).append("</td>\n");
                    html.append("    </tr>\n");
                });
        
        html.append("  </table>\n");
        
        // Tabla de tokens
        html.append("  <h2>Listado Detallado de Tokens</h2>\n");
        html.append("  <table>\n");
        html.append("    <tr>\n");
        html.append("      <th>#</th>\n");
        html.append("      <th>Token</th>\n");
        html.append("      <th>Lexema</th>\n");
        html.append("      <th>Línea</th>\n");
        html.append("      <th>Columna</th>\n");
        html.append("    </tr>\n");
        
        // Filas de la tabla
        int counter = 1;
        for (Token token : tokens) {
            html.append("    <tr>\n");
            html.append("      <td>").append(counter++).append("</td>\n");
            html.append("      <td>").append(token.getType().getValue()).append("</td>\n");
            html.append("      <td>").append(escapeHtml(token.getLexeme())).append("</td>\n");
            html.append("      <td>").append(token.getLine()).append("</td>\n");
            html.append("      <td>").append(token.getColumn()).append("</td>\n");
            html.append("    </tr>\n");
        }
        
        html.append("  </table>\n");
        
        // Tabla de tokens a nivel de carácter 
        html.append("  <h2>Análisis Carácter por Carácter</h2>\n");
        html.append("  <div class=\"token-count\">Total de caracteres analizados: ").append(charTokens.size()).append("</div>\n");
        html.append("  <table class=\"char-tokens\">\n");
        html.append("    <tr>\n");
        html.append("      <th>#</th>\n");
        html.append("      <th>Carácter</th>\n");
        html.append("      <th>Tipo</th>\n");
        html.append("      <th>Línea</th>\n");
        html.append("      <th>Columna</th>\n");
        html.append("    </tr>\n");
        
        // Filas de la tabla de caracteres
        counter = 1;
        for (CharacterToken charToken : charTokens) {
            html.append("    <tr>\n");
            html.append("      <td>").append(counter++).append("</td>\n");
            html.append("      <td>").append(escapeHtml(String.valueOf(charToken.getCharacter()))).append("</td>\n");
            html.append("      <td>").append(charToken.getType()).append("</td>\n");
            html.append("      <td>").append(charToken.getLine()).append("</td>\n");
            html.append("      <td>").append(charToken.getColumn()).append("</td>\n");
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
        html.append("    h1, h2 { color: #333; text-align: center; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
        html.append("    th, td { padding: 8px; text-align: left; border: 1px solid #ddd; }\n");
        html.append("    th { background-color: #f44336; color: white; }\n");
        html.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    tr:hover { background-color: #ddd; }\n");
        html.append("    .lexical th { background-color: #E91E63; }\n");
        html.append("    .syntactic th { background-color: #9C27B0; }\n");
        html.append("    .error-count { text-align: center; margin-top: 20px; font-weight: bold; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("  <h1>Reporte de Errores</h1>\n");
        
        // Resumen de errores
        long lexicalErrors = errors.stream()
                .filter(e -> e.getErrorType() == Token.ErrorType.LEXICAL)
                .count();
        long syntacticErrors = errors.stream()
                .filter(e -> e.getErrorType() == Token.ErrorType.SYNTACTIC)
                .count();
        
        html.append("  <div class=\"error-count\">Total de errores: ").append(errors.size()).append("</div>\n");
        html.append("  <table style=\"width: 50%; margin: 20px auto;\">\n");
        html.append("    <tr>\n");
        html.append("      <th>Tipo de Error</th>\n");
        html.append("      <th>Cantidad</th>\n");
        html.append("    </tr>\n");
        html.append("    <tr>\n");
        html.append("      <td>Errores Léxicos</td>\n");
        html.append("      <td>").append(lexicalErrors).append("</td>\n");
        html.append("    </tr>\n");
        html.append("    <tr>\n");
        html.append("      <td>Errores Sintácticos</td>\n");
        html.append("      <td>").append(syntacticErrors).append("</td>\n");
        html.append("    </tr>\n");
        html.append("  </table>\n");
        
        // Separar errores por tipo
        List<Token> lexicalErrorList = errors.stream()
                .filter(e -> e.getErrorType() == Token.ErrorType.LEXICAL)
                .collect(Collectors.toList());
        
        List<Token> syntacticErrorList = errors.stream()
                .filter(e -> e.getErrorType() == Token.ErrorType.SYNTACTIC)
                .collect(Collectors.toList());
        
        // Tabla de errores léxicos
        if (!lexicalErrorList.isEmpty()) {
            html.append("  <h2>Errores Léxicos</h2>\n");
            html.append("  <table class=\"lexical\">\n");
            html.append("    <tr>\n");
            html.append("      <th>#</th>\n");
            html.append("      <th>Error</th>\n");
            html.append("      <th>Línea</th>\n");
            html.append("      <th>Columna</th>\n");
            html.append("    </tr>\n");
            
            // Filas de la tabla
            int counter = 1;
            for (Token error : lexicalErrorList) {
                html.append("    <tr>\n");
                html.append("      <td>").append(counter++).append("</td>\n");
                html.append("      <td>").append(escapeHtml(error.getLexeme())).append("</td>\n");
                html.append("      <td>").append(error.getLine()).append("</td>\n");
                html.append("      <td>").append(error.getColumn()).append("</td>\n");
                html.append("    </tr>\n");
            }
            
            html.append("  </table>\n");
        }
        
        // Tabla de errores sintácticos
        if (!syntacticErrorList.isEmpty()) {
            html.append("  <h2>Errores Sintácticos</h2>\n");
            html.append("  <table class=\"syntactic\">\n");
            html.append("    <tr>\n");
            html.append("      <th>#</th>\n");
            html.append("      <th>Error</th>\n");
            html.append("      <th>Línea</th>\n");
            html.append("      <th>Columna</th>\n");
            html.append("    </tr>\n");
            
            // Filas de la tabla
            int counter = 1;
            for (Token error : syntacticErrorList) {
                html.append("    <tr>\n");
                html.append("      <td>").append(counter++).append("</td>\n");
                html.append("      <td>").append(escapeHtml(error.getLexeme())).append("</td>\n");
                html.append("      <td>").append(error.getLine()).append("</td>\n");
                html.append("      <td>").append(error.getColumn()).append("</td>\n");
                html.append("    </tr>\n");
            }
            
            html.append("  </table>\n");
        }
        
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