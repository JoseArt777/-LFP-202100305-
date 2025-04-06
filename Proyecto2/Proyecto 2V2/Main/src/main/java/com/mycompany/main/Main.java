/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.main;

/**
 *
 * @author iosea
 */

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal que inicia la aplicación.
 * Proyecto 2 - Generador Visual de Mapas Narrativos
 * Lenguajes Formales y de Programación
 */
public class Main {
    
    public static void main(String[] args) {
        // Configurar el Look and Feel para una mejor apariencia
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}