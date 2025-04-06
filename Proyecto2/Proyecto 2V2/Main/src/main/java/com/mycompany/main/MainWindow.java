
package com.mycompany.main;

/**
 *
 * @author iosea
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import javax.swing.Timer;


import javax.swing.JTextArea;

import javax.swing.JFrame;

/**
 * Ventana principal de la aplicación.
 */
public class MainWindow extends JFrame {
    
    private JTextArea textArea;
    private JPanel imagePanel;
    private JComboBox<String> mapSelector;
    private JButton loadButton, clearButton, analyzeButton, reportButton, aboutButton;
    private JSplitPane mainSplitPane;
    
    private Lexer lexer;
    private Parser parser;
    private ArrayList<World> worlds;
    private ArrayList<Token> tokens;
    private ArrayList<Token> errors;
    
    private Map<String, ImageIcon> mapImages;
    
    // Colores de la aplicación - Esquema moderno
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);      // Azul
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);    // Azul claro
    private final Color ACCENT_COLOR = new Color(231, 76, 60);        // Rojo
    private final Color BACKGROUND_COLOR = new Color(236, 240, 241);  // Gris claro
    private final Color TEXT_AREA_BG = new Color(255, 255, 255);      // Blanco
    private final Color TEXT_COLOR = new Color(44, 62, 80);           // Azul oscuro
    private final Color HOVER_COLOR = new Color(25, 111, 168);        // Azul más oscuro
    
    /**
     * Constructor de la ventana principal.
     */
    public MainWindow() {
        // Configuración básica de la ventana
        setTitle("Generador Visual de Mapas Narrativos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        try {
            // Intentar usar el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
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
        // Fuentes profesionales
        Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font codeFont = new Font("JetBrains Mono", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);

        // Área de texto con resaltado visual
        textArea = new JTextArea();
        textArea.setFont(codeFont);
        textArea.setBackground(TEXT_AREA_BG);
        textArea.setForeground(TEXT_COLOR);
        textArea.setCaretColor(PRIMARY_COLOR);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        
        // Borde moderno para el área de texto
        Border lineBorder = BorderFactory.createLineBorder(new Color(200, 200, 200));
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        textArea.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));

        // Panel de imagen con fondo estilizado
        imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Dibujar patrón de fondo
                g2d.setColor(new Color(245, 247, 250));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Dibujar imagen del mapa si existe
                String selectedMap = (String) mapSelector.getSelectedItem();
                if (selectedMap != null && mapImages.containsKey(selectedMap)) {
                    ImageIcon icon = mapImages.get(selectedMap);
                    if (icon != null) {
                        int x = (getWidth() - icon.getIconWidth()) / 2;
                        int y = (getHeight() - icon.getIconHeight()) / 2;
                        g2d.drawImage(icon.getImage(), x, y, this);
                    }
                } else {
                    // Dibujar mensaje cuando no hay imagen
                    g2d.setColor(new Color(150, 150, 150, 80));
                    g2d.setFont(new Font("Segoe UI", Font.ITALIC, 18));
                    String msg = "Seleccione un mapa para visualizar";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (getWidth() - fm.stringWidth(msg)) / 2;
                    int textY = getHeight() / 2;
                    g2d.drawString(msg, textX, textY);
                }
            }
        };
        imagePanel.setBackground(BACKGROUND_COLOR);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Selector de mapas mejorado visualmente
        mapSelector = new JComboBox<>();
        mapSelector.setFont(mainFont);
        mapSelector.setBackground(Color.WHITE);
        mapSelector.setForeground(TEXT_COLOR);
        ((JComponent) mapSelector.getRenderer()).setOpaque(true);
        mapSelector.setPreferredSize(new Dimension(200, 32));
        mapSelector.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(2, 8, 2, 8)
        ));

        // Botones estilizados
        loadButton = createStyledButton("Cargar Archivo", buttonFont, PRIMARY_COLOR);
        clearButton = createStyledButton("Limpiar Área", buttonFont, SECONDARY_COLOR);
        analyzeButton = createStyledButton("Analizar Archivo", buttonFont, PRIMARY_COLOR);
        reportButton = createStyledButton("Generar Reportes", buttonFont, SECONDARY_COLOR);
        aboutButton = createStyledButton("Acerca de", buttonFont, new Color(149, 165, 166));
        
        // Agregar iconos a los botones
        try {
            // Aquí deberías cargar los iconos. Como ejemplo, usaré null
            // En una implementación real, cargarías los iconos con ImageIO o similar
            loadButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
            clearButton.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
            analyzeButton.setIcon(UIManager.getIcon("Tree.leafIcon"));
            reportButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
            aboutButton.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea un botón estilizado con colores modernos y efectos hover.
     */
    private JButton createStyledButton(String text, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bordes redondeados
        button.putClientProperty("JButton.buttonType", "roundRect");
        
        // Efectos hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(darker(bgColor, 0.8f));
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(contains(e.getPoint(), button) ? HOVER_COLOR : bgColor);
            }
        });

        return button;
    }
    
    /**
     * Comprueba si un punto está dentro de un componente.
     */
    private boolean contains(Point p, Component c) {
        return new Rectangle(c.getSize()).contains(p);
    }
    
    /**
     * Oscurece un color por un factor dado.
     */
    private Color darker(Color c, float factor) {
        return new Color(Math.max((int)(c.getRed() * factor), 0),
                         Math.max((int)(c.getGreen() * factor), 0),
                         Math.max((int)(c.getBlue() * factor), 0),
                         c.getAlpha());
    }

    /**
     * Configura el layout de los componentes usando JSplitPane para permitir redimensionamiento.
     */
    private void layoutComponents() {
        // Panel principal con color de fondo
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Panel izquierdo (área de texto y botones)
        JPanel leftPanel = new JPanel(new BorderLayout(0, 10));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        
        // Título para el panel de código
        JLabel codeLabel = new JLabel("Código Fuente");
        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        codeLabel.setForeground(TEXT_COLOR);
        codeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Área de texto con scroll
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        
        // Panel de botones con estilo
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(loadButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(analyzeButton);
        
        // Agregar componentes al panel izquierdo
        leftPanel.add(codeLabel, BorderLayout.NORTH);
        leftPanel.add(scrollPane, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Panel derecho (visualización de mapas y controles)
        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        
        // Título para el panel de visualización
        JLabel visualLabel = new JLabel("Visualización de Mapas");
        visualLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        visualLabel.setForeground(TEXT_COLOR);
        visualLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Panel de imagen con scroll
        JScrollPane imageScrollPane = new JScrollPane(imagePanel);
        imageScrollPane.setBorder(null);
        imageScrollPane.setBackground(BACKGROUND_COLOR);
        
        // Panel superior derecho con selector y botones
        JPanel topRightPanel = new JPanel();
        topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topRightPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel selectLabel = new JLabel("Seleccionar mapa:");
        selectLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        selectLabel.setForeground(TEXT_COLOR);
        
        topRightPanel.add(reportButton);
        topRightPanel.add(selectLabel);
        topRightPanel.add(mapSelector);
        
        // Panel para la etiqueta y los controles
        JPanel headerRightPanel = new JPanel(new BorderLayout());
        headerRightPanel.setBackground(BACKGROUND_COLOR);
        headerRightPanel.add(visualLabel, BorderLayout.WEST);
        headerRightPanel.add(topRightPanel, BorderLayout.EAST);
        
        // Agregar componentes al panel derecho
        rightPanel.add(headerRightPanel, BorderLayout.NORTH);
        rightPanel.add(imageScrollPane, BorderLayout.CENTER);
        
        // Crear JSplitPane para permitir redimensionamiento
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        mainSplitPane.setDividerLocation(500);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setContinuousLayout(true);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setBorder(null);
        
        // Panel inferior para el botón "Acerca de"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.add(aboutButton);
        
        // Agregar componentes al panel principal
        mainPanel.add(mainSplitPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Agregar panel principal al frame
        setContentPane(mainPanel);
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
        
        // Teclas de acceso rápido para redimensionar paneles
        Action expandLeftAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainSplitPane.setDividerLocation(mainSplitPane.getDividerLocation() + 50);
            }
        };
        
        Action expandRightAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainSplitPane.setDividerLocation(mainSplitPane.getDividerLocation() - 50);
            }
        };
        
        // Vincula Ctrl+Left para expandir panel izquierdo
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "expandLeft");
        textArea.getActionMap().put("expandLeft", expandLeftAction);
        
        // Vincula Ctrl+Right para expandir panel derecho
        textArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "expandRight");
        textArea.getActionMap().put("expandRight", expandRightAction);
        
        // Añade menú contextual para redimensionar paneles
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem expandLeftItem = new JMenuItem("Expandir panel izquierdo");
        JMenuItem expandRightItem = new JMenuItem("Expandir panel derecho");
        JMenuItem resetSplitItem = new JMenuItem("Restablecer división");
        
        expandLeftItem.addActionListener(e -> mainSplitPane.setDividerLocation(mainSplitPane.getDividerLocation() + 100));
        expandRightItem.addActionListener(e -> mainSplitPane.setDividerLocation(mainSplitPane.getDividerLocation() - 100));
        resetSplitItem.addActionListener(e -> mainSplitPane.setDividerLocation(0.5));
        
        popupMenu.add(expandLeftItem);
        popupMenu.add(expandRightItem);
        popupMenu.add(resetSplitItem);
        
        // Añadir menú contextual al divisor
        mainSplitPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && isNearDivider(e.getPoint())) {
                    popupMenu.show(mainSplitPane, e.getX(), e.getY());
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && isNearDivider(e.getPoint())) {
                    popupMenu.show(mainSplitPane, e.getX(), e.getY());
                }
            }
        });
    }
    
    /**
     * Comprueba si el punto está cerca del divisor del JSplitPane.
     */
    private boolean isNearDivider(Point p) {
        int divLoc = mainSplitPane.getDividerLocation();
        int divSize = mainSplitPane.getDividerSize();
        return Math.abs(p.x - divLoc) <= divSize;
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
                
                // Mostrar notificación de éxito
                showNotification("Archivo cargado correctamente: " + selectedFile.getName(), false);
            } catch (IOException ex) {
                showNotification("Error al cargar el archivo: " + ex.getMessage(), true);
            }
        }
    }
    
    /**
     * Analiza el contenido del área de texto.
     */
    private void analyzeFile() {
        String input = textArea.getText();
        if (input.trim().isEmpty()) {
            showNotification("El área de texto está vacía. Por favor cargue un archivo.", true);
            return;
        }
        
        // Mostrar indicador de carga
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
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
            showNotification("No se encontraron mapas válidos en el archivo.", true);
        } else {
            for (World world : worlds) {
                mapSelector.addItem(world.getName());
            }
            
            // Generar imagen para el primer mapa
            if (mapSelector.getItemCount() > 0) {
                mapSelector.setSelectedIndex(0);
                String selectedMap = (String) mapSelector.getSelectedItem();
                generateMapImage(selectedMap);
                
                showNotification("Análisis completado. Se encontraron " + worlds.size() + " mapas.", false);
            }
        }
        
        // Restaurar cursor
        setCursor(Cursor.getDefaultCursor());
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
                // Mostrar indicador de carga
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                
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
                        
                        showNotification("Mapa generado correctamente: " + mapName, false);
                    }
                } else {
                    showNotification("Error al generar la imagen. Asegúrese de tener Graphviz instalado.", true);
                }
                
                // Restaurar cursor
                setCursor(Cursor.getDefaultCursor());
            } catch (Exception ex) {
                showNotification("Error al generar la imagen: " + ex.getMessage(), true);
                setCursor(Cursor.getDefaultCursor());
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Genera los reportes de tokens y errores.
     */
    private void generateReports() {
        if (tokens.isEmpty() && errors.isEmpty()) {
            showNotification("No hay tokens o errores para generar reportes. Por favor analice un archivo primero.", true);
            return;
        }
        
        // Mostrar indicador de carga
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
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
            
            showNotification("Reportes generados con éxito: tokens.html, errores.html", false);
            
            // Abrir reportes en el navegador
            Desktop.getDesktop().browse(new File("tokens.html").toURI());
            Desktop.getDesktop().browse(new File("errores.html").toURI());
        } catch (IOException ex) {
            showNotification("Error al generar reportes: " + ex.getMessage(), true);
        }
        
        // Restaurar cursor
        setCursor(Cursor.getDefaultCursor());
    }
    
    /**
     * Muestra un diálogo con información sobre el autor.
     */
    /**
 * Muestra un diálogo con información sobre el autor y la aplicación.
 */
private void showAboutDialog() {
    // Panel principal con degradado de fondo
    JPanel aboutPanel = new JPanel(new BorderLayout(20, 20)) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Crear un degradado suave como fondo
            int w = getWidth();
            int h = getHeight();
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(255, 255, 255),
                0, h, new Color(240, 244, 248)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    };
    aboutPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
    
    // Panel superior con logo y título
    JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
    headerPanel.setOpaque(false);
    
    // Logo personalizado más elaborado
    JPanel logoPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Fondo del logo con sombra
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillOval(3, 3, 84, 84);
            
            // Gradiente para el logo
            RadialGradientPaint paint = new RadialGradientPaint(
                new Point(42, 42),
                42,
                new float[]{0.0f, 1.0f},
                new Color[]{PRIMARY_COLOR, darker(PRIMARY_COLOR, 0.7f)}
            );
            g2d.setPaint(paint);
            g2d.fillOval(0, 0, 84, 84);
            
            // Borde
            g2d.setColor(new Color(255, 255, 255, 120));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(0, 0, 84, 84);
            
            // Texto con efecto 3D
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 32));
            FontMetrics fm = g2d.getFontMetrics();
            String text = "GV";
            int textWidth = fm.stringWidth(text);
            
            // Sombra de texto
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.drawString(text, (84 - textWidth) / 2 + 1, 48 + 1);
            
            // Texto principal
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, (84 - textWidth) / 2, 48);
        }
    };
    logoPanel.setPreferredSize(new Dimension(85, 85));
    logoPanel.setOpaque(false);
    
    // Panel para título y versión
    JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
    titlePanel.setOpaque(false);
    
    JLabel titleLabel = new JLabel("Generador Visual de Mapas Narrativos");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    titleLabel.setForeground(new Color(44, 62, 80));
    
    JLabel versionLabel = new JLabel("Versión 1.0.0");
    versionLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    versionLabel.setForeground(new Color(127, 140, 141));
    
    titlePanel.add(titleLabel);
    titlePanel.add(versionLabel);
    
    headerPanel.add(logoPanel, BorderLayout.WEST);
    headerPanel.add(titlePanel, BorderLayout.CENTER);
    
    // Separador personalizado con degradado
    JPanel separatorPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), 0),
                w/2, 0, PRIMARY_COLOR,
                true
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, 2);
        }
    };
    separatorPanel.setPreferredSize(new Dimension(0, 2));
    separatorPanel.setOpaque(false);
    
    // Panel de contenido con información sobre el autor y la aplicación
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
    contentPanel.setOpaque(false);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    
    // Información del proyecto con estilos personalizados
    String[] infoLabels = {
        "Proyecto:", 
        "Desarrollador:", 
        "Carnet:", 
        "Universidad:", 
        "Facultad:", 
        "Período:"
    };
    
    String[] infoValues = {
        "Proyecto 2 - Lenguajes Formales y de Programación",
        "José Alexander López López",
        "202100305",
        "Universidad de San Carlos de Guatemala",
        "Facultad de Ingeniería",
        "Primer Semestre 2025"
    };
    
    for (int i = 0; i < infoLabels.length; i++) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        rowPanel.setOpaque(false);
        
        JLabel label = new JLabel(infoLabels[i]);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        label.setPreferredSize(new Dimension(120, 25));
        
        JLabel value = new JLabel(infoValues[i]);
        value.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        value.setForeground(new Color(70, 70, 70));
        
        rowPanel.add(label);
        rowPanel.add(value);
        contentPanel.add(rowPanel);
    }
    
    // Panel con descripción adicional
    JPanel descPanel = new JPanel();
    descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
    descPanel.setOpaque(false);
    descPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    
    JLabel descTitle = new JLabel("Acerca de la aplicación");
    descTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
    descTitle.setForeground(PRIMARY_COLOR);
    descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JTextArea descText = new JTextArea(
        "Esta aplicación permite generar visualizaciones gráficas de mapas " +
        "narrativos a partir de descripciones textuales. Permite analizar " +
        "la estructura léxica y sintáctica de los archivos ingresados y " +
        "transformarlos en representaciones visuales mediante Graphviz."
    );
    descText.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    descText.setForeground(new Color(70, 70, 70));
    descText.setLineWrap(true);
    descText.setWrapStyleWord(true);
    descText.setEditable(false);
    descText.setBackground(new Color(0, 0, 0, 0));
    descText.setBorder(null);
    descText.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    descPanel.add(descTitle);
    descPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    descPanel.add(descText);
    
    // Botones de acción
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setOpaque(false);
    
    // Botón de sitio web
    JButton websiteButton = createStyledButton("Sitio Web", new Font("Segoe UI", Font.PLAIN, 13), SECONDARY_COLOR);
    websiteButton.setPreferredSize(new Dimension(120, 35));
    websiteButton.setIcon(UIManager.getIcon("FileView.computerIcon"));
    
    // Botón de cerrar
    JButton closeButton = createStyledButton("Cerrar", new Font("Segoe UI", Font.BOLD, 13), PRIMARY_COLOR);
    closeButton.setPreferredSize(new Dimension(120, 35));
    
    buttonPanel.add(websiteButton);
    buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
    buttonPanel.add(closeButton);
    
    // Ensamblar todos los paneles
    aboutPanel.add(headerPanel, BorderLayout.NORTH);
    aboutPanel.add(separatorPanel, BorderLayout.CENTER);
    
    JPanel southPanel = new JPanel(new BorderLayout());
    southPanel.setOpaque(false);
    southPanel.add(contentPanel, BorderLayout.NORTH);
    southPanel.add(descPanel, BorderLayout.CENTER);
    southPanel.add(buttonPanel, BorderLayout.SOUTH);
    
    aboutPanel.add(southPanel, BorderLayout.SOUTH);
    
    // Crear diálogo sin animación
    final JDialog aboutDialog = new JDialog(this, "Acerca de", true);
    aboutDialog.setContentPane(aboutPanel);
    aboutDialog.pack();
    aboutDialog.setSize(new Dimension(550, Math.max(aboutDialog.getHeight(), 400)));
    aboutDialog.setResizable(false);
    aboutDialog.setLocationRelativeTo(this);
    
    // Manejar eventos de botones
    websiteButton.addActionListener(e -> {
        try {
            Desktop.getDesktop().browse(new URI("https://www.usac.edu.gt/"));
        } catch (Exception ex) {
            showNotification("No se pudo abrir el sitio web", true);
        }
    });
    
    closeButton.addActionListener(e -> {
        aboutDialog.dispose();
    });
    
    // Mostrar diálogo sin animación
    aboutDialog.setVisible(true);
}
    
    /**
     * Muestra una notificación temporal en la parte inferior de la ventana.
     */
    private void showNotification(String message, boolean isError) {
        // Panel para la notificación
        JPanel notificationPanel = new JPanel(new BorderLayout(10, 0));
        notificationPanel.setBackground(isError ? new Color(231, 76, 60) : new Color(46, 204, 113));
        notificationPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Mensaje
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(Color.WHITE);
        
        // Botón de cerrar
        JLabel closeLabel = new JLabel("×");
        closeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        closeLabel.setForeground(Color.WHITE);
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        notificationPanel.add(messageLabel, BorderLayout.CENTER);
        notificationPanel.add(closeLabel, BorderLayout.EAST);
        
        // Agregar a la ventana en la parte inferior
        JPanel mainPanel = (JPanel) getContentPane();
        mainPanel.add(notificationPanel, BorderLayout.NORTH);
        
        // Timer for auto-dismissal
Timer timer = new Timer(5000, e -> {
    mainPanel.remove(notificationPanel);
    mainPanel.revalidate();
    mainPanel.repaint();
});
timer.setRepeats(false);
timer.start();

// Close button event handler
closeLabel.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        mainPanel.remove(notificationPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        timer.stop();
    }
});

mainPanel.revalidate();
mainPanel.repaint();
        

}

}
