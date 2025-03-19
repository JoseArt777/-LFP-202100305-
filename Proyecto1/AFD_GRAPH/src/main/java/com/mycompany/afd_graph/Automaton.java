/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */
import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Automaton {
    private String name;
    private String description;
    private List<State> states;
    private List<String> alphabet;
    private State initialState;
    private List<State> finalStates;
    private Map<State, List<Transition>> transitions;

    public Automaton(String name) {
        this.name = name;
        this.states = new ArrayList<>();
        this.alphabet = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.transitions = new HashMap<>();
    }

    public void addState(State state) {
        if (!states.contains(state)) {
            states.add(state);
            transitions.put(state, new ArrayList<>());
        }
    }

    public void addTransition(State fromState, String symbol, State toState) {
        if (!states.contains(fromState) || !states.contains(toState)) {
            throw new IllegalArgumentException("Estados no existen en el autómata");
        }
        
        if (!alphabet.contains(symbol)) {
            throw new IllegalArgumentException("Símbolo no pertenece al alfabeto");
        }
        
        Transition transition = new Transition(fromState, symbol, toState);
        transitions.get(fromState).add(transition);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = alphabet;
    }

    public State getInitialState() {
        return initialState;
    }

    public void setInitialState(State initialState) {
        if (!states.contains(initialState)) {
            throw new IllegalArgumentException("Estado inicial no existe en el autómata");
        }
        this.initialState = initialState;
    }

    public List<State> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(List<State> finalStates) {
        for (State state : finalStates) {
            if (!states.contains(state)) {
                throw new IllegalArgumentException("Estado final no existe en el autómata");
            }
        }
        this.finalStates = finalStates;
    }

    public Map<State, List<Transition>> getTransitions() {
        return transitions;
    }

    public List<Transition> getTransitionsFrom(State state) {
        return transitions.getOrDefault(state, new ArrayList<>());
    }

    @Override
    public String toString() {
        return name;
    }
}