/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AutomataManager {
    private Map<String, Automaton> automataMap;
    private LexicalAnalyzer lexicalAnalyzer;

    public AutomataManager() {
        this.automataMap = new HashMap<>();
    }

    public void loadAutomata(LexicalAnalyzer lexicalAnalyzer) {
        this.lexicalAnalyzer = lexicalAnalyzer;
        
        // Aquí implementarías el análisis sintáctico para construir los autómatas
        // a partir de los tokens generados por el analizador léxico
        
        // Por ahora, como ejemplo, podemos simular la carga de un autómata
        parseTokens(lexicalAnalyzer.getTokens());
    }
    
    private void parseTokens(java.util.List<Token> tokens) {
        // Aquí implementarías el análisis sintáctico 
        // Este es un trabajo más complejo que requiere recorrer los tokens
        // y construir los autómatas según la gramática del archivo de entrada
        
        // Por ejemplo, podríamos detectar el nombre del autómata, sus estados,
        // transiciones, etc.
        
        // Simplemente como demostración, creemos un autómata de ejemplo
        if (automataMap.isEmpty()) {
            createSampleAutomaton();
        }
    }
    
    private void createSampleAutomaton() {
        Automaton automaton = new Automaton("AFD1");
        automaton.setDescription("Este autómata reconoce cadenas numéricas.");
        
        // Crear estados
        State s0 = new State("S0");
        State s1 = new State("S1");
        State s2 = new State("S2");
        
        // Asignar posiciones para visualización
        s0.setPosX(150);
        s0.setPosY(100);
        s1.setPosX(300);
        s1.setPosY(100);
        s2.setPosX(450);
        s2.setPosY(100);
        
        // Añadir estados al autómata
        automaton.addState(s0);
        automaton.addState(s1);
        automaton.addState(s2);
        
        // Configurar alfabeto
        automaton.setAlphabet(java.util.Arrays.asList("1", "2", "3"));
        
        // Configurar estado inicial y estados finales
        automaton.setInitialState(s0);
        automaton.setFinalStates(java.util.Arrays.asList(s1, s2));
        
        // Añadir transiciones
        automaton.addTransition(s0, "1", s1);
        automaton.addTransition(s0, "2", s2);
        automaton.addTransition(s1, "2", s1);
        automaton.addTransition(s2, "3", s0);
        
        // Guardar el autómata
        automataMap.put(automaton.getName(), automaton);
    }
    
    public int getAutomataCount() {
        return automataMap.size();
    }
    
    public Set<String> getAutomataNames() {
        return automataMap.keySet();
    }
    
    public Automaton getAutomaton(String name) {
        return automataMap.get(name);
    }
    
    public LexicalAnalyzer getLexicalAnalyzer() {
        return lexicalAnalyzer;
    }
}