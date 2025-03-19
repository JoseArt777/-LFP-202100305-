/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */
public class Transition {
    private final State fromState;
    private final String symbol;
    private final State toState;

    public Transition(State fromState, String symbol, State toState) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toState = toState;
    }

    public State getFromState() {
        return fromState;
    }

    public String getSymbol() {
        return symbol;
    }

    public State getToState() {
        return toState;
    }

    @Override
    public String toString() {
        return fromState + " --" + symbol + "--> " + toState;
    }
}