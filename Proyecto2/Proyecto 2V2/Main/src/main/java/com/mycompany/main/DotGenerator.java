/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author iosea
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



/**
 * Generador de archivos DOT para Graphviz.
 */
public class DotGenerator {
    
    /**
     * Genera un archivo DOT para representar un mundo.
     * 
     * @param world Mundo a representar
     * @param filePath Ruta del archivo DOT a generar
     * @throws IOException Si ocurre un error al escribir el archivo
     */
    public void generateDotFile(World world, String filePath) throws IOException {
        StringBuilder dotContent = new StringBuilder();
        
        // Inicio del archivo DOT
        dotContent.append("digraph \"").append(sanitizeString(world.getName())).append("\" {\n");
        dotContent.append("  // Configuración general\n");
dotContent.append("  graph [layout=neato, splines=true, overlap=false, fontname=\"Arial\"]; // permite uso de coordenadas\n");
        dotContent.append("  node [fontname=\"Arial\", style=filled];\n");
        dotContent.append("  edge [fontname=\"Arial\"];\n\n");
        
        // Generar nodos para lugares
        dotContent.append("  // Lugares\n");
        Map<String, Place> placeMap = new HashMap<>();
        for (Place place : world.getPlaces()) {
            placeMap.put(place.getName(), place);
            dotContent.append("  \"").append(sanitizeString(place.getName())).append("\" [");
            dotContent.append("shape=").append(place.getShape()).append(", ");
            dotContent.append("fillcolor=\"").append(place.getFillColor()).append("\", ");
            dotContent.append("label=\"").append(sanitizeString(place.getName())).append("\", ");
            dotContent.append("pos=\"").append(place.getX()).append(",").append(place.getY()).append("!\"];\n");
        }
        dotContent.append("\n");
        
        // Generar nodos para objetos con coordenadas específicas
        dotContent.append("  // Objetos en coordenadas específicas\n");
        for (MapObject object : world.getObjects()) {
            if (!object.isAtPlace()) {
                String objectId = "obj_" + sanitizeString(object.getName()).replaceAll("\\s+", "_");
                dotContent.append("  \"").append(objectId).append("\" [");
                dotContent.append("shape=").append(object.getShape()).append(", ");
                dotContent.append("fillcolor=\"").append(object.getFillColor()).append("\", ");
                dotContent.append("label=\"").append(object.getEmoji()).append(" ").append(sanitizeString(object.getName())).append("\", ");
                dotContent.append("pos=\"").append(object.getX()).append(",").append(object.getY()).append("!\"];\n");
            }
        }
        dotContent.append("\n");
        
        // Generar nodos para objetos en lugares
        dotContent.append("  // Objetos en lugares\n");
        for (MapObject object : world.getObjects()) {
            if (object.isAtPlace()) {
                String objectId = "obj_" + sanitizeString(object.getName()).replaceAll("\\s+", "_");
                dotContent.append("  \"").append(objectId).append("\" [");
                dotContent.append("shape=").append(object.getShape()).append(", ");
                dotContent.append("fillcolor=\"").append(object.getFillColor()).append("\", ");
                dotContent.append("label=\"").append(object.getEmoji()).append(" ").append(sanitizeString(object.getName())).append("\"];\n");
                
                // Conexión entre el objeto y el lugar
                dotContent.append("  \"").append(objectId).append("\" -> \"").append(sanitizeString(object.getPlaceId())).append("\" [");
                dotContent.append("label=\"en\", dir=none, style=dotted];\n");
            }
        }
        dotContent.append("\n");
        
        // Generar conexiones entre lugares
        dotContent.append("  // Conexiones entre lugares\n");
        for (Connection conn : world.getConnections()) {
            dotContent.append("  \"").append(sanitizeString(conn.getSource())).append("\" -> \"").append(sanitizeString(conn.getTarget())).append("\" [");
            dotContent.append("label=\"").append(sanitizeString(conn.getType())).append("\", ");
            dotContent.append("color=\"").append(conn.getLineColor()).append("\", ");
            dotContent.append("style=").append(conn.getLineStyle()).append("];\n");
        }
        
        // Fin del archivo DOT
        dotContent.append("}\n");
        
        // Escribir archivo con codificación UTF-8
        try (FileWriter writer = new FileWriter(filePath, java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write(dotContent.toString());
        }
    }
    
    /**
     * Limpia una cadena para uso en GraphViz, removiendo acentos y caracteres especiales.
     * 
     * @param input Cadena a limpiar
     * @return Cadena limpia
     */
    private String sanitizeString(String input) {
        if (input == null) return "";
        
        // Reemplazar caracteres acentuados por sus equivalentes sin acento
        String normalized = java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        
        // Reemplazar la ñ/Ñ por n/N
        normalized = normalized.replace('ñ', 'n').replace('Ñ', 'N');
        
        // Reemplazar otros caracteres problemáticos para Graphviz
        normalized = normalized.replace("\"", "'")
                              .replace("\\", "\\\\");
        
        return normalized;
    }
}