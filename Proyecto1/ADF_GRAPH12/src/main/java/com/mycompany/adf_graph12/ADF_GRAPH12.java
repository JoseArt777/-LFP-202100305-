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
    
    // filtra los archivos .lfp
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Archivos LFP (*.lfp)", "lfp");
    fileChooser.setFileFilter(filter);
    
    int result = fileChooser.showOpenDialog(this);

    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        archivoArea.setText("");
        erroresLexicos.clear(); // Limpia errores anteriores
        automatas.clear(); // Limpia autómatas anteriores
        selectAutomata.removeAllItems(); // Limpia ComboBox
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int c;
            Automata automataActual = null;
            int lineaActual = 1;
            int charActual = 1; // Comienza en 1 
            StringBuilder currentLine = new StringBuilder();
            StringBuilder fileContent = new StringBuilder(); // muestra en el area de texto
            boolean dentroTransiciones = false;
            boolean transicionesAbiertas = false;
            int llavesCerradas = 0;

            while ((c = br.read()) != -1) {
                char ch = (char) c;
                fileContent.append(ch); 
                currentLine.append(ch);

                if (ch == '\n') {
                    String line = currentLine.toString().trim();
                    
                    // Procesa 
                    if (!line.isEmpty()) {
                        // Verificar si comienza con nombre de autómata
                        if (line.contains(":") && !line.startsWith("descripcion") && 
                            !line.startsWith("estados") && !line.startsWith("alfabeto") && 
                            !line.startsWith("inicial") && !line.startsWith("finales") && 
                            !line.startsWith("transiciones") && !dentroTransiciones) {
                            
                            String nombre = line.split(":")[0].trim();
                            
                            // Verificar que el nombre del autómata no comience con números
                            if (nombre.matches("^\\d.*")) {
                                // Find the position of the first digit in the name
                                int errorPos = line.indexOf(nombre);
                                if (errorPos < 0) errorPos = 0; // Fallback if name not found
                                
                                erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                    " carácter " + errorPos + ": El nombre del autómata '" + nombre + 
                                    "' no debe comenzar con un número");
                            }
                            
                            automataActual = new Automata(nombre);
                            automatas.put(nombre, automataActual);
                            selectAutomata.addItem(nombre);
                            
                        } else if (automataActual != null) {
                            try {
                                if (line.startsWith("descripcion")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "descripcion".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'descripcion'");
                                    } else {
                                        String valor = line.split(":", 2)[1].trim();
                                        if (!valor.startsWith("\"") || (!valor.endsWith("\"") && !valor.endsWith("\","))) {
                                            int errorPos = line.indexOf(":") + 2;
                                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                " carácter " + errorPos + ": La descripción debe estar entre comillas");
                                        } else {
                                            automataActual.descripcion = valor.replace("\"", "").replace(",", "").trim();
                                        }
                                    }
                                } else if (line.startsWith("estados")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "estados".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'estados'");
                                    } else if (!line.contains("[")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de apertura '[' en la lista de estados");
                                    } else if (!line.contains("]")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de cierre ']' en la lista de estados");
                                    } else {
                                        String estados = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                                        automataActual.estados.addAll(Arrays.asList(estados.split(",\\s*")));
                                    }
                                } else if (line.startsWith("alfabeto")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "alfabeto".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'alfabeto'");
                                    } else if (!line.contains("[")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de apertura '[' en el alfabeto");
                                    } else if (!line.contains("]")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de cierre ']' en el alfabeto");
                                    } else {
                                        String alfabeto = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                                        // Verificar que todos los símbolos estén entre comillas
                                        String[] simbolos = alfabeto.split(",\\s*");
                                        for (String simbolo : simbolos) {
                                            if (!simbolo.startsWith("\"") || !simbolo.endsWith("\"")) {
                                                int errorPos = line.indexOf(simbolo, line.indexOf("["));
                                                erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                    " carácter " + errorPos + ": Símbolo '" + simbolo + 
                                                    "' debe estar entre comillas");
                                            }
                                        }
                                        
                                        // Agregar símbolos al autómata
                                        automataActual.alfabeto.addAll(Arrays.asList(alfabeto.replace("\"", "").split(",\\s*")));
                                    }
                                } else if (line.startsWith("inicial")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "inicial".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'inicial'");
                                    } else {
                                        automataActual.estadoInicial = line.split(":", 2)[1].replace(",", "").trim();
                                    }
                                } else if (line.startsWith("finales")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "finales".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'finales'");
                                    } else if (!line.contains("[")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de apertura '[' en estados finales");
                                    } else if (!line.contains("]")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta corchete de cierre ']' en estados finales");
                                    } else {
                                        String finales = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                                        automataActual.estadosFinales.addAll(Arrays.asList(finales.split(",\\s*")));
                                    }
                                } else if (line.startsWith("transiciones")) {
                                    if (!line.contains(":")) {
                                        int errorPos = "transiciones".length() + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta el símbolo ':' después de 'transiciones'");
                                    } else if (!line.contains("{")) {
                                        int errorPos = line.indexOf(":") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Las transiciones deben estar entre llaves {}");
                                    } else {
                                        dentroTransiciones = true;
                                        transicionesAbiertas = true;
                                    }
                                } else if (dentroTransiciones && line.contains("=")) {
                                    // Procesando líneas de transiciones
                                    String estadoOrigen = line.split("=")[0].trim();
                                    
                                    // Verifica que el estado origen exista
                                    if (!automataActual.estados.contains(estadoOrigen)) {
                                        int errorPos = line.indexOf(estadoOrigen);
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Estado origen '" + estadoOrigen + "' no definido");
                                    }
                                    
                                    // Verifica paréntesis de apertura
                                    if (!line.contains("(")) {
                                        int errorPos = line.indexOf("=") + 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta paréntesis de apertura '(' en las transiciones");
                                    }
                                    
                                    // Verifica paréntesis de cierre
                                    if (!line.contains(")")) {
                                        int errorPos = line.length() - 1;
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + errorPos + ": Falta paréntesis de cierre ')' en las transiciones");
                                    } else {
                                        try {
                                            String transiciones = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")"));
                                            String[] trans = transiciones.split(",\\s*");
                                            HashMap<String, String> mapaTransiciones = new HashMap<>();
                                            
                                            for (String t : trans) {
                                                // Verificar flechas correctas
                                                if (t.contains("->>") || t.contains("->->") || t.contains("-<") || 
                                                    t.contains("<-") || !t.contains("->")) {
                                                    int errorPos = line.indexOf(t, line.indexOf("("));
                                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                        " carácter " + errorPos + ": Formato incorrecto de flecha en transición: " + t);
                                                    continue;
                                                }
                                                
                                                String[] partes = t.split("->");
                                                if (partes.length != 2) {
                                                    int errorPos = line.indexOf(t, line.indexOf("("));
                                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                        " carácter " + errorPos + ": Formato incorrecto en transición: " + t);
                                                    continue;
                                                }
                                                
                                                String simbolo = partes[0].trim().replace("\"", "");
                                                String destino = partes[1].trim();
                                                
                                                // Verificar comillas en símbolos
                                                if (!t.contains("\"")) {
                                                    int errorPos = line.indexOf(t, line.indexOf("("));
                                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                        " carácter " + errorPos + ": Símbolo debe estar entre comillas en: " + t);
                                                }
                                                
                                                // Verifica si el símbolo está en el alfabeto
                                                if (!automataActual.alfabeto.contains(simbolo)) {
                                                    int errorPos = line.indexOf(simbolo, line.indexOf("("));
                                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                        " carácter " + errorPos + ": Símbolo '" + simbolo + "' no definido en el alfabeto");
                                                }
                                                
                                                // Verificar que el estado destino exista
                                                if (!automataActual.estados.contains(destino)) {
                                                    int errorPos = line.indexOf(destino, line.indexOf("->"));
                                                    erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                        " carácter " + errorPos + ": Estado destino '" + destino + "' no definido");
                                                }
                                                
                                                mapaTransiciones.put(simbolo, destino);
                                            }
                                            automataActual.transiciones.put(estadoOrigen, mapaTransiciones);
                                        } catch (StringIndexOutOfBoundsException e) {
                                            // Si hay error en el formato de los paréntesis
                                            erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                                ": Problema con los paréntesis en las transiciones");
                                        }
                                    }
                                } else if (line.equals("}") && dentroTransiciones) {
                                    // Fin de las transiciones
                                    dentroTransiciones = false;
                                    llavesCerradas++;
                                    
                                    if (llavesCerradas == 2) {
                                        // Fin del autómata
                                        automataActual = null;
                                        llavesCerradas = 0;
                                    }
                                } else if (line.equals("}") && !dentroTransiciones) {
                                    // Fin del autómata
                                    llavesCerradas++;
                                    if (llavesCerradas == 2) {
                                        automataActual = null;
                                        llavesCerradas = 0;
                                    }
                                } else if (!line.startsWith("{") && !line.isEmpty()) {
                                    if (line.startsWith("final:")) {
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter 0: Uso incorrecto de 'final:' en lugar de 'finales:'");
                                    } else {
                                        // Get the position of the first character in the current line
                                        int errorPos = fileContent.toString().lastIndexOf(line);
                                        int charPosition = errorPos - fileContent.toString().lastIndexOf('\n', errorPos) - 1;
                                        if (charPosition < 0) charPosition = 0; // Handle first line
                                        
                                        erroresLexicos.add("Error léxico en línea " + lineaActual + 
                                            " carácter " + charPosition + ": Línea no reconocida: " + line);
                                    }
                                }
                            } catch (Exception ex) {
                                erroresLexicos.add("Error léxico en línea " + lineaActual + ": " + 
                                    line + " - " + ex.getMessage());
                            }
                        }
                    }
                    
                    // Prepara para la siguiente línea
                    lineaActual++;
                    charActual = 1;
                    currentLine.setLength(0);
                } else {
                    charActual++;
                }
            }
            
            // Mostrar el contenido en el área de texto
            archivoArea.setText(fileContent.toString());
            
            // Validar si los automatas están completos
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