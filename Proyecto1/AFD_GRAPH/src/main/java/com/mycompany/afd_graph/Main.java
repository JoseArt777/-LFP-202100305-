/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.afd_graph;

import javax.swing.SwingUtilities;

/**
 *
 * @author iosea
 */
public class Main {
    public static void main(String[] args) {
        // Iniciar la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}