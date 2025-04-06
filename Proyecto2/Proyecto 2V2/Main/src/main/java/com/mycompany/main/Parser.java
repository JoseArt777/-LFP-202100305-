package com.mycompany.main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author iosea
 */

import java.util.ArrayList;

/**
 * Analizador sintáctico para el lenguaje de mapas narrativos.
 */
public class Parser {
    private ArrayList<Token> tokens;
    private int position;
    private Token currentToken;
    
    private ArrayList<World> worlds;
    private ArrayList<Token> errors;
    
    /**
     * Constructor por defecto.
     */
    public Parser() {
        this.worlds = new ArrayList<>();
        this.errors = new ArrayList<>();
    }
    
    /**
     * Realiza el análisis sintáctico de los tokens.
     * 
     * @param tokens Lista de tokens a analizar
     */
    public void parse(ArrayList<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.worlds = new ArrayList<>();
        this.errors = new ArrayList<>();
        
        if (!tokens.isEmpty()) {
            currentToken = tokens.get(0);
            parseWorlds();
        }
    }
    
    /**
     * Avanza al siguiente token.
     */
    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        }
    }
    
    /**
     * Consume un token si es del tipo esperado, de lo contrario registra un error.
     * 
     * @param expectedType Tipo de token esperado
     * @param errorMessage Mensaje de error si no coincide
     * @return true si se consumió correctamente, false en caso contrario
     */
    private boolean consume(TokenType expectedType, String errorMessage) {
    if (currentToken.getType() == expectedType) {
        advance();
        return true;
    } else {
        errors.add(Token.createError(
            errorMessage + ", se encontró " + currentToken.getLexeme(),
            currentToken.getLine(),
            currentToken.getColumn(),
            Token.ErrorType.SYNTACTIC
        ));
        return false;
    }
}
    
    /**
     * Punto de entrada para el análisis de mundos.
     * Gramática: worlds -> world { world_body }, { world_body } ...
     */
    private void parseWorlds() {
    while (currentToken.getType() != TokenType.EOF) {
        if (currentToken.getType() == TokenType.WORLD) {
            parseWorld();
        } else {
            // Error: se esperaba WORLD, pero no es un error si es EOF
            if (currentToken.getType() != TokenType.EOF) {
                errors.add(Token.createError(
                    "Se esperaba 'world', se encontró " + currentToken.getLexeme(),
                    currentToken.getLine(),
                    currentToken.getColumn()
                ));
                advance(); // Recuperación: avanzar y continuar
            }
        }
    }
    
    // Eliminar cualquier error falso al final del archivo
    if (!errors.isEmpty() && errors.get(errors.size() - 1).getLexeme().contains("Se esperaba 'world'")) {
        errors.remove(errors.size() - 1);
    }
}
    
    /**
     * Analiza un mundo.
     * Gramática: world -> "world" STRING "{" place_list connection_list object_list "}"
     */
    private void parseWorld() {
        // Consumir "world"
        advance();
        
        // Obtener el nombre del mundo
        String worldName = "";
        if (currentToken.getType() == TokenType.STRING) {
            worldName = currentToken.getLexeme();
            // Quitar las comillas
            worldName = worldName.substring(1, worldName.length() - 1);
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un nombre de mundo entre comillas",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
        }
        
        World world = new World(worldName);
        
        // Consumir "{"
        if (consume(TokenType.LBRACE, "Se esperaba '{'")) {
            // Analizar el cuerpo del mundo
            parseWorldBody(world);
            
            // Consumir "}"
            consume(TokenType.RBRACE, "Se esperaba '}'");
            
            // Agregar el mundo a la lista
            worlds.add(world);
            
            // Si hay una coma, avanzar
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
            }
        } else {
            // Error: recuperación hasta el siguiente mundo o EOF
            while (currentToken.getType() != TokenType.WORLD && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
    }
    
    /**
     * Analiza el cuerpo de un mundo.
     * Gramática: world_body -> place_decl* connection_decl* object_decl*
     * 
     * @param world Mundo al que se agregarán los elementos
     */
    private void parseWorldBody(World world) {
        while (currentToken.getType() != TokenType.RBRACE && 
               currentToken.getType() != TokenType.EOF) {
            
            if (currentToken.getType() == TokenType.PLACE) {
                parsePlaceDecl(world);
            } else if (currentToken.getType() == TokenType.CONNECT) {
                parseConnectionDecl(world);
            } else if (currentToken.getType() == TokenType.OBJECT) {
                parseObjectDecl(world);
            } else {
                // Error: token inesperado
                errors.add(Token.createError(
                    "Token inesperado en el cuerpo del mundo: " + currentToken.getLexeme(),
                    currentToken.getLine(),
                    currentToken.getColumn()
                ));
                advance(); // Recuperación: avanzar y continuar
            }
        }
    }
    
    /**
     * Analiza una declaración de lugar.
     * Gramática: place_decl -> "place" IDENTIFIER ":" IDENTIFIER "at" "(" NUMBER "," NUMBER ")"
     * 
     * @param world Mundo al que se agregará el lugar
     */
    private void parsePlaceDecl(World world) {
        // Consumir "place"
        advance();
        
        // Obtener el nombre del lugar
        String placeName = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            placeName = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un identificador de lugar",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir ":"
        if (!consume(TokenType.COLON, "Se esperaba ':'")) {
            // Recuperación: buscar "at"
            while (currentToken.getType() != TokenType.AT && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener el tipo de lugar
        String placeType = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            placeType = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un tipo de lugar",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir "at"
        if (!consume(TokenType.AT, "Se esperaba 'at'")) {
            // Recuperación: buscar "("
            while (currentToken.getType() != TokenType.LPAREN && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Consumir "("
        if (!consume(TokenType.LPAREN, "Se esperaba '('")) {
            // Recuperación: buscar el número o ","
            while (currentToken.getType() != TokenType.NUMBER && 
                   currentToken.getType() != TokenType.COMMA && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener coordenada X
        int x = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            x = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un número para la coordenada X",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
        }
        
        // Consumir ","
        if (!consume(TokenType.COMMA, "Se esperaba ','")) {
            // Recuperación: buscar el número o ")"
            while (currentToken.getType() != TokenType.NUMBER && 
                   currentToken.getType() != TokenType.RPAREN && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener coordenada Y
        int y = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            y = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un número para la coordenada Y",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
        }
        
        // Consumir ")"
        consume(TokenType.RPAREN, "Se esperaba ')'");
        
        // Agregar el lugar al mundo
        if (!placeName.isEmpty() && !placeType.isEmpty()) {
            world.addPlace(new Place(placeName, placeType, x, y));
        }
    }
    
    /**
     * Analiza una declaración de conexión.
     * Gramática: connection_decl -> "connect" IDENTIFIER "to" IDENTIFIER "with" STRING
     * 
     * @param world Mundo al que se agregará la conexión
     */
    private void parseConnectionDecl(World world) {
        // Consumir "connect"
        advance();
        
        // Obtener el lugar de origen
        String sourcePlace = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            sourcePlace = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un identificador de lugar origen",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir "to"
        if (!consume(TokenType.TO, "Se esperaba 'to'")) {
            // Recuperación: buscar el lugar destino
            while (currentToken.getType() != TokenType.IDENTIFIER && 
                   currentToken.getType() != TokenType.WITH && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener el lugar de destino
        String targetPlace = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            targetPlace = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un identificador de lugar destino",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir "with"
        if (!consume(TokenType.WITH, "Se esperaba 'with'")) {
            // Recuperación: buscar el tipo de conexión
            while (currentToken.getType() != TokenType.STRING && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener el tipo de conexión
        String connectionType = "";
        if (currentToken.getType() == TokenType.STRING) {
            connectionType = currentToken.getLexeme();
            // Quitar las comillas
            connectionType = connectionType.substring(1, connectionType.length() - 1);
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un tipo de conexión entre comillas",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Agregar la conexión al mundo
        if (!sourcePlace.isEmpty() && !targetPlace.isEmpty() && !connectionType.isEmpty()) {
            world.addConnection(new Connection(sourcePlace, targetPlace, connectionType));
        }
    }
    
    /**
     * Analiza una declaración de objeto.
     * Gramática: 
     *   object_decl -> "object" STRING ":" IDENTIFIER "at" IDENTIFIER
     *   object_decl -> "object" STRING ":" IDENTIFIER "at" "(" NUMBER "," NUMBER ")"
     * 
     * @param world Mundo al que se agregará el objeto
     */
    private void parseObjectDecl(World world) {
        // Consumir "object"
        advance();
        
        // Obtener el nombre del objeto
        String objectName = "";
        if (currentToken.getType() == TokenType.STRING) {
            objectName = currentToken.getLexeme();
            // Quitar las comillas
            objectName = objectName.substring(1, objectName.length() - 1);
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un nombre de objeto entre comillas",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir ":"
        if (!consume(TokenType.COLON, "Se esperaba ':'")) {
            // Recuperación: buscar el tipo de objeto
            while (currentToken.getType() != TokenType.IDENTIFIER && 
                   currentToken.getType() != TokenType.AT && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Obtener el tipo de objeto
        String objectType = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            objectType = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un tipo de objeto",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            advance();
        }
        
        // Consumir "at"
        if (!consume(TokenType.AT, "Se esperaba 'at'")) {
            // Recuperación: buscar la ubicación
            while (currentToken.getType() != TokenType.IDENTIFIER && 
                   currentToken.getType() != TokenType.LPAREN && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
        
        // Determinar si el objeto está en un lugar o en coordenadas
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            // Objeto en un lugar
            String placeId = currentToken.getLexeme();
            advance();
            
            // Agregar el objeto al mundo
            if (!objectName.isEmpty() && !objectType.isEmpty()) {
                world.addObject(new MapObject(objectName, objectType, placeId));
            }
        } else if (currentToken.getType() == TokenType.LPAREN) {
            // Objeto en coordenadas
            advance();
            
            // Obtener coordenada X
            int x = 0;
            if (currentToken.getType() == TokenType.NUMBER) {
                x = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                errors.add(Token.createError(
                    "Se esperaba un número para la coordenada X",
                    currentToken.getLine(),
                    currentToken.getColumn()
                ));
                advance();
            }
            
            // Consumir ","
            if (!consume(TokenType.COMMA, "Se esperaba ','")) {
                // Recuperación: buscar el número o ")"
                while (currentToken.getType() != TokenType.NUMBER && 
                       currentToken.getType() != TokenType.RPAREN && 
                       currentToken.getType() != TokenType.RBRACE && 
                       currentToken.getType() != TokenType.EOF) {
                    advance();
                }
            }
            
            // Obtener coordenada Y
            int y = 0;
            if (currentToken.getType() == TokenType.NUMBER) {
                y = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                errors.add(Token.createError(
                    "Se esperaba un número para la coordenada Y",
                    currentToken.getLine(),
                    currentToken.getColumn()
                ));
            }
            
            // Consumir ")"
            consume(TokenType.RPAREN, "Se esperaba ')'");
            
            // Agregar el objeto al mundo
            if (!objectName.isEmpty() && !objectType.isEmpty()) {
                world.addObject(new MapObject(objectName, objectType, x, y));
            }
        } else {
            // Error: ubicación inválida
            errors.add(Token.createError(
                "Se esperaba un identificador de lugar o coordenadas",
                currentToken.getLine(),
                currentToken.getColumn()
            ));
            
            // Recuperación: avanzar hasta el siguiente token válido
            while (currentToken.getType() != TokenType.PLACE && 
                   currentToken.getType() != TokenType.CONNECT && 
                   currentToken.getType() != TokenType.OBJECT && 
                   currentToken.getType() != TokenType.RBRACE && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
    }
    
    /**
     * Obtiene la lista de mundos analizados.
     * 
     * @return Lista de mundos
     */
    public ArrayList<World> getWorlds() {
        return worlds;
    }
    
    /**
     * Obtiene la lista de errores sintácticos.
     * 
     * @return Lista de errores
     */
    public ArrayList<Token> getErrors() {
        return errors;
    }}