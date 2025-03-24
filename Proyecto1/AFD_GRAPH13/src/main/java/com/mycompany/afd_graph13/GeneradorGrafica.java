/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph13;



import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class GeneradorGrafica {
    public void graficarAutomata(Automata automata, JLabel imagenLabel) {
        try {
            PrintWriter pw = new PrintWriter("automata.dot");
            pw.println("digraph Automata {");
            pw.println("rankdir=LR;");
            pw.println("node [shape = doublecircle]; " + String.join(" ", automata.getEstadosFinales()) + ";");
            pw.println("node [shape = circle];");

            automata.getTransiciones().forEach((origen, mapa) ->
                mapa.forEach((simbolo, destino) ->
                    pw.println(origen + " -> " + destino + " [label=\"" + simbolo + "\"]"))
            );

            pw.println("}");
            pw.close();

            Runtime.getRuntime().exec("dot -Tpng automata.dot -o automata.png").waitFor();
            BufferedImage img = ImageIO.read(new File("automata.png"));
            imagenLabel.setIcon(new ImageIcon(img));
            imagenLabel.revalidate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al graficar: " + e.getMessage());
        }
    }
}
