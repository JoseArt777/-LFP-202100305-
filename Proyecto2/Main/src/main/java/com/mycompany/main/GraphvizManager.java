package com.mycompany.main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GraphvizManager {
    private MapModel map;
    
    // Mapas para estilos de lugares, conexiones y objetos
    private static final Map<String, String[]> PLACE_STYLES = new HashMap<>();
    private static final Map<String, String[]> CONNECTION_STYLES = new HashMap<>();
    private static final Map<String, String[]> OBJECT_STYLES = new HashMap<>();
    
    // Inicializar los mapas de estilos
    static {
        // Formato: [shape, fillcolor]
        PLACE_STYLES.put("playa", new String[]{"ellipse", "lightblue"});
        PLACE_STYLES.put("cueva", new String[]{"box", "gray"});
        PLACE_STYLES.put("templo", new String[]{"octagon", "gold"});
        PLACE_STYLES.put("jungla", new String[]{"parallelogram", "forestgreen"});
        PLACE_STYLES.put("montaña", new String[]{"triangle", "sienna"});
        PLACE_STYLES.put("pueblo", new String[]{"house", "burlywood"});
        PLACE_STYLES.put("isla", new String[]{"invtriangle", "lightgoldenrod"});
        PLACE_STYLES.put("río", new String[]{"hexagon", "deepskyblue"});
        PLACE_STYLES.put("volcán", new String[]{"doublecircle", "orangered"});
        PLACE_STYLES.put("pantano", new String[]{"trapezium", "darkseagreen"});
        
        // Formato: [style, color]
        CONNECTION_STYLES.put("camino", new String[]{"solid", "black"});
        CONNECTION_STYLES.put("puente", new String[]{"dotted", "gray"});
        CONNECTION_STYLES.put("sendero", new String[]{"dashed", "saddlebrown"});
        CONNECTION_STYLES.put("carretera", new String[]{"solid", "darkgray"});
        CONNECTION_STYLES.put("nado", new String[]{"dashed", "deepskyblue"});
        CONNECTION_STYLES.put("lancha", new String[]{"solid", "blue"});
        CONNECTION_STYLES.put("teleférico", new String[]{"dotted", "purple"});
        
        // Formato: [shape, fillcolor, emoji]
        OBJECT_STYLES.put("tesoro", new String[]{"box3d", "gold", "\\uD83C\\uDF81"});
        OBJECT_STYLES.put("llave", new String[]{"pentagon", "lightsteelblue", "\\uD83D\\uDD11"});
        OBJECT_STYLES.put("arma", new String[]{"diamond", "orangered", "\\uD83D\\uDDE1\\uFE0F"});
        OBJECT_STYLES.put("objeto mágico", new String[]{"component", "violet", "\\u2728"});
        OBJECT_STYLES.put("poción", new String[]{"cylinder", "plum", "\\u2697\\uFE0F"});
        OBJECT_STYLES.put("trampa", new String[]{"hexagon", "crimson", "\\uD83D\\uDCA3"});
        OBJECT_STYLES.put("libro", new String[]{"note", "navajowhite", "\\uD83D\\uDCD5"});
        OBJECT_STYLES.put("herramienta", new String[]{"folder", "darkkhaki", "\\uD83D\\uDEE0\\uFE0F"});
        OBJECT_STYLES.put("bandera", new String[]{"tab", "white", "\\uD83D\\uDEA9"});
        OBJECT_STYLES.put("gema", new String[]{"egg", "deepskyblue", "\\uD83D\\uDC8E"});
    }
    
    public GraphvizManager(MapModel map) {
        this.map = map;
    }
    
    public String generateDotCode() {
        StringBuilder dot = new StringBuilder();
        dot.append("digraph \"").append(map.getName()).append("\" {\n");
        dot.append("  node [fontname=\"Arial\"];\n");
        dot.append("  edge [fontname=\"Arial\"];\n");
        
        // Generar código para lugares
        for (MapModel.Place place : map.getPlaces()) {
            String[] style = getPlaceStyle(place.getType());
            dot.append("  \"").append(place.getName()).append("\" [");
            dot.append("shape=").append(style[0]).append(", ");
            dot.append("fillcolor=\"").append(style[1]).append("\", ");
            dot.append("style=filled, ");
            dot.append("label=\"").append(place.getName()).append("\"");
            dot.append("];\n");
        }
        
        // Generar código para conexiones
        for (MapModel.Connection conn : map.getConnections()) {
            String[] style = getConnectionStyle(conn.getType());
            dot.append("  \"").append(conn.getFrom()).append("\" -> \"");
            dot.append(conn.getTo()).append("\" [");
            dot.append("style=").append(style[0]).append(", ");
            dot.append("color=\"").append(style[1]).append("\", ");
            dot.append("label=\"").append(conn.getType()).append("\"");
            dot.append("];\n");
        }
        
        // Generar código para objetos
        for (MapModel.MapObject obj : map.getObjects()) {
            String[] style = getObjectStyle(obj.getType());
            String objectId = "obj_" + obj.getName().replaceAll("\\s+", "_");
            
            dot.append("  \"").append(objectId).append("\" [");
            dot.append("shape=").append(style[0]).append(", ");
            dot.append("fillcolor=\"").append(style[1]).append("\", ");
            dot.append("style=filled, ");
            dot.append("label=\"").append(style[2]).append(" ").append(obj.getName()).append("\"");
            dot.append("];\n");
            
            // Si el objeto está en un lugar específico
            if (!obj.isCoordinate()) {
                dot.append("  \"").append(objectId).append("\" -> \"");
                dot.append(obj.getLocation()).append("\" [");
                dot.append("style=dotted, ");
                dot.append("dir=none, ");
                dot.append("label=\"en\"");
                dot.append("];\n");
            }
            // Si está en coordenadas específicas, podríamos usar un subgrafo para posicionarlo
            // pero esto requeriría más implementación
        }
        
        dot.append("}");
        return dot.toString();
    }
    
    public String generateImage(String dotCode, String outputPath) {
        try {
            // Guardar código DOT en archivo temporal
            File dotFile = File.createTempFile("map", ".dot");
            FileWriter writer = new FileWriter(dotFile);
            writer.write(dotCode);
            writer.close();
            
            // Ejecutar Graphviz
            ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFile.getAbsolutePath(), "-o", outputPath);
            Process process = pb.start();
            process.waitFor();
            
            return outputPath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Métodos para mapear tipos a estilos visuales
    private String[] getPlaceStyle(String type) {
        String[] style = PLACE_STYLES.get(type.toLowerCase());
        if (style == null) {
            // Estilo por defecto si no se encuentra el tipo
            return new String[]{"ellipse", "lightgray"};
        }
        return style;
    }
    
    private String[] getConnectionStyle(String type) {
        String[] style = CONNECTION_STYLES.get(type.toLowerCase());
        if (style == null) {
            // Estilo por defecto si no se encuentra el tipo
            return new String[]{"solid", "black"};
        }
        return style;
    }
    
    private String[] getObjectStyle(String type) {
        String[] style = OBJECT_STYLES.get(type.toLowerCase());
        if (style == null) {
            // Estilo por defecto si no se encuentra el tipo
            return new String[]{"box", "lightgray", "📦"};
        }
        return style;
    }
}