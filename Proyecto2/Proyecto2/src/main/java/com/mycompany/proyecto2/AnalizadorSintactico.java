/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto2;

/**
 *
 * @author iosea
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Analizador sintáctico para el lenguaje de mapas narrativos
 */
public class AnalizadorSintactico {
    private List<Token> tokens;
    private int posicionActual;
    private Token tokenActual;
    private List<ErrorCompilacion> errores;
    private List<Mundo> mundos;
    
    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        this.posicionActual = 0;
        this.errores = new ArrayList<>();
        this.mundos = new ArrayList<>();
        
        if (!tokens.isEmpty()) {
            this.tokenActual = tokens.get(0);
        }
    }
    
    /**
     * Avanza al siguiente token
     */
    private void avanzar() {
        posicionActual++;
        if (posicionActual < tokens.size()) {
            tokenActual = tokens.get(posicionActual);
        }
    }
    
    /**
     * Verifica si el token actual es del tipo esperado y avanza
     * @param tipo Tipo de token esperado
     * @return true si coincide, false en caso contrario
     */
    private boolean coincidir(Token.Tipo tipo) {
        if (tokenActual.getTipo() == tipo) {
            avanzar();
            return true;
        }
        return false;
    }
    
    /**
     * Espera que el token actual sea del tipo especificado y avanza
     * Si no coincide, reporta un error
     * @param tipo Tipo de token esperado
     * @param mensaje Mensaje de error
     */
    private void consumir(Token.Tipo tipo, String mensaje) {
        if (tokenActual.getTipo() == tipo) {
            avanzar();
        } else {
            error(mensaje);
        }
    }
    
    /**
     * Reporta un error sintáctico
     * @param mensaje Mensaje de error
     */
    private void error(String mensaje) {
        errores.add(new ErrorCompilacion(ErrorCompilacion.Tipo.SINTACTICO, 
                mensaje, tokenActual.getLexema(), tokenActual.getLinea(), tokenActual.getColumna()));
    }
    
    /**
     * Inicia el análisis sintáctico
     * @return Lista de mundos encontrados
     */
    public List<Mundo> analizar() {
        mundos.clear();
        errores.clear();
        posicionActual = 0;
        
        if (!tokens.isEmpty()) {
            tokenActual = tokens.get(0);
        }
        
        programa();
        return mundos;
    }
    
    /**
     * Regla inicial de la gramática: programa -> mundo { mundo }
     */
    private void programa() {
        if (tokenActual.getTipo() == Token.Tipo.PALABRA_RESERVADA_WORLD) {
            mundo();
            
            // Puede haber más mundos separados por comas
            while (tokenActual.getTipo() == Token.Tipo.COMA) {
                avanzar(); // Consumir la coma
                if (tokenActual.getTipo() == Token.Tipo.PALABRA_RESERVADA_WORLD) {
                    mundo();
                } else {
                    error("Se esperaba la palabra reservada 'world' después de ','");
                    recuperacionPanico();
                }
            }
        } else {
            error("Se esperaba la palabra reservada 'world'");
        }
    }
    
    /**
     * Regla para un mundo: 'world' STRING '{' declaraciones '}'
     */
    private void mundo() {
        // Debe comenzar con 'world'
        if (coincidir(Token.Tipo.PALABRA_RESERVADA_WORLD)) {
            // Nombre del mundo (cadena)
            if (tokenActual.getTipo() == Token.Tipo.CADENA) {
                String nombreMundo = tokenActual.getLexema();
                // Quitar las comillas del nombre
                if (nombreMundo.length() >= 2) {
                    nombreMundo = nombreMundo.substring(1, nombreMundo.length() - 1);
                }
                
                Mundo nuevoMundo = new Mundo(nombreMundo);
                avanzar(); // Consumir el nombre
                
                // Abrir llave
                if (coincidir(Token.Tipo.LLAVE_APERTURA)) {
                    // Analizar declaraciones dentro del mundo
                    declaraciones(nuevoMundo);
                    
                    // Cerrar llave
                    if (coincidir(Token.Tipo.LLAVE_CIERRE)) {
                        mundos.add(nuevoMundo);
                    } else {
                        error("Se esperaba '}'");
                        recuperacionPanico();
                    }
                } else {
                    error("Se esperaba '{'");
                    recuperacionPanico();
                }
            } else {
                error("Se esperaba el nombre del mundo (cadena entre comillas)");
                recuperacionPanico();
            }
        }
    }
    
    /**
     * Analiza las declaraciones dentro de un mundo
     * @param mundo Mundo actual
     */
    private void declaraciones(Mundo mundo) {
        while (tokenActual.getTipo() != Token.Tipo.LLAVE_CIERRE && 
               tokenActual.getTipo() != Token.Tipo.FIN_ARCHIVO) {
            
            switch (tokenActual.getTipo()) {
                case PALABRA_RESERVADA_PLACE:
                    lugar(mundo);
                    break;
                case PALABRA_RESERVADA_CONNECT:
                    conexion(mundo);
                    break;
                case PALABRA_RESERVADA_OBJECT:
                    objeto(mundo);
                    break;
                default:
                    error("Se esperaba 'place', 'connect' u 'object'");
                    avanzar(); // Saltar token no esperado
                    break;
            }
        }
    }
    
    /**
     * Analiza una declaración de lugar: 'place' ID ':' ID 'at' '(' NUM ',' NUM ')'
     * @param mundo Mundo actual
     */
    private void lugar(Mundo mundo) {
        if (coincidir(Token.Tipo.PALABRA_RESERVADA_PLACE)) {
            if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                String nombreLugar = tokenActual.getLexema();
                avanzar();
                
                if (coincidir(Token.Tipo.DOS_PUNTOS)) {
                    if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                        String tipoLugar = tokenActual.getLexema();
                        avanzar();
                        
                        if (coincidir(Token.Tipo.PALABRA_RESERVADA_AT)) {
                            if (coincidir(Token.Tipo.PARENTESIS_APERTURA)) {
                                // Coordenada X
                                if (tokenActual.getTipo() == Token.Tipo.NUMERO) {
                                    int x = Integer.parseInt(tokenActual.getLexema());
                                    avanzar();
                                    
                                    if (coincidir(Token.Tipo.COMA)) {
                                        // Coordenada Y
                                        if (tokenActual.getTipo() == Token.Tipo.NUMERO) {
                                            int y = Integer.parseInt(tokenActual.getLexema());
                                            avanzar();
                                            
                                            if (coincidir(Token.Tipo.PARENTESIS_CIERRE)) {
                                                // Crear y agregar el lugar al mundo
                                                Lugar lugar = new Lugar(nombreLugar, tipoLugar, x, y);
                                                mundo.agregarLugar(lugar);
                                            } else {
                                                error("Se esperaba ')'");
                                            }
                                        } else {
                                            error("Se esperaba un número para la coordenada Y");
                                            avanzar();
                                        }
                                    } else {
                                        error("Se esperaba ','");
                                    }
                                } else {
                                    error("Se esperaba un número para la coordenada X");
                                    avanzar();
                                }
                            } else {
                                error("Se esperaba '('");
                            }
                        } else {
                            error("Se esperaba 'at'");
                        }
                    } else {
                        error("Se esperaba un identificador para el tipo de lugar");
                        avanzar();
                    }
                } else {
                    error("Se esperaba ':'");
                }
            } else {
                error("Se esperaba un identificador para el nombre del lugar");
                avanzar();
            }
        }
    }
    
    /**
     * Analiza una declaración de conexión: 'connect' ID 'to' ID 'with' STRING
     * @param mundo Mundo actual
     */
    private void conexion(Mundo mundo) {
        if (coincidir(Token.Tipo.PALABRA_RESERVADA_CONNECT)) {
            if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                String lugarOrigen = tokenActual.getLexema();
                avanzar();
                
                if (coincidir(Token.Tipo.PALABRA_RESERVADA_TO)) {
                    if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                        String lugarDestino = tokenActual.getLexema();
                        avanzar();
                        
                        if (coincidir(Token.Tipo.PALABRA_RESERVADA_WITH)) {
                            if (tokenActual.getTipo() == Token.Tipo.CADENA) {
                                String tipoCamino = tokenActual.getLexema();
                                // Quitar las comillas
                                if (tipoCamino.length() >= 2) {
                                    tipoCamino = tipoCamino.substring(1, tipoCamino.length() - 1);
                                }
                                avanzar();
                                
                                // Crear y agregar la conexión al mundo
                                Conexion conexion = new Conexion(lugarOrigen, lugarDestino, tipoCamino);
                                mundo.agregarConexion(conexion);
                            } else {
                                error("Se esperaba una cadena para el tipo de camino");
                                avanzar();
                            }
                        } else {
                            error("Se esperaba 'with'");
                        }
                    } else {
                        error("Se esperaba un identificador para el lugar destino");
                        avanzar();
                    }
                } else {
                    error("Se esperaba 'to'");
                }
            } else {
                error("Se esperaba un identificador para el lugar origen");
                avanzar();
            }
        }
    }
    
    /**
     * Analiza una declaración de objeto: 'object' STRING ':' ID 'at' (ID | '(' NUM ',' NUM ')')
     * @param mundo Mundo actual
     */
    private void objeto(Mundo mundo) {
        if (coincidir(Token.Tipo.PALABRA_RESERVADA_OBJECT)) {
            if (tokenActual.getTipo() == Token.Tipo.CADENA) {
                String nombreObjeto = tokenActual.getLexema();
                // Quitar las comillas
                if (nombreObjeto.length() >= 2) {
                    nombreObjeto = nombreObjeto.substring(1, nombreObjeto.length() - 1);
                }
                avanzar();
                
                if (coincidir(Token.Tipo.DOS_PUNTOS)) {
                    if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                        String tipoObjeto = tokenActual.getLexema();
                        avanzar();
                        
                        if (coincidir(Token.Tipo.PALABRA_RESERVADA_AT)) {
                            // Dos opciones: ubicar en un lugar o en coordenadas
                            if (tokenActual.getTipo() == Token.Tipo.IDENTIFICADOR) {
                                // Objeto en un lugar específico
                                String lugarAsociado = tokenActual.getLexema();
                                avanzar();
                                
                                // Crear y agregar objeto al mundo (asociado a un lugar)
                                Objeto objeto = new Objeto(nombreObjeto, tipoObjeto, lugarAsociado);
                                mundo.agregarObjeto(objeto);
                            } else if (coincidir(Token.Tipo.PARENTESIS_APERTURA)) {
                                // Objeto en coordenadas específicas
                                if (tokenActual.getTipo() == Token.Tipo.NUMERO) {
                                    int x = Integer.parseInt(tokenActual.getLexema());
                                    avanzar();
                                    
                                    if (coincidir(Token.Tipo.COMA)) {
                                        if (tokenActual.getTipo() == Token.Tipo.NUMERO) {
                                            int y = Integer.parseInt(tokenActual.getLexema());
                                            avanzar();
                                            
                                            if (coincidir(Token.Tipo.PARENTESIS_CIERRE)) {
                                                // Crear y agregar objeto al mundo (con coordenadas)
                                                Objeto objeto = new Objeto(nombreObjeto, tipoObjeto, x, y);
                                                mundo.agregarObjeto(objeto);
                                            } else {
                                                error("Se esperaba ')'");
                                            }
                                        } else {
                                            error("Se esperaba un número para la coordenada Y");
                                            avanzar();
                                        }
                                    } else {
                                        error("Se esperaba ','");
                                    }
                                } else {
                                    error("Se esperaba un número para la coordenada X");
                                    avanzar();
                                }
                            } else {
                                error("Se esperaba un identificador de lugar o '('");
                                avanzar();
                            }
                        } else {
                            error("Se esperaba 'at'");
                        }
                    } else {
                        error("Se esperaba un identificador para el tipo de objeto");
                        avanzar();
                    }
                } else {
                    error("Se esperaba ':'");
                }
            } else {
                error("Se esperaba una cadena para el nombre del objeto");
                avanzar();
            }
        }
    }
    
    /**
     * Método de recuperación de errores (modo pánico)
     * Avanza hasta encontrar un punto de sincronización
     */
    private void recuperacionPanico() {
        while (tokenActual.getTipo() != Token.Tipo.LLAVE_CIERRE && 
               tokenActual.getTipo() != Token.Tipo.PALABRA_RESERVADA_PLACE && 
               tokenActual.getTipo() != Token.Tipo.PALABRA_RESERVADA_CONNECT && 
               tokenActual.getTipo() != Token.Tipo.PALABRA_RESERVADA_OBJECT && 
               tokenActual.getTipo() != Token.Tipo.FIN_ARCHIVO) {
            avanzar();
        }
    }
    
    // Getters
    public List<ErrorCompilacion> getErrores() {
        return errores;
    }
    
    public List<Mundo> getMundos() {
        return mundos;
    }
}