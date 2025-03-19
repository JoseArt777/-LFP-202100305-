/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;


import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MainWindow extends JFrame {
    private final AutomataManager automataManager;
    private final JComboBox<String> automataSelector;
    private final AutomataViewer automataViewer;

    public MainWindow() {
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
                String content = FileHandler.readFile(selectedFile);
                LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(content);
                lexicalAnalyzer.analyze();
                
                // Actualizar el gestor de autómatas
                automataManager.loadAutomata(lexicalAnalyzer);
                
                // Actualizar el selector de autómatas
                updateAutomataSelector();
                
                JOptionPane.showMessageDialog(this, 
                    "Archivo analizado correctamente. Se encontraron " + 
                    automataManager.getAutomataCount() + " autómatas.",
                    "Análisis Completado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al analizar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateAutomataSelector() {
        automataSelector.removeAllItems();
        for (String automataName : automataManager.getAutomataNames()) {
            automataSelector.addItem(automataName);
        }
    }
    
    private void graphSelectedAutomata() {
        String selectedName = (String) automataSelector.getSelectedItem();
        if (selectedName != null) {
            Automaton automaton = automataManager.getAutomaton(selectedName);
            if (automaton != null) {
                automataViewer.setAutomaton(automaton);
                automataViewer.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione un autómata para graficar.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showTokenReport() {
        if (automataManager.getAutomataCount() > 0) {
            String report = ReportGenerator.generateTokenReport(automataManager.getLexicalAnalyzer());
            showReport("Reporte de Tokens", report);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No hay autómatas cargados para generar el reporte.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showErrorReport() {
        if (automataManager.getAutomataCount() > 0) {
            String report = ReportGenerator.generateErrorReport(automataManager.getLexicalAnalyzer());
            showReport("Reporte de Errores", report);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No hay autómatas cargados para generar el reporte.",
                "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showReport(String title, String content) {
        JFrame reportFrame = new JFrame(title);
        reportFrame.setSize(600, 400);
        reportFrame.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        
        reportFrame.add(new JScrollPane(textArea));
        reportFrame.setVisible(true);
    }
}