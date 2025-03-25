package com.mycompany.afd_graph13;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;

public class AnalizadorLexico {
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<Token> errores = new ArrayList<>();
    private int linea = 1;
    private int columna = 0;

    public void analizarArchivo(JFrame frame, JTextArea area, JComboBox<String> selector, HashMap<String, Automata> automatas, ArrayList<String> erroresLexicos) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Archivos .lfp", "lfp"));

        if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        area.setText("");
        automatas.clear();
        erroresLexicos.clear();
        selector.removeAllItems();
        tokens.clear();
        errores.clear();
        linea = 1;
        columna = 0;

        StringBuilder contenido = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String lineaTexto;
            while ((lineaTexto = br.readLine()) != null) {
                contenido.append(lineaTexto).append("\n");
            }
            
            area.setText(contenido.toString());
            
            // Analiza tokens
            analizarTokens(contenido.toString());
            
            // Construye automatas a partir de los tokens
            construirAutomatas(automatas);
            
            // Actualiza el selector de combobox
            for (String nombre : automatas.keySet()) {
                selector.addItem(nombre);
            }
            
            // Convierte errores a formato string para el reporte
            for (Token error : errores) {
                erroresLexicos.add("Error léxico: '" + error.getLexema() + "' en línea " + error.getLinea() + ", columna " + error.getColumna());
            }

            JOptionPane.showMessageDialog(frame, "Archivo analizado correctamente. Total de tokens: " + tokens.size() + ", Errores: " + errores.size());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error al leer el archivo: " + e.getMessage());
        }
    }
    
    private void analizarTokens(String contenido) {
        int estado = 0;
        StringBuilder lexema = new StringBuilder();
        
        for (int i = 0; i < contenido.length(); i++) {
            char c = contenido.charAt(i);
            columna++;
            
            if (c == '\n') {
                linea++;
                columna = 0;
            }
            
            switch (estado) {
                case 0: 
                    if (Character.isWhitespace(c)) {
                    } else if (c == '{') {
                        tokens.add(new Token(TipoToken.LLAVE_ABRE, "{", linea, columna));
                    } else if (c == '}') {
                        tokens.add(new Token(TipoToken.LLAVE_CIERRA, "}", linea, columna));
                    } else if (c == '[') {
                        tokens.add(new Token(TipoToken.CORCHETE_ABRE, "[", linea, columna));
                    } else if (c == ']') {
                        tokens.add(new Token(TipoToken.CORCHETE_CIERRA, "]", linea, columna));
                    } else if (c == '(') {
                        tokens.add(new Token(TipoToken.PARENTESIS_ABRE, "(", linea, columna));
                    } else if (c == ')') {
                        tokens.add(new Token(TipoToken.PARENTESIS_CIERRA, ")", linea, columna));
                    } else if (c == ',') {
                        tokens.add(new Token(TipoToken.COMA, ",", linea, columna));
                    } else if (c == ':') {
                        tokens.add(new Token(TipoToken.DOS_PUNTOS, ":", linea, columna));
                    } else if (c == '=') {
                        tokens.add(new Token(TipoToken.IGUAL, "=", linea, columna));
                    } else if (c == '-') {
                        estado = 1; // Posible flecha
                    } else if (c == '"') {
                        estado = 2; // Inicio de cadena
                    } else if (Character.isLetterOrDigit(c)) {
                        lexema.append(c);
                        estado = 3; // Posible identificador o palabra clave
                    } else {
                        errores.add(new Token(TipoToken.ERROR, String.valueOf(c), linea, columna));
                    }
                    break;
                    
                case 1: 
                    if (c == '>') {
                        tokens.add(new Token(TipoToken.FLECHA, "->", linea, columna-1));
                        estado = 0;
                    } else {
                        errores.add(new Token(TipoToken.ERROR, "-" + c, linea, columna-1));
                        estado = 0;
                        i--; // Retroceder para procesar el carácter actual de nuevo
                    }
                    break;
                    
                case 2: 
                    if (c == '"') {
                        tokens.add(new Token(TipoToken.CADENA, lexema.toString(), linea, columna - lexema.length()));
                        lexema.setLength(0);
                        estado = 0;
                    } else {
                        lexema.append(c);
                    }
                    break;
                    
                case 3: // Identificador o palabra clave
                    if (Character.isLetterOrDigit(c)) {
                        lexema.append(c);
                    } else {
                        String valor = lexema.toString();
                        TipoToken tipo = TipoToken.fromString(valor);
                        
                        if (tipo != null) {
                            tokens.add(new Token(tipo, valor, linea, columna - valor.length()));
                        } else {
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, valor, linea, columna - valor.length()));
                        }
                        
                        lexema.setLength(0);
                        estado = 0;
                        i--; // Retrocede para procesar el carácter actual de nuevo
                    }
                    break;
            }
        }
        
        // Maneja token final si quedó algo en el buffer
        if (estado == 3 && lexema.length() > 0) {
            String valor = lexema.toString();
            TipoToken tipo = TipoToken.fromString(valor);
            
            if (tipo != null) {
                tokens.add(new Token(tipo, valor, linea, columna - valor.length() + 1));
            } else {
                tokens.add(new Token(TipoToken.IDENTIFICADOR, valor, linea, columna - valor.length() + 1));
            }
        }
    }
    
    private void construirAutomatas(HashMap<String, Automata> automatas) {
        Automata automataActual = null;
        String claveActual = "";
        boolean enTransiciones = false;
        String estadoOrigen = "";
        
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            
            // Creación de un nuevo atuomata
            if (token.getTipo() == TipoToken.IDENTIFICADOR && i + 1 < tokens.size() && 
                tokens.get(i + 1).getTipo() == TipoToken.DOS_PUNTOS) {
                automataActual = new Automata(token.getLexema());
                automatas.put(automataActual.getNombre(), automataActual);
                i++; // Saltar los dos puntos
            } 
            // Dentro del autómata
            else if (automataActual != null) {
                // Procesamiento de claves
                if (token.getTipo() == TipoToken.DESCRIPCION || 
                    token.getTipo() == TipoToken.ESTADOS || 
                    token.getTipo() == TipoToken.ALFABETO || 
                    token.getTipo() == TipoToken.INICIAL || 
                    token.getTipo() == TipoToken.FINALES || 
                    token.getTipo() == TipoToken.TRANSICIONES) {
                    
                    claveActual = token.getLexema();
                    enTransiciones = claveActual.equals("transiciones");
                    i++; // Saltar los dos puntos que deben seguir
                }
                // Procesar valores según la clave actual
                else if (!claveActual.isEmpty()) {
                    switch (claveActual) {
                        case "descripcion":
                            if (token.getTipo() == TipoToken.CADENA) {
                                automataActual.setDescripcion(token.getLexema());
                            }
                            break;
                            
                        case "estados":
                            if (token.getTipo() == TipoToken.IDENTIFICADOR) {
                                automataActual.getEstados().add(token.getLexema());
                            }
                            break;
                            
                        case "alfabeto":
                            if (token.getTipo() == TipoToken.CADENA) {
                                automataActual.getAlfabeto().add(token.getLexema());
                            }
                            break;
                            
                        case "inicial":
                            if (token.getTipo() == TipoToken.IDENTIFICADOR) {
                                automataActual.setEstadoInicial(token.getLexema());
                            }
                            break;
                            
                        case "finales":
                            if (token.getTipo() == TipoToken.IDENTIFICADOR) {
                                automataActual.getEstadosFinales().add(token.getLexema());
                            }
                            break;
                            
                        case "transiciones":
                            if (enTransiciones) {
                                if (token.getTipo() == TipoToken.IDENTIFICADOR && estadoOrigen.isEmpty()) {
                                    estadoOrigen = token.getLexema();
                                } 
                                else if (token.getTipo() == TipoToken.CADENA && !estadoOrigen.isEmpty() && i + 2 < tokens.size() && 
                                         tokens.get(i + 1).getTipo() == TipoToken.FLECHA && 
                                         tokens.get(i + 2).getTipo() == TipoToken.IDENTIFICADOR) {
                                    
                                    String simbolo = token.getLexema();
                                    String destino = tokens.get(i + 2).getLexema();
                                    automataActual.agregarTransicion(estadoOrigen, simbolo, destino);
                                    i += 2; // Saltar la flecha y el destino
                                }
                                else if (token.getTipo() == TipoToken.COMA) {
                                    // Continúa con la siguiente transición
                                }
                                else if (token.getTipo() == TipoToken.PARENTESIS_CIERRA) {
                                    estadoOrigen = ""; // Fin de las transiciones para este estado
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
    
    public ArrayList<Token> getTokens() {
        return tokens;
    }
    
    public ArrayList<Token> getErrores() {
        return errores;
    }
}