package com.mycompany.adf_graph12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ADF_GRAPH12 extends JFrame {

    private HashMap<String, Automata> automatas = new HashMap<>();
    private ArrayList<String> erroresLexicos = new ArrayList<>();
    private JLabel imagenLabel = new JLabel();
    private JTextArea archivoArea = new JTextArea();

    public ADF_GRAPH12() {
    // Configuracion  de la ventana
    setTitle("AFDGraph - Visualizador de Autómatas");
    setSize(1200, 700);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null); // Centrar ventana
    
    try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    //  panel con margen
    JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
    panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panelPrincipal.setBackground(new Color(240, 240, 245));
    
    // Panel de presentadncion
    JPanel panelTitulo = new JPanel(new BorderLayout());
    panelTitulo.setBackground(new Color(60, 63, 65));
    panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    
    JLabel tituloLabel = new JLabel("AFDGraph - Visualizador de Autómatas Finitos Deterministas");
    tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    tituloLabel.setForeground(Color.WHITE);
    
    JLabel descripcionLabel = new JLabel("Carga, visualiza y analiza autómatas finitos deterministas desde archivos de texto");
    descripcionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    descripcionLabel.setForeground(new Color(200, 200, 200));
    
    JPanel labelPanel = new JPanel(new GridLayout(2, 1, 0, 5));
    labelPanel.setBackground(new Color(60, 63, 65));
    labelPanel.add(tituloLabel);
    labelPanel.add(descripcionLabel);
    panelTitulo.add(labelPanel, BorderLayout.WEST);
    
    // botones y diseño
    JPanel panelBotones = new JPanel();
    panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.X_AXIS));
    panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
    panelBotones.setBackground(new Color(240, 240, 245));
    
    // Estilo de botones
    Font btnFont = new Font("Segoe UI", Font.BOLD, 14);
    Dimension btnSize = new Dimension(180, 40);
    
    //  Analizar
    JButton analizarBtn = new JButton("Analizar Archivo");
    analizarBtn.setFont(btnFont);
    analizarBtn.setPreferredSize(btnSize);
    analizarBtn.setBackground(new Color(66, 139, 202));
    analizarBtn.setForeground(Color.BLACK);
    analizarBtn.setFocusPainted(false);
    analizarBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(46, 109, 164), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    
    // ComboBox 
    JComboBox<String> selectAutomata = new JComboBox<>();
    selectAutomata.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    selectAutomata.setPreferredSize(new Dimension(250, 40));
    selectAutomata.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    selectAutomata.setBackground(Color.BLACK);
    
    //  Graficar
    JButton graficarBtn = new JButton("Graficar Autómata");
    graficarBtn.setFont(btnFont);
    graficarBtn.setPreferredSize(btnSize);
    graficarBtn.setBackground(new Color(92, 184, 92));
    graficarBtn.setForeground(Color.BLACK);
    graficarBtn.setFocusPainted(false);
    graficarBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(72, 164, 72), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    
    //  Reportes
    JButton reportesBtn = new JButton("Generar Reportes");
    reportesBtn.setFont(btnFont);
    reportesBtn.setPreferredSize(btnSize);
    reportesBtn.setBackground(new Color(240, 173, 78));
    reportesBtn.setForeground(Color.BLACK);
    reportesBtn.setFocusPainted(false);
    reportesBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 153, 58), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    
    //  panel de botones
    panelBotones.add(analizarBtn);
    panelBotones.add(Box.createRigidArea(new Dimension(15, 0)));
    panelBotones.add(reportesBtn);
    panelBotones.add(Box.createRigidArea(new Dimension(15, 0)));
    panelBotones.add(selectAutomata);
    panelBotones.add(Box.createRigidArea(new Dimension(15, 0)));
    panelBotones.add(graficarBtn);
    
    // panel de contenido
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setBorder(null);
    splitPane.setDividerSize(5);
    splitPane.setDividerLocation(500);
    splitPane.setContinuousLayout(true);
    
    // Panel izquierdo, área de texto
    JPanel panelArchivo = new JPanel(new BorderLayout(0, 10));
    panelArchivo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    panelArchivo.setBackground(new Color(240, 240, 245));
    
    JLabel archivoLabel = new JLabel("Contenido del Archivo");
    archivoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    archivoLabel.setForeground(new Color(60, 63, 65));
    
    archivoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
    archivoArea.setEditable(false);
    archivoArea.setBackground(Color.WHITE);
    archivoArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    
    JScrollPane scrollArchivo = new JScrollPane(archivoArea);
    scrollArchivo.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    
    panelArchivo.add(archivoLabel, BorderLayout.NORTH);
    panelArchivo.add(scrollArchivo, BorderLayout.CENTER);
    
    // Panel derecho, imagen del autómata
    JPanel panelGrafica = new JPanel(new BorderLayout(0, 10));
    panelGrafica.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
    panelGrafica.setBackground(new Color(240, 240, 245));
    
    JLabel graficaLabel = new JLabel("Visualización del Autómata");
    graficaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
    graficaLabel.setForeground(new Color(60, 63, 65));
    
    imagenLabel.setBackground(Color.WHITE);
    imagenLabel.setHorizontalAlignment(JLabel.CENTER);
    imagenLabel.setVerticalAlignment(JLabel.CENTER);
    
    JScrollPane scrollGrafica = new JScrollPane(imagenLabel);
    scrollGrafica.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
    scrollGrafica.setBackground(Color.WHITE);
    
    panelGrafica.add(graficaLabel, BorderLayout.NORTH);
    panelGrafica.add(scrollGrafica, BorderLayout.CENTER);
    
    // split pane
    splitPane.setLeftComponent(panelArchivo);
    splitPane.setRightComponent(panelGrafica);
    
    // Barra de estado
    JPanel statusBar = new JPanel(new BorderLayout());
    statusBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    statusBar.setBackground(new Color(248, 248, 250));
    
    JLabel statusLabel = new JLabel("Listo para comenzar. Seleccione un archivo para analizar.");
    statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    
    JLabel versionLabel = new JLabel("AFDGraph v1.0");
    versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    versionLabel.setForeground(new Color(150, 150, 150));
    
    statusBar.add(statusLabel, BorderLayout.WEST);
    statusBar.add(versionLabel, BorderLayout.EAST);
    
    // event listener
    analizarBtn.addActionListener(e -> {
        analizarArchivo(selectAutomata);
        statusLabel.setText("Archivo analizado. " + automatas.size() + " autómatas cargados. " + 
                            erroresLexicos.size() + " errores encontrados.");
    });
    
    graficarBtn.addActionListener(e -> {
        String automata = (String) selectAutomata.getSelectedItem();
        if (automata != null) {
            graficarAutomata(automata);
            statusLabel.setText("Autómata '" + automata + "' graficado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un autómata primero.", 
                                         "Error", JOptionPane.WARNING_MESSAGE);
        }
    });
    
    reportesBtn.addActionListener(e -> {
        generarReportes();
        statusLabel.setText("Reportes generados correctamente.");
    });
    
    // panel principal
    panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
    panelPrincipal.add(panelBotones, BorderLayout.NORTH);
    panelPrincipal.add(splitPane, BorderLayout.CENTER);
    panelPrincipal.add(statusBar, BorderLayout.SOUTH);
    
    // hover para botones
    MouseAdapter btnHoverEffect = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton btn = (JButton) e.getSource();
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(btn.getBackground().darker(), 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            JButton btn = (JButton) e.getSource();
            Color borderColor = btn.getBackground().darker();
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(borderColor, 1),
                    BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        }
    };
    
    analizarBtn.addMouseListener(btnHoverEffect);
    graficarBtn.addMouseListener(btnHoverEffect);
    reportesBtn.addMouseListener(btnHoverEffect);
    
    // Configura la ventana principal
    add(panelPrincipal);
    
    // icono
    try {
    setIconImage(new ImageIcon(new File("C:\\Users\\iosea\\OneDrive\\Escritorio\\ALEX\\LENGUAJES FORMALES Y DE PROGRAMACIÓN\\LAB LFP\\[LFP]202100305\\-LFP-202100305-\\Proyecto1\\ADF_GRAPH12\\src\\main\\java\\com\\mycompany\\adf_graph12\\icon.png").getAbsolutePath()).getImage());
} catch (Exception e) {
    e.printStackTrace();
}
    
    // Empaquetar y mostrar
    pack();
    setMinimumSize(new Dimension(1000, 600));
    setVisible(true);
}
private void analizarArchivo(JComboBox<String> selectAutomata) {
    JFileChooser fileChooser = new JFileChooser();
    
    // Filter for .lfp files
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Archivos LFP (*.lfp)", "lfp");
    fileChooser.setFileFilter(filter);
    
    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        archivoArea.setText("");
        erroresLexicos.clear(); // Clear previous errors
        automatas.clear(); // Clear previous automata
        selectAutomata.removeAllItems(); // Clear ComboBox
        
        try (FileInputStream fis = new FileInputStream(file)) {
            // Variables for file content display
            StringBuilder fileContent = new StringBuilder();
            
            // Variables for lexical analysis
            int c;
            int estado = 0;
            int lineaActual = 1;
            int charActual = 1;
            StringBuilder lexema = new StringBuilder();
            
            // Variables to store automata information
            Automata automataActual = null;
            String seccionActual = "";
            String estadoOrigen = "";
            String simboloTransicion = "";
            String estadoDestino = "";
            boolean dentroComillas = false;
            boolean dentroCorchetes = false;
            boolean dentroParentesis = false;
            boolean dentroTransiciones = false;
            int llaves = 0;
            ArrayList<String> valoresTemp = new ArrayList<>();
            
            // Process file character by character
            while ((c = fis.read()) != -1) {
                char ch = (char) c;
                fileContent.append(ch);
                
                // Process character according to current state
                switch (estado) {
                    case 0: // Initial state
                        if (Character.isLetter(ch)) {
                            lexema.append(ch);
                            estado = 1; // Possible identifier or keyword
                        } else if (ch == ':') {
                            // Process colon
                            if (lexema.length() > 0) {
                                String token = lexema.toString().trim();
                                
                                // Check if it's a new automaton
                                if (!token.equals("descripcion") && !token.equals("estados") && 
                                    !token.equals("alfabeto") && !token.equals("inicial") && 
                                    !token.equals("finales") && !token.equals("transiciones")) {
                                    
                                    // Verify automaton name doesn't start with numbers
                                    if (Character.isDigit(token.charAt(0))) {
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + (charActual - token.length()) + 
                                            ": El nombre del autómata '" + token + 
                                            "' no debe comenzar con un número");
                                    }
                                    
                                    automataActual = new Automata(token);
                                    automatas.put(token, automataActual);
                                    selectAutomata.addItem(token);
                                    seccionActual = "";
                                } else {
                                    // It's a section keyword
                                    seccionActual = token;
                                }
                            }
                            lexema.setLength(0);
                            estado = 2; // After colon
                        } else if (ch == '=') {
                            if (lexema.length() > 0 && dentroTransiciones) {
                                estadoOrigen = lexema.toString().trim();
                                // Verify the origin state exists
                                if (automataActual != null && !automataActual.estados.contains(estadoOrigen)) {
                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                        " carácter " + (charActual - estadoOrigen.length()) + 
                                        ": Estado origen '" + estadoOrigen + "' no definido");
                                }
                            }
                            lexema.setLength(0);
                            estado = 8; // Transition definition
                        } else if (ch == '{') {
                            llaves++;
                            if (seccionActual.equals("transiciones")) {
                                dentroTransiciones = true;
                            }
                            lexema.setLength(0);
                        } else if (ch == '}') {
                            llaves--;
                            if (llaves == 0 && dentroTransiciones) {
                                dentroTransiciones = false;
                            }
                            if (llaves == 0) {
                                automataActual = null; // End of automaton
                            }
                            lexema.setLength(0);
                        } else if (ch == '[') {
                            dentroCorchetes = true;
                            valoresTemp.clear();
                            lexema.setLength(0);
                            estado = 3; // Inside brackets
                        } else if (ch == '"') {
                            dentroComillas = true;
                            lexema.setLength(0);
                            estado = 4; // Inside quotes
                        } else if (ch == '(') {
                            dentroParentesis = true;
                            lexema.setLength(0);
                            estado = 9; // Inside parentheses for transitions
                        } else if (ch == ',') {
                            // Separator, ignore
                            lexema.setLength(0);
                        } else if (ch == '\n') {
                            lineaActual++;
                            charActual = 0;
                            lexema.setLength(0);
                        } else if (!Character.isWhitespace(ch)) {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 1: // Reading identifier or keyword
                        if (Character.isLetterOrDigit(ch) || ch == '_') {
                            lexema.append(ch);
                        } else {
                            // Reprocess current character in state 0
                            estado = 0;
                            continue;
                        }
                        break;
                        
                    case 2: // After colon
                        if (ch == '"') {
                            dentroComillas = true;
                            lexema.setLength(0);
                            estado = 4; // Inside quotes
                        } else if (ch == '[') {
                            dentroCorchetes = true;
                            valoresTemp.clear();
                            lexema.setLength(0);
                            estado = 3; // Inside brackets
                        } else if (ch == '{') {
                            llaves++;
                            if (seccionActual.equals("transiciones")) {
                                dentroTransiciones = true;
                            }
                            estado = 0;
                        } else if (!Character.isWhitespace(ch)) {
                            // For single values like 'inicial: q0'
                            lexema.append(ch);
                            estado = 5; // Reading single value
                        }
                        break;
                        
                    case 3: // Inside brackets
                        if (ch == '"') {
                            dentroComillas = true;
                            lexema.setLength(0);
                            estado = 6; // Quote inside brackets
                        } else if (ch == ']') {
                            dentroCorchetes = false;
                            
                            // Process collected values according to current section
                            if (seccionActual.equals("estados") && automataActual != null) {
                                automataActual.estados.addAll(valoresTemp);
                            } else if (seccionActual.equals("alfabeto") && automataActual != null) {
                                automataActual.alfabeto.addAll(valoresTemp);
                            } else if (seccionActual.equals("finales") && automataActual != null) {
                                automataActual.estadosFinales.addAll(valoresTemp);
                            }
                            
                            valoresTemp.clear();
                            estado = 0;
                        } else if (ch == ',') {
                            if (lexema.length() > 0) {
                                valoresTemp.add(lexema.toString().trim());
                                lexema.setLength(0);
                            }
                        } else if (!Character.isWhitespace(ch)) {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 4: // Inside quotes (for description)
                        if (ch == '"') {
                            dentroComillas = false;
                            
                            if (seccionActual.equals("descripcion") && automataActual != null) {
                                automataActual.descripcion = lexema.toString();
                            }
                            
                            lexema.setLength(0);
                            estado = 0;
                        } else {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 5: // Reading single value (for inicial)
                        if (ch == ',' || ch == '\n') {
                            if (seccionActual.equals("inicial") && automataActual != null) {
                                automataActual.estadoInicial = lexema.toString().trim();
                            }
                            
                            lexema.setLength(0);
                            estado = 0;
                            
                            if (ch == '\n') {
                                lineaActual++;
                                charActual = 0;
                            }
                        } else if (!Character.isWhitespace(ch)) {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 6: // Quote inside brackets (for alfabeto)
                        if (ch == '"') {
                            dentroComillas = false;
                            valoresTemp.add(lexema.toString());
                            lexema.setLength(0);
                            estado = 7; // After quote in brackets
                        } else {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 7: // After quote in brackets
                        if (ch == ',') {
                            estado = 3; // Back to inside brackets
                        } else if (ch == ']') {
                            dentroCorchetes = false;
                            
                            // Process collected values according to current section
                            if (seccionActual.equals("estados") && automataActual != null) {
                                automataActual.estados.addAll(valoresTemp);
                            } else if (seccionActual.equals("alfabeto") && automataActual != null) {
                                automataActual.alfabeto.addAll(valoresTemp);
                            } else if (seccionActual.equals("finales") && automataActual != null) {
                                automataActual.estadosFinales.addAll(valoresTemp);
                            }
                            
                            valoresTemp.clear();
                            estado = 0;
                        }
                        break;
                        
                    case 8: // Transition definition (after =)
                        if (ch == '(') {
                            dentroParentesis = true;
                            lexema.setLength(0);
                            estado = 9; // Inside parentheses for transitions
                        } else if (!Character.isWhitespace(ch)) {
                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                " carácter " + charActual + 
                                ": Se esperaba '(' después de '='");
                            estado = 0;
                        }
                        break;
                        
                    case 9: // Inside parenthesis for transitions
                        if (ch == '"') {
                            dentroComillas = true;
                            lexema.setLength(0);
                            estado = 10; // Reading symbol in transitions
                        } else if (ch == ')') {
                            dentroParentesis = false;
                            estado = 0;
                        } else if (!Character.isWhitespace(ch) && ch != ',') {
                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                " carácter " + charActual + 
                                ": Se esperaba símbolo entre comillas");
                        }
                        break;
                        
                    case 10: // Reading symbol in transitions
                        if (ch == '"') {
                            dentroComillas = false;
                            simboloTransicion = lexema.toString();
                            lexema.setLength(0);
                            estado = 11; // After symbol
                        } else {
                            lexema.append(ch);
                        }
                        break;
                        
                    case 11: // After symbol, expecting ->
                        if (ch == '-') {
                            estado = 12; // First char of arrow
                        } else if (!Character.isWhitespace(ch)) {
                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                " carácter " + charActual + 
                                ": Se esperaba '->' después del símbolo");
                            estado = 9; // Back to inside parentheses
                        }
                        break;
                        
                    case 12: // Arrow first char
                        if (ch == '>') {
                            estado = 13; // After arrow, expecting destination state
                        } else {
                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                " carácter " + charActual + 
                                ": Flecha mal formada, se esperaba '>'");
                            estado = 9; // Back to inside parentheses
                        }
                        break;
                        
                    case 13: // After arrow, reading destination state
                        if (ch == ',' || ch == ')') {
                            estadoDestino = lexema.toString().trim();
                            
                            // Verify the destination state exists
                            if (automataActual != null && !automataActual.estados.contains(estadoDestino)) {
                                erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                    " carácter " + (charActual - estadoDestino.length()) + 
                                    ": Estado destino '" + estadoDestino + "' no definido");
                            }
                            
                            // Verify the symbol is in the alphabet
                            if (automataActual != null && !automataActual.alfabeto.contains(simboloTransicion)) {
                                erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                    " carácter " + (charActual - estadoDestino.length() - 2 - simboloTransicion.length()) + 
                                    ": Símbolo '" + simboloTransicion + "' no definido en el alfabeto");
                            }
                            
                            // Add transition
                            if (automataActual != null) {
                                HashMap<String, String> transiciones = automataActual.transiciones.getOrDefault(estadoOrigen, new HashMap<>());
                                transiciones.put(simboloTransicion, estadoDestino);
                                automataActual.transiciones.put(estadoOrigen, transiciones);
                            }
                            
                            lexema.setLength(0);
                            
                            if (ch == ',') {
                                estado = 9; // Back to inside parentheses
                            } else if (ch == ')') {
                                dentroParentesis = false;
                                estado = 0;
                            }
                        } else if (!Character.isWhitespace(ch)) {
                            lexema.append(ch);
                        }
                        break;
                }
                
                charActual++;
            }
            
            // Show content in the text area
            archivoArea.setText(fileContent.toString());
            
            // Validate automata completeness
            for (Map.Entry<String, Automata> entry : automatas.entrySet()) {
                Automata aut = entry.getValue();
                if (aut.descripcion == null || aut.descripcion.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": Falta descripción");
                }
                if (aut.estados.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": No se definieron estados");
                }
                if (aut.alfabeto.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": No se definió alfabeto");
                }
                if (aut.estadoInicial == null || aut.estadoInicial.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": No se definió estado inicial");
                }
                if (aut.estadosFinales.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": No se definieron estados finales");
                }
                if (aut.transiciones.isEmpty()) {
                    erroresLexicos.add("Error en autómata " + entry.getKey() + ": No se definieron transiciones");
                }
            }
            
            if (erroresLexicos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Archivo analizado y cargado completamente sin errores léxicos.");
            } else {
                JOptionPane.showMessageDialog(this, "Archivo analizado con " + erroresLexicos.size() + 
                    " errores léxicos. Revise el reporte de errores.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage());
        }
    }
    // Verificar autómatas cargados
if (automatas.isEmpty()) {
    JOptionPane.showMessageDialog(this, "No se detectaron autómatas en el archivo.");
} else {
    System.out.println("Autómatas cargados: " + automatas.size());
    for (String nombre : automatas.keySet()) {
        System.out.println("- " + nombre);
    }
    
    // Forzar actualización del ComboBox
    selectAutomata.revalidate();
    selectAutomata.repaint();
}
}
   private void graficarAutomata(String nombreAutomata) {
        Automata automata = automatas.get(nombreAutomata);
        if (automata != null) {
            try {
                File dotFile = new File("automata.dot");
                PrintWriter pw = new PrintWriter(dotFile);
                pw.println("digraph Automata {");
                pw.println("rankdir=LR;");
                pw.println("node [shape = doublecircle]; " + String.join(" ", automata.estadosFinales) + ";");
                pw.println("node [shape = circle];");

                automata.transiciones.forEach((origen, mapa) -> mapa.forEach((simb, destino) -> pw.println(origen + " -> " + destino + " [label=\"" + simb + "\"]")));
                pw.println("}");
                pw.close();

                Runtime.getRuntime().exec("dot -Tpng automata.dot -o automata.png").waitFor();
                BufferedImage img = ImageIO.read(new File("automata.png"));
                imagenLabel.setIcon(new ImageIcon(img));
                imagenLabel.revalidate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al graficar el autómata: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona un autómata válido.");
        }
    }


private void generarReportes() {
    try (PrintWriter tokensReport = new PrintWriter("reporte_tokens.html");
         PrintWriter erroresReport = new PrintWriter("reporte_errores.html")) {

        String estilo = "<style>" +
                "body{font-family:Arial,sans-serif;margin:20px;background-color:#f8f9fa;}" +
                "h1{color:#343a40;}" +
                ".automata{border:1px solid #dee2e6;padding:10px;margin-bottom:20px;border-radius:5px;background-color:#fff;box-shadow:0 2px 4px rgba(0,0,0,0.1);}" +
                "ul{background-color:#fff;padding:15px;border-radius:5px;box-shadow:0 2px 4px rgba(0,0,0,0.1);} li{margin-bottom:8px;}" +
                "table{width:100%;border-collapse:collapse;margin-bottom:20px;}" +
                "th,td{border:1px solid #dee2e6;padding:8px;text-align:left;}" +
                "th{background-color:#e9ecef;}" +
                "</style>";

        tokensReport.println("<html><head><meta charset='UTF-8'><title>Reporte de Tokens</title>" + estilo + "</head><body>");
        tokensReport.println("<h1>Reporte de Tokens</h1>");
        
        // Si no hay autómatas, mostrar mensaje
        if (automatas.isEmpty()) {
            tokensReport.println("<p>No hay autómatas cargados.</p>");
        } else {
            automatas.forEach((nombre, automata) -> {
                tokensReport.println("<div class='automata'>");
                tokensReport.println("<h2>" + nombre + "</h2>");
                tokensReport.println("<p><strong>Descripción:</strong> " + automata.descripcion + "</p>");
                
                // Tabla de estados
                tokensReport.println("<h3>Estados</h3>");
                tokensReport.println("<ul>");
                for (String estado : automata.estados) {
                    String tipo = "";
                    if (estado.equals(automata.estadoInicial)) {
                        tipo = " (inicial)";
                    }
                    if (automata.estadosFinales.contains(estado)) {
                        tipo += " (final)";
                    }
                    tokensReport.println("<li>" + estado + tipo + "</li>");
                }
                tokensReport.println("</ul>");
                
                // Tabla de alfabeto
                tokensReport.println("<h3>Alfabeto</h3>");
                tokensReport.println("<ul>");
                for (String simbolo : automata.alfabeto) {
                    tokensReport.println("<li>\"" + simbolo + "\"</li>");
                }
                tokensReport.println("</ul>");
                
                // Tabla de transiciones
                tokensReport.println("<h3>Tabla de Transiciones</h3>");
                tokensReport.println("<table>");
                tokensReport.println("<tr><th>Estado</th>");
                for (String simbolo : automata.alfabeto) {
                    tokensReport.println("<th>\"" + simbolo + "\"</th>");
                }
                tokensReport.println("</tr>");
                
                for (String estado : automata.estados) {
                    tokensReport.println("<tr>");
                    tokensReport.println("<td>" + estado + "</td>");
                    
                    HashMap<String, String> transEstado = automata.transiciones.get(estado);
                    if (transEstado != null) {
                        for (String simbolo : automata.alfabeto) {
                            String destino = transEstado.get(simbolo);
                            tokensReport.println("<td>" + (destino != null ? destino : "-") + "</td>");
                        }
                    } else {
                        for (int i = 0; i < automata.alfabeto.size(); i++) {
                            tokensReport.println("<td>-</td>");
                        }
                    }
                    
                    tokensReport.println("</tr>");
                }
                tokensReport.println("</table>");
                
                tokensReport.println("</div>");
            });
        }
        tokensReport.println("</body></html>");

        // Reporte de errores léxicos
        erroresReport.println("<html><head><meta charset='UTF-8'><title>Reporte de Errores Léxicos</title>" + estilo + "</head><body>");
        erroresReport.println("<h1>Reporte de Errores Léxicos</h1>");
        
        if (erroresLexicos.isEmpty()) {
            erroresReport.println("<p>No se encontraron errores léxicos.</p>");
        } else {
            erroresReport.println("<ul>");
            for (String error : erroresLexicos) {
                erroresReport.println("<li>" + error + "</li>");
            }
            erroresReport.println("</ul>");
        }
        
        erroresReport.println("</body></html>");

        JOptionPane.showMessageDialog(this, "Reportes HTML generados correctamente.");
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(this, "Error al generar reportes: " + ex.getMessage());
    }
}


    public static void main(String[] args) {
        new ADF_GRAPH12();
    }

    class Automata {
        String nombre;
        String descripcion;
        ArrayList<String> estados = new ArrayList<>();
        ArrayList<String> alfabeto = new ArrayList<>();
        String estadoInicial;
        ArrayList<String> estadosFinales = new ArrayList<>();
        HashMap<String, HashMap<String, String>> transiciones = new HashMap<>();

        Automata(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return "<strong>Nombre:</strong> " + nombre + "<br>" +
                   "<strong>Descripción:</strong> " + descripcion + "<br>" +
                   "<strong>Estados:</strong> " + estados + "<br>" +
                   "<strong>Alfabeto:</strong> " + alfabeto + "<br>" +
                   "<strong>Estado Inicial:</strong> " + estadoInicial + "<br>" +
                   "<strong>Estados Finales:</strong> " + estadosFinales + "<br>" +
                   "<strong>Transiciones:</strong> " + transiciones + "<br><br>";
        }
    }
}