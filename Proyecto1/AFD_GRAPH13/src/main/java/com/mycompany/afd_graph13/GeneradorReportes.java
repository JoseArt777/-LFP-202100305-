package com.mycompany.afd_graph13;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class GeneradorReportes {
    private AnalizadorLexico analizador;

    public GeneradorReportes(AnalizadorLexico analizador) {
        this.analizador = analizador;
    }

    public void generar(HashMap<String, Automata> automatas, ArrayList<String> erroresLexicos) {
        generarReporteTokens();
        generarReporteAutomatas(automatas);
        generarReporteErrores(erroresLexicos);
    }

    private void generarReporteTokens() {
        try (PrintWriter writer = new PrintWriter("reporte_tokens.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            writer.println("<head>");
            writer.println("    <meta charset='UTF-8'>");
            writer.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.println("    <title>Reporte de Tokens</title>");
            writer.println("    <style>");
            writer.println("        :root {");
            writer.println("            --primary-color: #3498db;");
            writer.println("            --secondary-color: #2980b9;");
            writer.println("            --accent-color: #e74c3c;");
            writer.println("            --text-color: #333;");
            writer.println("            --light-bg: #f9f9f9;");
            writer.println("            --border-color: #ddd;");
            writer.println("        }");
            writer.println("        * {");
            writer.println("            margin: 0;");
            writer.println("            padding: 0;");
            writer.println("            box-sizing: border-box;");
            writer.println("        }");
            writer.println("        body {");
            writer.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            writer.println("            color: var(--text-color);");
            writer.println("            line-height: 1.6;");
            writer.println("            background-color: #f5f7fa;");
            writer.println("            padding: 0;");
            writer.println("            margin: 0;");
            writer.println("        }");
            writer.println("        .container {");
            writer.println("            max-width: 1200px;");
            writer.println("            margin: 0 auto;");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        header {");
            writer.println("            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));");
            writer.println("            color: white;");
            writer.println("            padding: 30px 0;");
            writer.println("            margin-bottom: 30px;");
            writer.println("            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);");
            writer.println("        }");
            writer.println("        h1 {");
            writer.println("            text-align: center;");
            writer.println("            font-size: 2.5rem;");
            writer.println("            font-weight: 300;");
            writer.println("            letter-spacing: 1px;");
            writer.println("        }");
            writer.println("        .card {");
            writer.println("            background-color: white;");
            writer.println("            border-radius: 8px;");
            writer.println("            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);");
            writer.println("            overflow: hidden;");
            writer.println("            margin-bottom: 30px;");
            writer.println("        }");
            writer.println("        .card-header {");
            writer.println("            background-color: #f0f3f7;");
            writer.println("            padding: 15px 20px;");
            writer.println("            border-bottom: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("        .card-header h2 {");
            writer.println("            margin: 0;");
            writer.println("            font-size: 1.4rem;");
            writer.println("            color: var(--secondary-color);");
            writer.println("        }");
            writer.println("        .card-body {");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        table {");
            writer.println("            width: 100%;");
            writer.println("            border-collapse: collapse;");
            writer.println("            margin-top: 10px;");
            writer.println("            box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);");
            writer.println("        }");
            writer.println("        th, td {");
            writer.println("            padding: 12px 15px;");
            writer.println("            text-align: left;");
            writer.println("            border-bottom: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("        th {");
            writer.println("            background-color: var(--primary-color);");
            writer.println("            color: white;");
            writer.println("            font-weight: 500;");
            writer.println("            text-transform: uppercase;");
            writer.println("            font-size: 0.85rem;");
            writer.println("            letter-spacing: 0.5px;");
            writer.println("        }");
            writer.println("        tr:nth-child(even) {");
            writer.println("            background-color: #f8f9fa;");
            writer.println("        }");
            writer.println("        tr:hover {");
            writer.println("            background-color: #e9f5fe;");
            writer.println("        }");
            writer.println("        .footer {");
            writer.println("            text-align: center;");
            writer.println("            margin-top: 40px;");
            writer.println("            padding: 20px;");
            writer.println("            color: #777;");
            writer.println("            font-size: 0.9rem;");
            writer.println("            border-top: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("        .badge {");
            writer.println("            display: inline-block;");
            writer.println("            padding: 3px 7px;");
            writer.println("            border-radius: 4px;");
            writer.println("            font-size: 0.8rem;");
            writer.println("            font-weight: 500;");
            writer.println("        }");
            writer.println("        .badge-primary {");
            writer.println("            background-color: #e3f2fd;");
            writer.println("            color: #1565c0;");
            writer.println("        }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <header>");
            writer.println("        <h1>Reporte de Tokens</h1>");
            writer.println("    </header>");
            writer.println("    <div class='container'>");
            writer.println("        <div class='card'>");
            writer.println("            <div class='card-header'>");
            writer.println("                <h2>Listado de Tokens</h2>");
            writer.println("            </div>");
            writer.println("            <div class='card-body'>");
            writer.println("                <table>");
            writer.println("                    <thead>");
            writer.println("                        <tr>");
            writer.println("                            <th>#</th>");
            writer.println("                            <th>Tipo</th>");
            writer.println("                            <th>Lexema</th>");
            writer.println("                            <th>Línea</th>");
            writer.println("                            <th>Columna</th>");
            writer.println("                        </tr>");
            writer.println("                    </thead>");
            writer.println("                    <tbody>");

            ArrayList<Token> tokens = analizador.getTokens();
            for (int i = 0; i < tokens.size(); i++) {
                Token token = tokens.get(i);
                writer.println("                        <tr>");
                writer.println("                            <td>" + (i+1) + "</td>");
                writer.println("                            <td><span class='badge badge-primary'>" + token.getTipo() + "</span></td>");
                writer.println("                            <td>" + token.getLexema() + "</td>");
                writer.println("                            <td>" + token.getLinea() + "</td>");
                writer.println("                            <td>" + token.getColumna() + "</td>");
                writer.println("                        </tr>");
            }

            writer.println("                    </tbody>");
            writer.println("                </table>");
            writer.println("            </div>");
            writer.println("        </div>");
            writer.println("        <div class='footer'>");
            writer.println("            <p>AFDGraph | Generado: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</p>");
            writer.println("        </div>");
            writer.println("    </div>");
            writer.println("</body>");
            writer.println("</html>");
        } catch (Exception e) {
            System.err.println("Error al generar reporte de tokens: " + e.getMessage());
        }
    }

    private void generarReporteAutomatas(HashMap<String, Automata> automatas) {
        try (PrintWriter writer = new PrintWriter("reporte_automatas.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            writer.println("<head>");
            writer.println("    <meta charset='UTF-8'>");
            writer.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.println("    <title>Reporte de Autómatas</title>");
            writer.println("    <style>");
            writer.println("        :root {");
            writer.println("            --primary-color: #6b5b95;");
            writer.println("            --secondary-color: #4b3f6b;");
            writer.println("            --accent-color: #feb236;");
            writer.println("            --text-color: #333;");
            writer.println("            --border-color: #e0e0e0;");
            writer.println("        }");
            writer.println("        * {");
            writer.println("            margin: 0;");
            writer.println("            padding: 0;");
            writer.println("            box-sizing: border-box;");
            writer.println("        }");
            writer.println("        body {");
            writer.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            writer.println("            color: var(--text-color);");
            writer.println("            line-height: 1.6;");
            writer.println("            background-color: #f8f7fc;");
            writer.println("            padding: 0;");
            writer.println("            margin: 0;");
            writer.println("        }");
            writer.println("        .container {");
            writer.println("            max-width: 1200px;");
            writer.println("            margin: 0 auto;");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        header {");
            writer.println("            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));");
            writer.println("            color: white;");
            writer.println("            padding: 30px 0;");
            writer.println("            margin-bottom: 30px;");
            writer.println("            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);");
            writer.println("        }");
            writer.println("        h1 {");
            writer.println("            text-align: center;");
            writer.println("            font-size: 2.5rem;");
            writer.println("            font-weight: 300;");
            writer.println("            letter-spacing: 1px;");
            writer.println("        }");
            writer.println("        .automata-container {");
            writer.println("            display: grid;");
            writer.println("            grid-template-columns: repeat(auto-fill, minmax(500px, 1fr));");
            writer.println("            gap: 20px;");
            writer.println("            margin-bottom: 30px;");
            writer.println("        }");
            writer.println("        .automata {");
            writer.println("            background-color: white;");
            writer.println("            border-radius: 8px;");
            writer.println("            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);");
            writer.println("            overflow: hidden;");
            writer.println("            transition: transform 0.3s ease, box-shadow 0.3s ease;");
            writer.println("        }");
            writer.println("        .automata:hover {");
            writer.println("            transform: translateY(-5px);");
            writer.println("            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);");
            writer.println("        }");
            writer.println("        .automata-header {");
            writer.println("            background-color: var(--primary-color);");
            writer.println("            color: white;");
            writer.println("            padding: 15px 20px;");
            writer.println("            position: relative;");
            writer.println("        }");
            writer.println("        .automata-header h2 {");
            writer.println("            margin: 0;");
            writer.println("            font-size: 1.4rem;");
            writer.println("            font-weight: 500;");
            writer.println("        }");
            writer.println("        .automata-body {");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        .info-group {");
            writer.println("            margin-bottom: 15px;");
            writer.println("            padding-bottom: 15px;");
            writer.println("            border-bottom: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("        .info-group:last-child {");
            writer.println("            border-bottom: none;");
            writer.println("            margin-bottom: 0;");
            writer.println("            padding-bottom: 0;");
            writer.println("        }");
            writer.println("        .info-label {");
            writer.println("            font-weight: 600;");
            writer.println("            color: var(--secondary-color);");
            writer.println("            margin-bottom: 5px;");
            writer.println("            display: block;");
            writer.println("        }");
            writer.println("        .info-value {");
            writer.println("            padding: 8px 12px;");
            writer.println("            background-color: #f8f9fa;");
            writer.println("            border-radius: 4px;");
            writer.println("            border-left: 3px solid var(--primary-color);");
            writer.println("        }");
            writer.println("        .tag {");
            writer.println("            display: inline-block;");
            writer.println("            padding: 4px 8px;");
            writer.println("            margin: 2px;");
            writer.println("            background-color: #e9ecef;");
            writer.println("            border-radius: 4px;");
            writer.println("            font-size: 0.85rem;");
            writer.println("        }");
            writer.println("        .transiciones-list {");
            writer.println("            list-style-type: none;");
            writer.println("        }");
            writer.println("        .transicion-item {");
            writer.println("            padding: 8px;");
            writer.println("            margin-bottom: 5px;");
            writer.println("            border-radius: 4px;");
            writer.println("            background-color: #f0f4f8;");
            writer.println("            border-left: 3px solid var(--accent-color);");
            writer.println("        }");
            writer.println("        .transicion-item span {");
            writer.println("            font-family: 'Courier New', monospace;");
            writer.println("            font-weight: bold;");
            writer.println("        }");
            writer.println("        .footer {");
            writer.println("            text-align: center;");
            writer.println("            margin-top: 40px;");
            writer.println("            padding: 20px;");
            writer.println("            color: #777;");
            writer.println("            font-size: 0.9rem;");
            writer.println("            border-top: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <header>");
            writer.println("        <h1>Reporte de Autómatas</h1>");
            writer.println("    </header>");
            writer.println("    <div class='container'>");
            writer.println("        <div class='automata-container'>");

            for (String nombre : automatas.keySet()) {
                Automata a = automatas.get(nombre);
                writer.println("            <div class='automata'>");
                writer.println("                <div class='automata-header'>");
                writer.println("                    <h2>" + a.getNombre() + "</h2>");
                writer.println("                </div>");
                writer.println("                <div class='automata-body'>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Descripción</span>");
                writer.println("                        <div class='info-value'>" + a.getDescripcion() + "</div>");
                writer.println("                    </div>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Estados</span>");
                writer.println("                        <div class='info-value'>");
                for (String estado : a.getEstados()) {
                    writer.println("                            <span class='tag'>" + estado + "</span>");
                }
                writer.println("                        </div>");
                writer.println("                    </div>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Alfabeto</span>");
                writer.println("                        <div class='info-value'>");
                for (String simbolo : a.getAlfabeto()) {
                    writer.println("                            <span class='tag'>" + simbolo + "</span>");
                }
                writer.println("                        </div>");
                writer.println("                    </div>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Estado Inicial</span>");
                writer.println("                        <div class='info-value'>" + a.getEstadoInicial() + "</div>");
                writer.println("                    </div>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Estados Finales</span>");
                writer.println("                        <div class='info-value'>");
                for (String estado : a.getEstadosFinales()) {
                    writer.println("                            <span class='tag'>" + estado + "</span>");
                }
                writer.println("                        </div>");
                writer.println("                    </div>");
                
                writer.println("                    <div class='info-group'>");
                writer.println("                        <span class='info-label'>Transiciones</span>");
                writer.println("                        <ul class='transiciones-list'>");
                for (String origen : a.getTransiciones().keySet()) {
                    for (String simbolo : a.getTransiciones().get(origen).keySet()) {
                        String destino = a.getTransiciones().get(origen).get(simbolo);
                        writer.println("                            <li class='transicion-item'>");
                        writer.println("                                <span>" + origen + "</span> --(" + simbolo + ")--> <span>" + destino + "</span>");
                        writer.println("                            </li>");
                    }
                }
                writer.println("                        </ul>");
                writer.println("                    </div>");
                
                writer.println("                </div>");
                writer.println("            </div>");
            }

            writer.println("        </div>");
            writer.println("        <div class='footer'>");
            writer.println("            <p>AFDGraph | Generado: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</p>");
            writer.println("        </div>");
            writer.println("    </div>");
            writer.println("</body>");
            writer.println("</html>");
        } catch (Exception e) {
            System.err.println("Error al generar reporte de autómatas: " + e.getMessage());
        }
    }

    private void generarReporteErrores(ArrayList<String> errores) {
        try (PrintWriter writer = new PrintWriter("reporte_errores.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html lang='es'>");
            writer.println("<head>");
            writer.println("    <meta charset='UTF-8'>");
            writer.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            writer.println("    <title>Reporte de Errores</title>");
            writer.println("    <style>");
            writer.println("        :root {");
            writer.println("            --primary-color: #e74c3c;");
            writer.println("            --secondary-color: #c0392b;");
            writer.println("            --accent-color: #3498db;");
            writer.println("            --text-color: #333;");
            writer.println("            --border-color: #ddd;");
            writer.println("            --error-bg: #fdecea;");
            writer.println("        }");
            writer.println("        * {");
            writer.println("            margin: 0;");
            writer.println("            padding: 0;");
            writer.println("            box-sizing: border-box;");
            writer.println("        }");
            writer.println("        body {");
            writer.println("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;");
            writer.println("            color: var(--text-color);");
            writer.println("            line-height: 1.6;");
            writer.println("            background-color: #fafafa;");
            writer.println("            padding: 0;");
            writer.println("            margin: 0;");
            writer.println("        }");
            writer.println("        .container {");
            writer.println("            max-width: 900px;");
            writer.println("            margin: 0 auto;");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        header {");
            writer.println("            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));");
            writer.println("            color: white;");
            writer.println("            padding: 30px 0;");
            writer.println("            margin-bottom: 30px;");
            writer.println("            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);");
            writer.println("        }");
            writer.println("        h1 {");
            writer.println("            text-align: center;");
            writer.println("            font-size: 2.5rem;");
            writer.println("            font-weight: 300;");
            writer.println("            letter-spacing: 1px;");
            writer.println("        }");
            writer.println("        .card {");
            writer.println("            background-color: white;");
            writer.println("            border-radius: 8px;");
            writer.println("            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);");
            writer.println("            overflow: hidden;");
            writer.println("            margin-bottom: 30px;");
            writer.println("        }");
            writer.println("        .card-header {");
            writer.println("            background-color: var(--primary-color);");
            writer.println("            color: white;");
            writer.println("            padding: 15px 20px;");
            writer.println("        }");
            writer.println("        .card-header h2 {");
            writer.println("            margin: 0;");
            writer.println("            font-size: 1.4rem;");
            writer.println("            font-weight: 500;");
            writer.println("        }");
            writer.println("        .card-body {");
            writer.println("            padding: 20px;");
            writer.println("        }");
            writer.println("        .error-list {");
            writer.println("            list-style-type: none;");
            writer.println("        }");
            writer.println("        .error-item {");
            writer.println("            background-color: var(--error-bg);");
            writer.println("            border-left: 4px solid var(--primary-color);");
            writer.println("            margin-bottom: 10px;");
            writer.println("            padding: 15px;");
            writer.println("            border-radius: 4px;");
            writer.println("            display: flex;");
            writer.println("            align-items: center;");
            writer.println("            font-size: 0.95rem;");
            writer.println("        }");
            writer.println("        .error-icon {");
            writer.println("            margin-right: 15px;");
            writer.println("            color: var(--primary-color);");
            writer.println("            font-size: 1.4rem;");
            writer.println("        }");
            writer.println("        .no-errors {");
            writer.println("            background-color: #e8f5e9;");
            writer.println("            border-left: 4px solid #4caf50;");
            writer.println("            padding: 15px;");
            writer.println("            border-radius: 4px;");
            writer.println("            font-size: 1rem;");
            writer.println("            text-align: center;");
            writer.println("            color: #2e7d32;");
            writer.println("        }");
            writer.println("        .footer {");
            writer.println("            text-align: center;");
            writer.println("            margin-top: 40px;");
            writer.println("            padding: 20px;");
            writer.println("            color: #777;");
            writer.println("            font-size: 0.9rem;");
            writer.println("            border-top: 1px solid var(--border-color);");
            writer.println("        }");
            writer.println("    </style>");
            writer.println("</head>");
            writer.println("<body>");
            writer.println("    <header>");
            writer.println("        <h1>Reporte de Errores Léxicos</h1>");
            writer.println("    </header>");
            writer.println("    <div class='container'>");
            writer.println("        <div class='card'>");
            writer.println("            <div class='card-header'>");
            writer.println("                <h2>Listado de Errores</h2>");
            writer.println("            </div>");
            writer.println("            <div class='card-body'>");

            if (errores.isEmpty()) {
                writer.println("                <div class='no-errors'>");
                writer.println("                    <p>✓ No se encontraron errores léxicos en el análisis.</p>");
                writer.println("                </div>");
            } else {
                writer.println("                <ul class='error-list'>");
                for (String error : errores) {
                    writer.println("                    <li class='error-item'>");
                    writer.println("                        <span class='error-icon'>⚠</span>");
                    writer.println("                        <span>" + error + "</span>");
                    writer.println("                    </li>");
                }
                writer.println("                </ul>");
            }

            writer.println("            </div>");
            writer.println("        </div>");
            writer.println("        <div class='footer'>");
            writer.println("            <p>AFDGraph | Generado: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</p>");
            writer.println("        </div>");
            writer.println("    </div>");
            writer.println("</body>");
            writer.println("</html>");
        } catch (Exception e) {
            System.err.println("Error al generar reporte de errores: " + e.getMessage());
        }
    }
}