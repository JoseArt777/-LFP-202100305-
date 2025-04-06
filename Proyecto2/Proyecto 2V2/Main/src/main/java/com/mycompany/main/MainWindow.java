/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.main;

/**
 *
 * @author iosea
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;



/**
 * Ventana principal de la aplicación.
 */
public class MainWindow extends JFrame {
    
    private JTextArea textArea;
    private JPanel imagePanel;
    private JComboBox<String> mapSelector;
    private JButton loadButton, clearButton, analyzeButton, reportButton, aboutButton;
    
    private Lexer lexer;
    private Parser parser;
    private ArrayList<World> worlds;
    private ArrayList<Token> tokens;
    private ArrayList<Token> errors;
    
    private Map<String, ImageIcon> mapImages;
    
    /**
     * Constructor de la ventana principal.
     */
    public MainWindow() {
        // Configuración básica de la ventana
        setTitle("Generador Visual de Mapas Narrativos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        
        // Inicializar componentes
        initComponents();
        
        // Inicializar estructuras de datos
        lexer = new Lexer();
        parser = new Parser();
        worlds = new ArrayList<>();
        tokens = new ArrayList<>();
        errors = new ArrayList<>();
        mapImages = new HashMap<>();
        
        // Configurar layout
        layoutComponents();
        
        // Configurar eventos
        setupEventListeners();
    }
    
    /**
     * Inicializa los componentes de la interfaz.
     */
    private void initComponents() {
        // Área de texto
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Panel de imagen
        imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String selectedMap = (String) mapSelector.getSelectedItem();
                if (selectedMap != null && mapImages.containsKey(selectedMap)) {
                    ImageIcon icon = mapImages.get(selectedMap);
                    if (icon != null) {
                        g.drawImage(icon.getImage(), 0, 0, this);
                    }
                }
            }
        };
        imagePanel.setBackground(Color.WHITE);
        
        // Selector de mapas
        mapSelector = new JComboBox<>();
        mapSelector.setPreferredSize(new Dimension(200, 25));
        
        // Botones
        loadButton = new JButton("Cargar Archivo");
        clearButton = new JButton("Limpiar Área");
        analyzeButton = new JButton("Analizar Archivo");
        reportButton = new JButton("Generar Reportes");
        aboutButton = new JButton("Acerca de");
    }
    
    /**
     * Configura el layout de los componentes.
     */
    private void layoutComponents() {
        // Panel principal con GridBagLayout para mayor flexibilidad
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Panel izquierdo (área de texto y botones)
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 500));
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(analyzeButton);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Panel derecho (visualización de mapas y controles)
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        JScrollPane imageScrollPane = new JScrollPane(imagePanel);
        imageScrollPane.setPreferredSize(new Dimension(450, 500));
        rightPanel.add(imageScrollPane, BorderLayout.CENTER);
        
        // Panel superior derecho (selector y botones)
        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRightPanel.add(reportButton);
        topRightPanel.add(new JLabel("Seleccionar mapa:"));
        topRightPanel.add(mapSelector);
        rightPanel.add(topRightPanel, BorderLayout.NORTH);
        
        // Agregar paneles al panel principal
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 5);
        mainPanel.add(leftPanel, gbc);
        
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 5, 10, 10);
        mainPanel.add(rightPanel, gbc);
        
        // Panel inferior para el botón "Acerca de"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(aboutButton);
        
        // Agregar paneles al marco
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Configura los listeners de eventos para los componentes.
     */
    private void setupEventListeners() {
        // Botón Cargar Archivo
        loadButton.addActionListener(e -> loadFile());
        
        // Botón Limpiar Área
        clearButton.addActionListener(e -> textArea.setText(""));
        
        // Botón Analizar Archivo
        analyzeButton.addActionListener(e -> analyzeFile());
        
        // Selector de mapas
        mapSelector.addActionListener(e -> {
            String selectedMap = (String) mapSelector.getSelectedItem();
            if (selectedMap != null) {
                generateMapImage(selectedMap);
                imagePanel.repaint();
            }
        });
        
        // Botón Generar Reportes
        reportButton.addActionListener(e -> generateReports());
        
        // Botón Acerca de
        aboutButton.addActionListener(e -> showAboutDialog());
    }
    
    /**
     * Carga un archivo en el área de texto.
     */
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
                reader.close();
                
                textArea.setText(content.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al cargar el archivo: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Analiza el contenido del área de texto.
     */
    private void analyzeFile() {
        String input = textArea.getText();
        if (input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El área de texto está vacía. Por favor cargue un archivo.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Realizar análisis léxico
        lexer.analyze(input);
        tokens = lexer.getTokens();
        errors = lexer.getErrors();
        
        // Realizar análisis sintáctico
        parser.parse(tokens);
        worlds = parser.getWorlds();
        errors.addAll(parser.getErrors());
        
        // Actualizar selector de mapas
        mapSelector.removeAllItems();
        mapImages.clear();
        
        if (worlds.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No se encontraron mapas válidos en el archivo.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (World world : worlds) {
                mapSelector.addItem(world.getName());
            }
            
            // Generar imagen para el primer mapa
            if (mapSelector.getItemCount() > 0) {
                mapSelector.setSelectedIndex(0);
                String selectedMap = (String) mapSelector.getSelectedItem();
                generateMapImage(selectedMap);
            }
        }
    }
    
    /**
     * Genera una imagen para el mapa seleccionado.
     */
    private void generateMapImage(String mapName) {
        // Buscar el mundo correspondiente
        World selectedWorld = null;
        for (World world : worlds) {
            if (world.getName().equals(mapName)) {
                selectedWorld = world;
                break;
            }
        }
        
        if (selectedWorld != null) {
            try {
                // Generar archivo DOT
                DotGenerator dotGenerator = new DotGenerator();
                String dotFilePath = "temp_" + mapName.replaceAll("\\s+", "_") + ".dot";
                String pngFilePath = "temp_" + mapName.replaceAll("\\s+", "_") + ".png";
                
                dotGenerator.generateDotFile(selectedWorld, dotFilePath);
                
                // Ejecutar Graphviz para convertir DOT a PNG
                ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", dotFilePath, "-o", pngFilePath);
                Process process = pb.start();
                int exitCode = process.waitFor();
                
                if (exitCode == 0) {
                    // Cargar la imagen generada
                    File imageFile = new File(pngFilePath);
                    if (imageFile.exists()) {
                        ImageIcon icon = new ImageIcon(ImageIO.read(imageFile));
                        mapImages.put(mapName, icon);
                        
                        // Ajustar tamaño del panel de imagen
                        imagePanel.setPreferredSize(new Dimension(
                            icon.getIconWidth(), icon.getIconHeight()));
                        imagePanel.revalidate();
                        imagePanel.repaint();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al generar la imagen. Asegúrese de tener Graphviz instalado.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error al generar la imagen: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Genera los reportes de tokens y errores.
     */
    private void generateReports() {
        if (tokens.isEmpty() && errors.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay tokens o errores para generar reportes. Por favor analice un archivo primero.", 
                "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        ReportGenerator reportGenerator = new ReportGenerator();
        try {
            String tokensReport = reportGenerator.generateTokensReport(tokens);
            String errorsReport = reportGenerator.generateErrorsReport(errors);
            
            // Guardar reportes
            try (FileWriter tokensWriter = new FileWriter("tokens.html");
                 FileWriter errorsWriter = new FileWriter("errores.html")) {
                tokensWriter.write(tokensReport);
                errorsWriter.write(errorsReport);
            }
            
            JOptionPane.showMessageDialog(this, 
                "Reportes generados con éxito:\n- tokens.html\n- errores.html", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir reportes en el navegador
            Desktop.getDesktop().browse(new File("tokens.html").toURI());
            Desktop.getDesktop().browse(new File("errores.html").toURI());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error al generar reportes: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Muestra un diálogo con información sobre el autor.
     */
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Generador Visual de Mapas Narrativos\n" +
            "Proyecto 2 - Lenguajes Formales y de Programación\n\n" +
            "Desarrollado por: [Tu Nombre]\n" +
            "Carnet: [Tu Carnet]\n" +
            "Universidad de San Carlos de Guatemala\n" +
            "Facultad de Ingeniería\n" +
            "Primer Semestre 2025",
            "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
}