/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal - Generador Visual de Mapas Narrativos
 * USAC - Lenguajes Formales y de Programación
 */
public class Proyecto2 {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al configurar UI: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}