package com.mycompany.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main extends JFrame {
    // Componentes de GUI
    private JTextArea textArea;
    private JPanel imagePanel;
    private JButton loadButton, clearButton, analyzeButton, reportButton;
    private JComboBox<String> mapSelector;
    
    // Instancias de clases
    private Analyzer analyzer;
private java.util.List<MapModel> maps = new java.util.ArrayList<>();
    private GraphvizManager graphviz;
    
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
    
    public Main() {
            maps = new ArrayList<>();

        // Configuración básica de la ventana
        setTitle("Generador Visual de Mapas Narrativos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Inicialización de componentes
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        
        loadButton = new JButton("Cargar Archivo");
        clearButton = new JButton("Limpiar Área");
        analyzeButton = new JButton("Analizar Archivo");
        reportButton = new JButton("Generar Reportes");
        
        mapSelector = new JComboBox<>();
        mapSelector.setEnabled(false);
        
        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(analyzeButton);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.WEST);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(reportButton, BorderLayout.NORTH);
        rightPanel.add(mapSelector, BorderLayout.SOUTH);
        
        topPanel.add(rightPanel, BorderLayout.EAST);
        
        // Organización de paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, imagePanel);
        splitPane.setResizeWeight(0.5);
        
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        // Añadir listeners a botones
        loadButton.addActionListener(e -> loadFile());
        clearButton.addActionListener(e -> clearText());
        analyzeButton.addActionListener(e -> analyzeCode());
        reportButton.addActionListener(e -> generateReports());
        
        // Listener para el selector de mapas
        mapSelector.addActionListener(e -> {
            if (mapSelector.getSelectedIndex() >= 0 && maps != null && !maps.isEmpty()) {
                MapModel selectedMap = maps.get(mapSelector.getSelectedIndex());
                displayMap(selectedMap);
            }
        });
    }
    
    // Métodos para manejar acciones de la interfaz
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = readFile(selectedFile);
                textArea.setText(content);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar el archivo: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearText() {
        textArea.setText("");
    }
    
    private void analyzeCode() {
        String code = textArea.getText();
        if (code == null || code.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay código para analizar.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        analyzer = new Analyzer(code);
        analyzer.lexicalAnalysis();
        maps = analyzer.syntacticAnalysis();
        
        if (maps != null && !maps.isEmpty()) {
            mapSelector.removeAllItems();
            for (MapModel map : maps) {
                mapSelector.addItem(map.getName());
            }
            mapSelector.setEnabled(true);
            mapSelector.setSelectedIndex(0);
            
            // Mostrar el primer mapa
            displayMap(maps.get(0));
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron mapas válidos.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void displayMap(MapModel map) {
        if (map != null) {
            graphviz = new GraphvizManager(map);
            String dotCode = graphviz.generateDotCode();
            
            try {
                String imagePath = graphviz.generateImage(dotCode, "temp_map.png");
                
                if (imagePath != null) {
                    ImageIcon icon = new ImageIcon(imagePath);
                    JLabel imageLabel = new JLabel(icon);
                    
                    imagePanel.removeAll();
                    imagePanel.add(imageLabel);
                    imagePanel.revalidate();
                    imagePanel.repaint();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al generar la imagen: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void generateReports() {
        if (analyzer == null) {
            JOptionPane.showMessageDialog(this, "Primero debe analizar un archivo.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            ReportGenerator.generateTokenReport(analyzer.getTokens(), "tokens.html");
            ReportGenerator.generateErrorReport(analyzer.getLexicalErrors(), analyzer.getSyntaxErrors(), "errors.html");
            
            JOptionPane.showMessageDialog(this, "Reportes generados correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar reportes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this, 
                "Generador Visual de Mapas Narrativos\n" +
                "Desarrollado por: [Tu Nombre]\n" +
                "Carnet: [Tu Carnet]",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Método para leer/escribir archivos
    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    private void writeFile(String content, String fileName) throws IOException {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
        }
    }
}