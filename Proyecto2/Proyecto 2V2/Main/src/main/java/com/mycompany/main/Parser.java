package com.mycompany.main;

import java.util.ArrayList;

/**
 * Analizador sintáctico 
 */
public class Parser {
    private ArrayList<Token> tokens;
    private int position;
    private Token currentToken;

    private ArrayList<World> worlds;
    private ArrayList<Token> errors;

    public Parser() {
        this.worlds = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

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

    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        }
    }

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

    private void parseWorlds() {
        while (currentToken.getType() != TokenType.EOF) {
            if (currentToken.getType() == TokenType.WORLD) {
                parseWorld();
            } else {
                if (currentToken.getType() != TokenType.EOF) {
                    errors.add(Token.createError(
                        "Se esperaba 'world', se encontró " + currentToken.getLexeme(),
                        currentToken.getLine(),
                        currentToken.getColumn(),
                        Token.ErrorType.SYNTACTIC
                    ));
                    advance();
                }
            }
        }
        if (!errors.isEmpty() && errors.get(errors.size() - 1).getLexeme().contains("Se esperaba 'world'")) {
            errors.remove(errors.size() - 1);
        }
    }

    private void parseWorld() {
        advance();
        String worldName = "";
        if (currentToken.getType() == TokenType.STRING) {
            worldName = currentToken.getLexeme();
            worldName = worldName.substring(1, worldName.length() - 1);
            advance();
        } else {
            errors.add(Token.createError(
                "Se esperaba un nombre de mundo entre comillas",
                currentToken.getLine(),
                currentToken.getColumn(),
                Token.ErrorType.SYNTACTIC
            ));
        }
        World world = new World(worldName);
        if (consume(TokenType.LBRACE, "Se esperaba '{'")) {
            parseWorldBody(world);
            consume(TokenType.RBRACE, "Se esperaba '}'");
            worlds.add(world);
            if (currentToken.getType() == TokenType.COMMA) {
                advance();
            }
        } else {
            while (currentToken.getType() != TokenType.WORLD && 
                   currentToken.getType() != TokenType.EOF) {
                advance();
            }
        }
    }

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
                errors.add(Token.createError(
                    "Token inesperado en el cuerpo del mundo: " + currentToken.getLexeme(),
                    currentToken.getLine(),
                    currentToken.getColumn(),
                    Token.ErrorType.SYNTACTIC
                ));
                advance();
            }
        }
    }

    private void parsePlaceDecl(World world) {
        advance();
        String placeName = "";

        if (currentToken.getType() == TokenType.IDENTIFIER) {
            placeName = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError("Se esperaba un identificador de lugar", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize();
            return;
        }

        if (!consume(TokenType.COLON, "Se esperaba ':'")) {
            synchronize();
            return;
        }

        String placeType = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            placeType = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError("Se esperaba un tipo de lugar", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize();
            return;
        }

        if (!consume(TokenType.AT, "Se esperaba 'at'")) {
            synchronize();
            return;
        }

        if (!consume(TokenType.LPAREN, "Se esperaba '('")) {
            synchronize();
            return;
        }

        int x = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            x = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            errors.add(Token.createError("Se esperaba un número para X", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize();
            return;
        }

        if (!consume(TokenType.COMMA, "Se esperaba ','")) {
            synchronize();
            return;
        }

        int y = 0;
        if (currentToken.getType() == TokenType.NUMBER) {
            y = Integer.parseInt(currentToken.getLexeme());
            advance();
        } else {
            errors.add(Token.createError("Se esperaba un número para Y", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize();
            return;
        }

        if (!consume(TokenType.RPAREN, "Se esperaba ')'")) {
            synchronize();
            return;
        }

        if (!placeName.isEmpty() && !placeType.isEmpty()) {
            world.addPlace(new Place(placeName, placeType, x, y));
        }
    }

    private void synchronize() {
        while (currentToken.getType() != TokenType.PLACE &&
               currentToken.getType() != TokenType.CONNECT &&
               currentToken.getType() != TokenType.OBJECT &&
               currentToken.getType() != TokenType.RBRACE &&
               currentToken.getType() != TokenType.EOF) {
            advance();
        }
    }
    private void parseConnectionDecl(World world) {
        advance();
        String source = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            source = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError("Se esperaba lugar de origen", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }

        if (!consume(TokenType.TO, "Se esperaba 'to'")) { synchronize(); return; }

        String target = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            target = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError("Se esperaba lugar destino", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }

        if (!consume(TokenType.WITH, "Se esperaba 'with'")) { synchronize(); return; }

        String connection = "";
        if (currentToken.getType() == TokenType.STRING) {
            connection = currentToken.getLexeme().replaceAll("\"", "");
            advance();
        } else {
            errors.add(Token.createError("Se esperaba tipo de conexión entre comillas", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }

        if (!source.isEmpty() && !target.isEmpty() && !connection.isEmpty()) {
            world.addConnection(new Connection(source, target, connection));
        }
    }

    private void parseObjectDecl(World world) {
        advance();
        String name = "";
        if (currentToken.getType() == TokenType.STRING) {
            name = currentToken.getLexeme().substring(1, currentToken.getLexeme().length() - 1);
            advance();
        } else {
            errors.add(Token.createError("Se esperaba nombre del objeto entre comillas", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }

        if (!consume(TokenType.COLON, "Se esperaba ':'")) { synchronize(); return; }

        String type = "";
        if (currentToken.getType() == TokenType.IDENTIFIER) {
            type = currentToken.getLexeme();
            advance();
        } else {
            errors.add(Token.createError("Se esperaba tipo de objeto", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }

        if (!consume(TokenType.AT, "Se esperaba 'at'")) { synchronize(); return; }

        if (currentToken.getType() == TokenType.IDENTIFIER) {
            String place = currentToken.getLexeme();
            advance();
            if (!name.isEmpty() && !type.isEmpty()) {
                world.addObject(new MapObject(name, type, place));
            }
        } else if (currentToken.getType() == TokenType.LPAREN) {
            advance();
            int x = 0, y = 0;
            if (currentToken.getType() == TokenType.NUMBER) {
                x = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                errors.add(Token.createError("Se esperaba número X", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
                synchronize(); return;
            }
            if (!consume(TokenType.COMMA, "Se esperaba ','")) { synchronize(); return; }
            if (currentToken.getType() == TokenType.NUMBER) {
                y = Integer.parseInt(currentToken.getLexeme());
                advance();
            } else {
                errors.add(Token.createError("Se esperaba número Y", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
                synchronize(); return;
            }
            if (!consume(TokenType.RPAREN, "Se esperaba ')'")) { synchronize(); return; }
            if (!name.isEmpty() && !type.isEmpty()) {
                world.addObject(new MapObject(name, type, x, y));
            }
        } else {
            errors.add(Token.createError("Se esperaba identificador o coordenadas", currentToken.getLine(), currentToken.getColumn(), Token.ErrorType.SYNTACTIC));
            synchronize(); return;
        }
    }


    public ArrayList<World> getWorlds() {
        return worlds;
    }

    public ArrayList<Token> getErrors() {
        return errors;
    }
}
