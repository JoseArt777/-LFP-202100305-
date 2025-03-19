/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.List;

public class AutomataViewer extends JPanel {
    private Automaton automaton;
    private static final int STATE_RADIUS = 30;
    private static final int ARROW_SIZE = 10;

    public AutomataViewer() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    public void setAutomaton(Automaton automaton) {
        this.automaton = automaton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (automaton == null) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar estados
        for (State state : automaton.getStates()) {
            drawState(g2d, state, automaton.getInitialState().equals(state), automaton.getFinalStates().contains(state));
        }
        
        // Dibujar transiciones
        for (State fromState : automaton.getStates()) {
            List<Transition> transitions = automaton.getTransitionsFrom(fromState);
            for (Transition transition : transitions) {
                drawTransition(g2d, transition);
            }
        }
    }
    
    private void drawState(Graphics2D g2d, State state, boolean isInitial, boolean isFinal) {
        int x = state.getPosX();
        int y = state.getPosY();
        
        // Dibujar círculo del estado
        g2d.setColor(Color.WHITE);
        g2d.fill(new Ellipse2D.Double(x - STATE_RADIUS, y - STATE_RADIUS, 2 * STATE_RADIUS, 2 * STATE_RADIUS));
        g2d.setColor(Color.BLACK);
        g2d.draw(new Ellipse2D.Double(x - STATE_RADIUS, y - STATE_RADIUS, 2 * STATE_RADIUS, 2 * STATE_RADIUS));
        
        // Si es un estado final, dibujar un círculo adicional
        if (isFinal) {
            g2d.draw(new Ellipse2D.Double(x - STATE_RADIUS + 5, y - STATE_RADIUS + 5, 2 * STATE_RADIUS - 10, 2 * STATE_RADIUS - 10));
        }
        
        // Si es el estado inicial, dibujar una flecha apuntando hacia él
        if (isInitial) {
            g2d.draw(new Line2D.Double(x - 2 * STATE_RADIUS, y, x - STATE_RADIUS, y));
            drawArrowHead(g2d, x - STATE_RADIUS, y, 0);
        }
        
        // Dibujar el nombre del estado
        FontMetrics fm = g2d.getFontMetrics();
        String name = state.getName();
        int textWidth = fm.stringWidth(name);
        int textHeight = fm.getHeight();
        g2d.drawString(name, x - textWidth / 2, y + textHeight / 4);
    }
    
    private void drawTransition(Graphics2D g2d, Transition transition) {
        State fromState = transition.getFromState();
        State toState = transition.getToState();
        String symbol = transition.getSymbol();
        
        int x1 = fromState.getPosX();
        int y1 = fromState.getPosY();
        int x2 = toState.getPosX();
        int y2 = toState.getPosY();
        
        // Calcular el ángulo de la línea
        double angle = Math.atan2(y2 - y1, x2 - x1);
        
        // Calcular los puntos de inicio y fin ajustados por el radio del estado
        int startX = (int) (x1 + STATE_RADIUS * Math.cos(angle));
        int startY = (int) (y1 + STATE_RADIUS * Math.sin(angle));
        int endX = (int) (x2 - STATE_RADIUS * Math.cos(angle));
        int endY = (int) (y2 - STATE_RADIUS * Math.sin(angle));
        
        // Dibujar la línea de la transición
        g2d.draw(new Line2D.Double(startX, startY, endX, endY));
        
        // Dibujar la punta de la flecha
        drawArrowHead(g2d, endX, endY, angle);
        
        // Dibujar el símbolo de la transición
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(symbol);
        int textHeight = fm.getHeight();
        
        // Posición del texto (a mitad de camino entre los estados)
        int textX = (startX + endX) / 2 - textWidth / 2;
        int textY = (startY + endY) / 2 + textHeight / 4;
        
        // Dibujar un fondo blanco para el texto
        g2d.setColor(Color.WHITE);
        g2d.fillRect(textX - 2, textY - textHeight + 2, textWidth + 4, textHeight);
        
        // Dibujar el texto
        g2d.setColor(Color.BLACK);
        g2d.drawString(symbol, textX, textY);
    }
    private void drawArrowHead(Graphics2D g2d, int x, int y, double angle) {
    AffineTransform old = g2d.getTransform();
    
    g2d.translate(x, y);
    g2d.rotate(angle);
    
    // Dibujar la punta de flecha
    int[] xPoints = {0, -ARROW_SIZE, -ARROW_SIZE};
    int[] yPoints = {0, -ARROW_SIZE/2, ARROW_SIZE/2};
    g2d.fillPolygon(xPoints, yPoints, 3);
    
    g2d.setTransform(old);
}
}    

    
