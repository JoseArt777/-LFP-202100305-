/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana principal de la aplicación
 */
public class VentanaPrincipal extends JFrame {
    private JTextArea areaTexto;
    private JPanel panelImagen;
    private JComboBox<String> selectorMapas;
    private JButton btnCargar, btnLimpiar, btnAnalizar, btnReportes, btnAcerca;
    private DefaultComboBoxModel<String> modeloSelector;
    
    private AnalizadorArchivo analizador;
    private GeneradorDot generadorDot;
    private GeneradorReportes generadorReportes;
    private List<Mundo> mundos;
    private String directorioTemporal;
    
    public VentanaPrincipal() {
        super("Generador Visual de Mapas Narrativos");
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
        // Configuración de codificación Unicode
System.setProperty("file.encoding", "UTF-8");
        
        analizador = new AnalizadorArchivo();
        generadorDot = new GeneradorDot();
        generadorReportes = new GeneradorReportes();
        mundos = new ArrayList<>();
        
        // Crear directorio temporal para archivos DOT e imágenes
        try {
            directorioTemporal = "temp_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Files.createDirectories(Paths.get(directorioTemporal));
        } catch (IOException e) {
            System.err.println("Error al crear directorio temporal: " + e.getMessage());
            directorioTemporal = ".";
        }
    }
    
    private void inicializarComponentes() {
        // Área de texto
        areaTexto = new JTextArea();
        areaTexto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        
        // Panel de imagen
        panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBackground(Color.WHITE);
        JScrollPane scrollImagen = new JScrollPane(panelImagen);
        
        // Botones
        btnCargar = new JButton("Cargar Archivo");
        btnLimpiar = new JButton("Limpiar Área");
        btnAnalizar = new JButton("Analizar Archivo");
        btnReportes = new JButton("Generar Reportes");
        btnAcerca = new JButton("Acerca de");
        
        // Selector de mapas
        modeloSelector = new DefaultComboBoxModel<>();
        selectorMapas = new JComboBox<>(modeloSelector);
        selectorMapas.setEnabled(false);
    }
    
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Panel izquierdo (área de texto y botones)
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnCargar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnAnalizar);
        
        JLabel labelTexto = new JLabel("Área de Texto:");
        labelTexto.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panelIzquierdo.add(labelTexto, BorderLayout.NORTH);
        panelIzquierdo.add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);
        
        // Panel derecho (imagen y selector)
        JPanel panelDerecho = new JPanel(new BorderLayout());
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        panelControl.add(new JLabel("Seleccionar mapa:"));
        panelControl.add(selectorMapas);
        panelControl.add(btnReportes);
        panelControl.add(btnAcerca);
        
        JLabel labelImagen = new JLabel("Área de Imagen:");
        labelImagen.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        panelDerecho.add(labelImagen, BorderLayout.NORTH);
        panelDerecho.add(new JScrollPane(panelImagen), BorderLayout.CENTER);
        panelDerecho.add(panelControl, BorderLayout.SOUTH);
        
        // Divisor principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelIzquierdo, panelDerecho);
        splitPane.setDividerLocation(500);
        
        // Agregar al frame principal
        getContentPane().add(splitPane, BorderLayout.CENTER);
        
        // Configurar evento de cierre para limpiar archivos temporales
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                limpiarDirectorioTemporal();
                super.windowClosing(e);
            }
        });
    }
    
    private void configurarEventos() {
        btnCargar.addActionListener((ActionEvent e) -> cargarArchivo());
        btnLimpiar.addActionListener((ActionEvent e) -> areaTexto.setText(""));
        btnAnalizar.addActionListener((ActionEvent e) -> analizarArchivo());
        btnReportes.addActionListener((ActionEvent e) -> mostrarMenuReportes());
        btnAcerca.addActionListener((ActionEvent e) -> mostrarAcercaDe());
        selectorMapas.addActionListener((ActionEvent e) -> seleccionarMapa());
    }
    
    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showOpenDialog(this);
        
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (FileReader fr = new FileReader(archivo)) {
                StringBuilder contenido = new StringBuilder();
                char[] buffer = new char[1024];
                int leido;
                
                while ((leido = fr.read(buffer)) != -1) {
                    contenido.append(buffer, 0, leido);
                }
                
                areaTexto.setText(contenido.toString());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                        "Error al cargar el archivo: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void analizarArchivo() {
        String texto = areaTexto.getText();
        if (texto.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No hay texto para analizar", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpiar selector de mapas
        modeloSelector.removeAllElements();
        selectorMapas.setEnabled(false);
        
        // Limpiar panel de imagen
        panelImagen.removeAll();
        panelImagen.revalidate();
        panelImagen.repaint();
        
        // Analizar el texto
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            mundos = analizador.analizar(texto);
            
            if (mundos.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                        "No se encontraron mapas válidos en el texto", 
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // Llenar el selector con los nombres de los mundos
                for (Mundo mundo : mundos) {
                    modeloSelector.addElement(mundo.getNombre());
                }
                
                selectorMapas.setEnabled(true);
                selectorMapas.setSelectedIndex(0);
                seleccionarMapa(); // Mostrar el primer mapa
                
                JOptionPane.showMessageDialog(this, 
                        "Análisis completado. Se encontraron " + mundos.size() + " mundos.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Mostrar errores si los hay
            if (!analizador.getErrores().isEmpty()) {
                int cantidadErrores = analizador.getErrores().size();
                int respuesta = JOptionPane.showConfirmDialog(this, 
                        "Se encontraron " + cantidadErrores + " errores durante el análisis. ¿Desea ver el reporte de errores?", 
                        "Errores encontrados", JOptionPane.YES_NO_OPTION);
                
                if (respuesta == JOptionPane.YES_OPTION) {
                    generarReporteErrores();
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al analizar el archivo: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void seleccionarMapa() {
        int indice = selectorMapas.getSelectedIndex();
        if (indice >= 0 && indice < mundos.size()) {
            Mundo mundoSeleccionado = mundos.get(indice);
            visualizarMapa(mundoSeleccionado);
        }
    }
    
    private void visualizarMapa(Mundo mundo) {
        // Limpiar panel de imagen
        panelImagen.removeAll();
        
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            // Generar nombre de archivo único
            String nombreArchivo = mundo.getNombre().replaceAll("[^a-zA-Z0-9]", "_");
            String rutaDot = directorioTemporal + "/" + nombreArchivo + ".dot";
            String rutaImagen = directorioTemporal + "/" + nombreArchivo + ".png";
            
            // Generar DOT y convertir a imagen
            if (generadorDot.generarDot(mundo, rutaDot)) {
                boolean imagenGenerada = generadorDot.generarImagen(rutaDot, rutaImagen, "png");
                
                if (imagenGenerada) {
                    // Cargar y mostrar la imagen
                    BufferedImage imagen = ImageIO.read(new File(rutaImagen));
                    JLabel labelImagen = new JLabel(new ImageIcon(imagen));
                    
                    JScrollPane scrollPane = new JScrollPane(labelImagen);
                    panelImagen.setLayout(new BorderLayout());
                    panelImagen.add(scrollPane, BorderLayout.CENTER);
                } else {
                    // Si hay error al generar la imagen, mostrar el código DOT
                    mostrarCodigoDot(mundo);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error al generar el archivo DOT para el mapa", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            mostrarCodigoDot(mundo);
            System.err.println("Error al visualizar mapa: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
            panelImagen.revalidate();
            panelImagen.repaint();
        }
    }
    
    /**
     * Muestra el código DOT en lugar de la imagen (alternativa si Graphviz falla)
     */
    private void mostrarCodigoDot(Mundo mundo) {
        String codigoDot = generadorDot.obtenerContenidoDot(mundo);
        
        JTextArea areaDot = new JTextArea(codigoDot);
        areaDot.setEditable(false);
        areaDot.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(areaDot);
        panelImagen.setLayout(new BorderLayout());
        
        JLabel etiquetaError = new JLabel("No se pudo generar la imagen. Mostrando código DOT:");
        etiquetaError.setForeground(Color.RED);
        etiquetaError.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        panelImagen.add(etiquetaError, BorderLayout.NORTH);
        panelImagen.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void mostrarMenuReportes() {
        if (analizador.getTokens().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "No hay datos para generar reportes. Analice un archivo primero.", 
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] opciones = {"Reporte de Tokens", "Reporte de Errores", "Ambos"};
        int seleccion = JOptionPane.showOptionDialog(this, 
                "Seleccione el tipo de reporte a generar", 
                "Generar Reportes", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, opciones, opciones[0]);
        
        switch (seleccion) {
            case 0: // Tokens
                generarReporteTokens();
                break;
            case 1: // Errores
                generarReporteErrores();
                break;
            case 2: // Ambos
                generarAmbosReportes();
                break;
            default:
                // No hacer nada si se cierra el diálogo
                break;
        }
    }
    
    private void generarReporteTokens() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (generadorReportes.generarReportes(analizador.getTokens(), new ArrayList<>())) {
                boolean abierto = generadorReportes.abrirReporte("tokens");
                
                if (!abierto) {
                    JOptionPane.showMessageDialog(this, 
                            "El reporte se generó correctamente, pero no se pudo abrir automáticamente.\n" +
                            "Puede encontrarlo en la carpeta 'reportes'.", 
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al generar el reporte de tokens: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void generarReporteErrores() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (generadorReportes.generarReportes(new ArrayList<>(), analizador.getErrores())) {
                boolean abierto = generadorReportes.abrirReporte("errores");
                
                if (!abierto) {
                    JOptionPane.showMessageDialog(this, 
                            "El reporte se generó correctamente, pero no se pudo abrir automáticamente.\n" +
                            "Puede encontrarlo en la carpeta 'reportes'.", 
                            "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al generar el reporte de errores: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void generarAmbosReportes() {
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            if (generadorReportes.generarReportes(analizador.getTokens(), analizador.getErrores())) {
                JOptionPane.showMessageDialog(this, 
                        "Los reportes se generaron correctamente en la carpeta 'reportes'.", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
                // Mostrar opciones para abrir
                String[] opciones = {"Reporte de Tokens", "Reporte de Errores", "Ninguno"};
                int seleccion = JOptionPane.showOptionDialog(this, 
                        "¿Qué reporte desea abrir?", 
                        "Abrir Reporte", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, opciones, opciones[0]);
                
                switch (seleccion) {
                    case 0: // Tokens
                        generadorReportes.abrirReporte("tokens");
                        break;
                    case 1: // Errores
                        generadorReportes.abrirReporte("errores");
                        break;
                    default:
                        // No hacer nada si selecciona "Ninguno" o cierra el diálogo
                        break;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Error al generar los reportes: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    private void mostrarAcercaDe() {
        JOptionPane.showMessageDialog(this, 
                "Generador Visual de Mapas Narrativos\n\n" +
                "Desarrollado por: [Tu Nombre]\n" +
                "Carnet: [Tu Carnet]\n\n" +
                "Universidad de San Carlos de Guatemala\n" +
                "Facultad de Ingeniería\n" +
                "Lenguajes Formales y de Programación\n" +
                "Primer Semestre 2025", 
                "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Limpia los archivos temporales al cerrar la aplicación
     */
    private void limpiarDirectorioTemporal() {
        try {
            Path directorio = Paths.get(directorioTemporal);
            if (Files.exists(directorio)) {
                Files.walk(directorio)
                    .sorted((a, b) -> b.toString().compareTo(a.toString())) // Orden inverso (primero archivos, luego directorios)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("No se pudo eliminar: " + path);
                        }
                    });
            }
        } catch (IOException e) {
            System.err.println("Error al limpiar directorio temporal: " + e.getMessage());
        }
    }
}