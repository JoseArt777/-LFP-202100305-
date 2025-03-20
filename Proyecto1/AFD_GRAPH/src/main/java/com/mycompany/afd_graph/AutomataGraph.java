package com.mycompany.afd_graph;
/**
 *
 * Autor: iosea
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class AutomataGraph {
    public void createGraph(Automaton automaton) {
        StringBuilder dotCode = new StringBuilder();
        dotCode.append("digraph automaton {\n");
        dotCode.append("  rankdir=LR;\n"); // De izquierda a derecha
        
        // Configurar nodos
        for (State state : automaton.getStates()) {
            dotCode.append("  \"").append(state.getName()).append("\"");
            
            // Inicio de atributos
            dotCode.append(" [");
            
            // Forma del nodo
            if (state.isAccepting()) {
                dotCode.append("shape=doublecircle");
            } else {
                dotCode.append("shape=circle");
            }
            
            // Estilo para estado inicial
            if (state.isInitial()) {
                dotCode.append(", style=bold");
            }
            
            // Cierre de atributos
            dotCode.append("];\n");
        }
        
        // Configurar transiciones
        for (State state : automaton.getStates()) {
            for (String symbol : automaton.getAlphabet()) {
                String nextStateName = automaton.getTransition(state.getName(), symbol);
                if (nextStateName != null) {
                    dotCode.append("  \"").append(state.getName()).append("\" -> \"")
                           .append(nextStateName).append("\" [label=\"").append(symbol).append("\"];\n");
                }
            }
        }
        
        dotCode.append("}");
        
        // Guardar el código DOT en un archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("automaton.dot"))) {
            writer.write(dotCode.toString());
            System.out.println("Archivo DOT generado correctamente: automaton.dot");
            
            // Intentar generar la imagen usando el comando externo de Graphviz
            try {
                ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", "automaton.dot", "-o", "automaton.png");
                Process process = pb.start();
                int exitCode = process.waitFor();
                
                if (exitCode == 0) {
                    System.out.println("Imagen PNG generada correctamente: automaton.png");
                } else {
                    System.err.println("Error al generar la imagen. Código de salida: " + exitCode);
                    System.err.println("Para generar la imagen manualmente, ejecuta:");
                    System.err.println("dot -Tpng automaton.dot -o automaton.png");
                }
            } catch (Exception e) {
                System.err.println("No se pudo ejecutar el comando 'dot'. Es posible que Graphviz no esté instalado.");
                System.err.println("Para generar la imagen manualmente, instala Graphviz y ejecuta:");
                System.err.println("dot -Tpng automaton.dot -o automaton.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}