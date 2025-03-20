/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Automaton {

    private Set<State> states;
    private Map<State, Set<Transition>> transitions;
    private State initialState;
    private Set<State> finalStates;
    private Set<String> alphabet;
    private String name;
    private String description;

    public Automaton(String name) {
        this.name = name;
        this.states = new HashSet<>();
        this.transitions = new HashMap<>();
        this.finalStates = new HashSet<>();
        this.alphabet = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAlphabet(List<String> alphabet) {
        this.alphabet = new HashSet<>(alphabet);
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public void setFinalStates(List<State> finalStates) {
        this.finalStates = new HashSet<>(finalStates);
    }

    public Set<State> getFinalStates() {
        return finalStates;
    }

    public void addState(State state) {
        states.add(state);
    }

    public void addTransition(State fromState, String symbol, State toState) {
        Transition transition = new Transition(fromState, symbol, toState);
        transitions.computeIfAbsent(fromState, k -> new HashSet<>()).add(transition);
    }

    public List<Transition> getTransitionsFrom(State fromState) {
        return new ArrayList<>(transitions.getOrDefault(fromState, new HashSet<>()));
    }

    public void setInitialState(State state) {
        initialState = state;
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Transition> getTransitionsFromState(State state) {
        return transitions.getOrDefault(state, new HashSet<>());
    }

    public State getInitialState() {
        return initialState;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Automaton:\n");
        sb.append("States:\n");
        for (State state : states) {
            sb.append(state).append("\n");
        }
        sb.append("Initial State:\n").append(initialState).append("\n");
        sb.append("Final States:\n");
        for (State state : finalStates) {
            sb.append(state).append("\n");
        }
        sb.append("Transitions:\n");
        for (Map.Entry<State, Set<Transition>> entry : transitions.entrySet()) {
            for (Transition transition : entry.getValue()) {
                sb.append(transition).append("\n");
            }
        }
        return sb.toString();
    }
    
    public String getTransition(String state, String symbol) {
        for (Transition transition : transitions.getOrDefault(new State(state), new HashSet<>())) {
            if (transition.getSymbol().equals(symbol)) {
                return transition.getToState().getName();
            }
        }
        return null;
    }
}