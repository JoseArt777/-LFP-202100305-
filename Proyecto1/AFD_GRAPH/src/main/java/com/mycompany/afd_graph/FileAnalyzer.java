/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.afd_graph;

/**
 *
 * @author iosea
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class FileAnalyzer {
    private HashMap<String, Automaton> automataMap;

    public FileAnalyzer() {
        this.automataMap = new HashMap<>();
    }

    public void analyzeFile(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse the line and construct automata
                // Example: parseAutomaton(line);
            }
        }
    }

    public HashMap<String, Automaton> getAutomataMap() {
        return automataMap;
    }

    // Method to parse automaton from file line
    private void parseAutomaton(String line) {
        // Parse logic here
    }
}