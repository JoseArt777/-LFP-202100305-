package com.mycompany.afd_graph13;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class InterfazPrincipal extends JFrame {
    private final HashMap<String, Automata> automatas = new HashMap<>();
    private final ArrayList<String> erroresLexicos = new ArrayList<>();
    private final AnalizadorLexico analizador = new AnalizadorLexico();

    private final JTextArea archivoArea = new JTextArea();
    private final JLabel imagenLabel = new JLabel();
    private final JComboBox<String> selectAutomata = new JComboBox<>();
    
    // Definición de colores personalizados
    private final Color COLOR_FONDO = new Color(240, 240, 245);
    private final Color COLOR_PANEL = new Color(255, 255, 255);
    private final Color COLOR_BORDE = new Color(70, 130, 180);
    private final Color COLOR_BOTON = new Color(70, 130, 180);
    private final Color COLOR_TEXTO_BOTON = Color.WHITE;
    private final Color COLOR_TITULO = new Color(40, 80, 120);

    public InterfazPrincipal() {
        setTitle("AFDGraph - Visualizador de Autómatas");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Establecer el color de fondo de la ventana principal
        getContentPane().setBackground(COLOR_FONDO);

        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception e) { e.printStackTrace(); }
        
        // Personalizar la apariencia de los componentes Swing
        personalizarUI();
        
        configurarComponentes();

        setVisible(true);
    }
    
    private void personalizarUI() {
        // Personalizar botones
        UIManager.put("Button.background", COLOR_BOTON);
        UIManager.put("Button.foreground", COLOR_TEXTO_BOTON);
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 12));
        UIManager.put("Button.select", new Color(50, 100, 140));
        UIManager.put("Button.focus", new Color(70, 130, 180, 80));
        
        // Personalizar ComboBox
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.selectionBackground", COLOR_BOTON);
        UIManager.put("ComboBox.selectionForeground", Color.WHITE);
        
        // Personalizar bordes de TitledBorder
        UIManager.put("TitledBorder.font", new Font("Arial", Font.BOLD, 12));
        UIManager.put("TitledBorder.titleColor", COLOR_TITULO);
    }
    
    private void configurarComponentes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(COLOR_FONDO);

        // Panel superior con botones
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        panelSuperior.setBackground(COLOR_FONDO);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton analizarBtn = crearBotonEstilizado("Analizar Archivo");
        JButton graficarBtn = crearBotonEstilizado("Graficar Autómata");
        JButton reportesBtn = crearBotonEstilizado("Generar Reportes");

        analizarBtn.setPreferredSize(new Dimension(160, 35));
        graficarBtn.setPreferredSize(new Dimension(160, 35));
        reportesBtn.setPreferredSize(new Dimension(160, 35));

        // Personalizar ComboBox
        selectAutomata.setPreferredSize(new Dimension(200, 35));
        selectAutomata.setFont(new Font("Arial", Font.PLAIN, 12));
        ((JComponent) selectAutomata.getRenderer()).setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        panelSuperior.add(analizarBtn);
        panelSuperior.add(Box.createHorizontalStrut(15));
        panelSuperior.add(reportesBtn);
        panelSuperior.add(Box.createHorizontalStrut(15));
        panelSuperior.add(selectAutomata);
        panelSuperior.add(Box.createHorizontalStrut(15));
        panelSuperior.add(graficarBtn);

        // Panel inferior con split horizontal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(550);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);

        // Panel izquierdo: contenido del archivo
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBackground(COLOR_PANEL);
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 2, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ),
                "Contenido del Archivo"
        ));

        archivoArea.setEditable(false);
        archivoArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        archivoArea.setBackground(new Color(252, 252, 255));
        archivoArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollArchivo = new JScrollPane(archivoArea);
        scrollArchivo.setBorder(BorderFactory.createEmptyBorder());
        scrollArchivo.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(70, 130, 180, 150);
                this.trackColor = Color.WHITE;
            }
        });

        panelIzquierdo.add(scrollArchivo, BorderLayout.CENTER);

        // Panel derecho: imagen del autómata
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBackground(COLOR_PANEL);
        panelDerecho.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDE, 2, true),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ),
                "Visualización del Autómata"
        ));

        imagenLabel.setHorizontalAlignment(JLabel.CENTER);
        imagenLabel.setBackground(new Color(248, 248, 252));
        imagenLabel.setOpaque(true);
        
        JScrollPane scrollImagen = new JScrollPane(imagenLabel);
        scrollImagen.setBorder(BorderFactory.createEmptyBorder());
        scrollImagen.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(70, 130, 180, 150);
                this.trackColor = Color.WHITE;
            }
        });

        panelDerecho.add(scrollImagen, BorderLayout.CENTER);

        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);

        // Eventos
        analizarBtn.addActionListener(e -> analizarArchivo());
        graficarBtn.addActionListener(e -> graficar());
        reportesBtn.addActionListener(e -> generarReportes());

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(splitPane, BorderLayout.CENTER);

        add(panelPrincipal);
    }
    
    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradiente para el fondo del botón
                GradientPaint gp = new GradientPaint(
                        0, 0, COLOR_BOTON, 
                        0, getHeight(), new Color(40, 90, 130));
                
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Efecto de brillo
                if (getModel().isPressed()) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                }
                
                // Dibujar texto
                g2.setColor(COLOR_TEXTO_BOTON);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
        };
        
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(COLOR_TEXTO_BOTON);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return boton;
    }

    private void analizarArchivo() {
        analizador.analizarArchivo(this, archivoArea, selectAutomata, automatas, erroresLexicos);
    }

    private void graficar() {
        String nombre = (String) selectAutomata.getSelectedItem();
        if (nombre != null && automatas.containsKey(nombre)) {
            new GeneradorGrafica().graficarAutomata(automatas.get(nombre), imagenLabel);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un autómata válido para graficar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReportes() {
        new GeneradorReportes(analizador).generar(automatas, erroresLexicos);
        
        // Diálogo de confirmación personalizado
        JDialog dialog = new JDialog(this, "Reportes Generados", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_PANEL);
        
        JLabel mensaje = new JLabel("<html>Reportes generados correctamente:<br><br>" +
                "• reporte_tokens.html<br>" +
                "• reporte_automatas.html<br>" +
                "• reporte_errores.html</html>");
        mensaje.setFont(new Font("Arial", Font.PLAIN, 13));
        
        JButton okBtn = crearBotonEstilizado("Aceptar");
        okBtn.addActionListener(e -> dialog.dispose());
        
        panel.add(mensaje, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(COLOR_PANEL);
        btnPanel.add(okBtn);
        
        panel.add(btnPanel, BorderLayout.SOUTH);
        dialog.add(panel);
        
        dialog.setVisible(true);
    }
}