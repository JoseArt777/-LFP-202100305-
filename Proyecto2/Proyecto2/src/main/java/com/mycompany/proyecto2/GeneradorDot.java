/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Generador de archivos DOT para Graphviz
 */
public class GeneradorDot {
    
    /**
     * Genera un archivo DOT para el mundo especificado
     * @param mundo Mundo a representar
     * @param rutaSalida Ruta del archivo DOT de salida
     * @return true si se generó correctamente, false en caso contrario
     */
    public boolean generarDot(Mundo mundo, String rutaSalida) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {
            // Cabecera del archivo DOT
            writer.write("digraph \"" + mundo.getNombre() + "\" {\n");
            writer.write("  rankdir=LR;\n");
            writer.write("  node [style=filled, fontname=Arial];\n");
            writer.write("  edge [len=1.5, fontname=Arial];\n");
            writer.write("  labelloc=\"t\";\n");
            writer.write("  fontname=Arial;\n\n");
            
            // Generar nodos para los lugares
            for (Lugar lugar : mundo.getLugares().values()) {
                writer.write(String.format("  %s [label=\"%s\", shape=%s, fillcolor=%s];\n",
                        lugar.getNombre(),
                        lugar.getNombre(),
                        lugar.obtenerForma(),
                        lugar.obtenerColor()));
            }
            
            writer.write("\n");
            
            // Generar nodos para los objetos que no están asociados a lugares
            Map<String, Objeto> objetosIndependientes = new HashMap<>();
            int contadorObjetos = 0;
            
            for (Objeto objeto : mundo.getObjetos()) {
                if (objeto.isUbicacionExplicita()) {
                    String idObjeto = "obj_" + contadorObjetos++;
                    objetosIndependientes.put(idObjeto, objeto);
                    
                    writer.write(String.format("  %s [label=<%s %s>, shape=%s, fillcolor=%s];\n",
                            idObjeto,
                            obtenerEtiquetaConEmoji(objeto),
                            objeto.getNombre(),
                            objeto.obtenerForma(),
                            objeto.obtenerColor()));
                }
            }
            
            writer.write("\n");
            
            // Generar conexiones entre lugares
            for (Conexion conexion : mundo.getConexiones()) {
                writer.write(String.format("  %s -> %s [label=\"%s\", style=%s, color=%s];\n",
                        conexion.getLugarOrigen(),
                        conexion.getLugarDestino(),
                        conexion.getTipoCamino(),
                        conexion.obtenerEstiloLinea(),
                        conexion.obtenerColorLinea()));
            }
            
            writer.write("\n");
            
            // Generar conexiones entre objetos y lugares
            for (Objeto objeto : mundo.getObjetos()) {
                if (!objeto.isUbicacionExplicita() && objeto.getLugarAsociado() != null) {
                    // Crear un nodo para el objeto
                    String idObjeto = "obj_" + contadorObjetos++;
                    
                    writer.write(String.format("  %s [label=<%s %s>, shape=%s, fillcolor=%s];\n",
                            idObjeto,
                            obtenerEtiquetaConEmoji(objeto),
                            objeto.getNombre(),
                            objeto.obtenerForma(),
                            objeto.obtenerColor()));
                    
                    // Conectar el objeto con su lugar
                    writer.write(String.format("  %s -> %s [label=\"en\", style=dotted, dir=none];\n",
                            idObjeto,
                            objeto.getLugarAsociado()));
                }
            }
            
            // Cerrar el gráfico
            writer.write("}\n");
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al generar archivo DOT: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Ejecuta el comando de Graphviz para convertir el archivo DOT a una imagen
     * @param rutaDot Ruta del archivo DOT
     * @param rutaImagen Ruta de la imagen de salida
     * @param formato Formato de la imagen (png, svg, pdf, etc.)
     * @return true si se generó correctamente, false en caso contrario
     */
    public boolean generarImagen(String rutaDot, String rutaImagen, String formato) {
        try {
            // Construir el comando para ejecutar Graphviz
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "dot", "-T" + formato, rutaDot, "-o", rutaImagen
            );
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al generar imagen: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el contenido DOT para el mundo especificado (sin escribir a archivo)
     * @param mundo Mundo a representar
     * @return String con el contenido DOT
     */
    public String obtenerContenidoDot(Mundo mundo) {
        StringBuilder dot = new StringBuilder();
        
        // Cabecera del archivo DOT
        dot.append("digraph \"").append(mundo.getNombre()).append("\" {\n");
        dot.append("  rankdir=LR;\n");
        dot.append("  node [style=filled, fontname=Arial];\n");
        dot.append("  edge [len=1.5, fontname=Arial];\n");
        dot.append("  labelloc=\"t\";\n");
        dot.append("  fontname=Arial;\n\n");
        
        // Generar nodos para los lugares
        for (Lugar lugar : mundo.getLugares().values()) {
            dot.append(String.format("  %s [label=\"%s\", shape=%s, fillcolor=%s];\n",
                    lugar.getNombre(),
                    lugar.getNombre(),
                    lugar.obtenerForma(),
                    lugar.obtenerColor()));
        }
        
        dot.append("\n");
        
        // Generar nodos para los objetos que no están asociados a lugares
        Map<String, Objeto> objetosIndependientes = new HashMap<>();
        int contadorObjetos = 0;
        
        for (Objeto objeto : mundo.getObjetos()) {
            if (objeto.isUbicacionExplicita()) {
                String idObjeto = "obj_" + contadorObjetos++;
                objetosIndependientes.put(idObjeto, objeto);
                
                dot.append(String.format("  %s [label=<%s %s>, shape=%s, fillcolor=%s];\n",
                        idObjeto,
                        obtenerEtiquetaConEmoji(objeto),
                        objeto.getNombre(),
                        objeto.obtenerForma(),
                        objeto.obtenerColor()));
            }
        }
        
        dot.append("\n");
        
        // Generar conexiones entre lugares
        for (Conexion conexion : mundo.getConexiones()) {
            dot.append(String.format("  %s -> %s [label=\"%s\", style=%s, color=%s];\n",
                    conexion.getLugarOrigen(),
                    conexion.getLugarDestino(),
                    conexion.getTipoCamino(),
                    conexion.obtenerEstiloLinea(),
                    conexion.obtenerColorLinea()));
        }
        
        dot.append("\n");
        
        // Generar conexiones entre objetos y lugares
        for (Objeto objeto : mundo.getObjetos()) {
            if (!objeto.isUbicacionExplicita() && objeto.getLugarAsociado() != null) {
                // Crear un nodo para el objeto
                String idObjeto = "obj_" + contadorObjetos++;
                
                dot.append(String.format("  %s [label=<%s %s>, shape=%s, fillcolor=%s];\n",
                        idObjeto,
                        obtenerEtiquetaConEmoji(objeto),
                        objeto.getNombre(),
                        objeto.obtenerForma(),
                        objeto.obtenerColor()));
                
                // Conectar el objeto con su lugar
                dot.append(String.format("  %s -> %s [label=\"en\", style=dotted, dir=none];\n",
                        idObjeto,
                        objeto.getLugarAsociado()));
            }
        }
        
        // Cerrar el gráfico
        dot.append("}\n");
        
        return dot.toString();
    }

    // Método privado para generar etiquetas con emojis coloreados
private String obtenerEtiquetaConEmoji(Objeto objeto) {
    String emoji = objeto.obtenerEmoji();
    String tipo = objeto.getTipo().toLowerCase();
    
    // Depuración
    System.out.println("Tipo: " + tipo);
    System.out.println("Emoji original: " + emoji);
    System.out.println("Código Unicode: " + unicodeEscape(emoji));
    
    return emoji; // Temporal, para ver qué está pasando
}

// Método auxiliar para ver códigos Unicode
private String unicodeEscape(String input) {
    StringBuilder b = new StringBuilder();
    for (char c : input.toCharArray()) {
        b.append(String.format("\\u%04X", (int) c));
    }
    return b.toString();
}
  
}