/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutomataManager {
    private final Map<String, Automaton> automata;
    private LexicalAnalyzer lexicalAnalyzer;

    public AutomataManager() {
        this.automata = new HashMap<>();
    }

    public void loadAutomata(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        
        // Lógica para construir los autómatas a partir de los tokens
        // Esta es solo una versión simplificada, necesitarás implementar la lógica completa
        List<Token> tokens = lexicalAnalyzer.getTokens();
        
        // Aquí implementarías el algoritmo para procesar los tokens y construir los autómatas
        // Esto incluiría la interpretación de la estructura del archivo de entrada
        
        // Por ahora, solo como ejemplo, creamos un autómata de prueba
        createSampleAutomaton();
    }
    
    private void createSampleAutomaton() {
        // Creamos un autómata de ejemplo para fines de prueba
        Automaton automaton = new Automaton("AFD1");
        automaton.setDescription("Este autómata reconoce cadenas numéricas.");
        
        // Crear estados
        State s0 = new State("S0");
        State s1 = new State("S1");
        State s2 = new State("S2");
        
        // Agregar estados al autómata
        automaton.addState(s0);
        automaton.addState(s1);
        automaton.addState(s2);
        
        // Definir alfabeto
        List<String> alphabet = new ArrayList<>();
        alphabet.add("1");
        alphabet.add("2");
        automaton.setAlphabet(alphabet);
        
        // Definir estado inicial
        automaton.setInitialState(s0);
        
        // Definir estados finales
        List<State> finalStates = new ArrayList<>();
        finalStates.add(s2);
        automaton.setFinalStates(finalStates);
        
        // Definir transiciones
        automaton.addTransition(s0, "1", s1);
        automaton.addTransition(s1, "2", s2);
        
        // Añadir el autómata al mapa
        automata.put(automaton.getName(), automaton);
    }
    
    public Automaton getAutomaton(String name) {
        return automata.get(name);
    }
    
    public List<String> getAutomataNames() {
        return new ArrayList<>(automata.keySet());
    }
    
    public int getAutomataCount() {
        return automata.size();
    }
    
    public LexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }
}