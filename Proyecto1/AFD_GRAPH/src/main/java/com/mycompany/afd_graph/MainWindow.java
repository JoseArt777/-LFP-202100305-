/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final FileAnalyzer fileAnalyzer;
    private final JComboBox<String> automataSelector;
    private final AutomataGraph automataGraph;
    private final AutomataViewer automataViewer;
    private final AutomataManager automataManager;

    public MainWindow() {
        fileAnalyzer = new FileAnalyzer();
        automataGraph = new AutomataGraph();
        automataManager = new AutomataManager();
        
        // Configuración básica de la ventana
        setTitle("AFDGraph - Visualizador de Autómatas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Crear componentes de la UI
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel superior con botones
        JPanel controlPanel = new JPanel();
        JButton loadButton = new JButton("Analizar Archivo");
        automataSelector = new JComboBox<>();
        JButton graphButton = new JButton("Graficar Autómata");
        JButton tokenReportButton = new JButton("Reporte de Tokens");
        JButton errorReportButton = new JButton("Reporte de Errores");
        
        controlPanel.add(loadButton);
        controlPanel.add(new JLabel("Seleccionar Autómata:"));
        controlPanel.add(automataSelector);
        controlPanel.add(graphButton);
        controlPanel.add(tokenReportButton);
        controlPanel.add(errorReportButton);
        
        // Panel para visualizar el autómata
        automataViewer = new AutomataViewer();
        
        // Agregar componentes al panel principal
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(automataViewer), BorderLayout.CENTER);
        
        // Configurar acciones de los botones
        loadButton.addActionListener(e -> loadAutomataFile());
        graphButton.addActionListener(e -> graphSelectedAutomata());
        tokenReportButton.addActionListener(e -> showTokenReport());
        errorReportButton.addActionListener(e -> showErrorReport());
        
        setContentPane(mainPanel);
    }
    
    private void loadAutomataFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos LFP", "lfp"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                fileAnalyzer.analyzeFile(selectedFile.getAbsolutePath());
                
                // Actualizar el gestor de autómatas
                updateAutomataSelector();
                
                JOptionPane.showMessageDialog(this, 
                    "Archivo analizado correctamente. Se encontraron " + 
                    fileAnalyzer.getAutomataMap().size() + " autómatas.",
                    "Análisis Completado", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al analizar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateAutomataSelector() {
        automataSelector.removeAllItems();
        for (String automataName : fileAnalyzer.getAutomataMap().keySet()) {
            automataSelector.addItem(automataName);
        }
    }
    
    private void graphSelectedAutomata() {
        String selectedAutomataName = (String) automataSelector.getSelectedItem();
        if (selectedAutomataName != null) {
            Automaton automaton = automataManager.getAutomaton(selectedAutomataName);
            automataGraph.createGraph(automaton);
            JOptionPane.showMessageDialog(this, "Imagen del autómata generada: automaton.png",
                    "Generación Completa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor selecciona un autómata primero.",
                                          "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showTokenReport() {
        // Implementar la lógica para generar el reporte de tokens
    }
    
    private void showErrorReport() {
        // Implementar la lógica para generar el reporte de errores léxicos
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}