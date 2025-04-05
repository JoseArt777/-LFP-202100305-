package com.mycompany.main;

import java.util.*;
import java.util.regex.*;

public class Analyzer {
    private String input;
    private List<Token> tokens;
    private List<String> lexicalErrors;
    private List<String> syntaxErrors;
    private List<MapModel> worlds;
    
    private int position;
    private int line;
    private int column;
    
    // Regular expressions for more precise token matching
    private static final Pattern WORLD_PATTERN = Pattern.compile("^world\\b");
    private static final Pattern PLACE_PATTERN = Pattern.compile("^place\\b");
    private static final Pattern CONNECT_PATTERN = Pattern.compile("^connect\\b");
    private static final Pattern OBJECT_PATTERN = Pattern.compile("^object\\b");
    private static final Pattern AT_PATTERN = Pattern.compile("^at\\b");
    private static final Pattern TO_PATTERN = Pattern.compile("^to\\b");
    private static final Pattern WITH_PATTERN = Pattern.compile("^with\\b");
    
    private static final Pattern STRING_PATTERN = Pattern.compile("^\"([^\"]*)\"");
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?");
    private static final Pattern COORDINATE_PATTERN = Pattern.compile("^\\((-?\\d+),(-?\\d+)\\)");
    
    public Analyzer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
        this.lexicalErrors = new ArrayList<>();
        this.syntaxErrors = new ArrayList<>();
        this.worlds = new ArrayList<>();
        this.position = 0;
        this.line = 1;
        this.column = 1;
    }
    
    public List<Token> lexicalAnalysis() {
        tokens.clear();
        lexicalErrors.clear();
        position = 0;
        line = 1;
        column = 1;
        
        // Trim leading and trailing whitespace
        input = input.trim();
        
        while (position < input.length()) {
            if (skipWhitespace()) {
                continue; // Continue after skipping whitespace instead of breaking
            }
            
            String remainingInput = input.substring(position);
            boolean matched = 
                matchWorldDeclaration(remainingInput) ||
                matchPlaceDeclaration(remainingInput) ||
                matchConnectDeclaration(remainingInput) ||
                matchObjectDeclaration(remainingInput) ||
                matchSpecialChars(remainingInput);
            
            if (!matched) {
                addLexicalError(remainingInput.charAt(0));
                position++;
                column++;
            }
        }
        
        return tokens;
    }
    
    private boolean skipWhitespace() {
        boolean foundWhitespace = false;
        
        while (position < input.length() && Character.isWhitespace(input.charAt(position))) {
            foundWhitespace = true;
            char currentChar = input.charAt(position);
            
            if (currentChar == '\n') {
                line++;
                column = 1;
            } else if (currentChar == '\r') {
                // Handle carriage return, often paired with newline
                if (position + 1 < input.length() && input.charAt(position + 1) == '\n') {
                    position++; // Skip the following newline
                }
                line++;
                column = 1;
            } else if (currentChar == '\t') {
                column += 4; // Assume tab width of 4
            } else {
                column++;
            }
            position++;
        }
        
        return foundWhitespace; // Return if whitespace was found and skipped
    }
    
    private boolean matchWorldDeclaration(String remainingInput) {
        Matcher matcher = WORLD_PATTERN.matcher(remainingInput);
        if (matcher.find()) {
            addToken(Token.Type.WORLD, "world", line, column);
            position += 5;
            column += 5;
            
            if (skipWhitespace()) { /* Skip any whitespace */ }
            
            // Match world name (string)
            matcher = STRING_PATTERN.matcher(input.substring(position));
            if (matcher.find()) {
                String worldName = matcher.group(1);
                addToken(Token.Type.STRING, worldName, line, column);
                position += matcher.group(0).length();
                column += matcher.group(0).length();
                
                if (skipWhitespace()) { /* Skip any whitespace */ }
                
                // Match opening brace
                if (position < input.length() && input.charAt(position) == '{') {
                    addToken(Token.Type.LEFT_BRACE, "{", line, column);
                    position++;
                    column++;
                    return true;
                } else {
                    addLexicalError(position < input.length() ? input.charAt(position) : ' ');
                }
            }
        }
        return false;
    }
    
    private boolean matchPlaceDeclaration(String remainingInput) {
        Matcher matcher = PLACE_PATTERN.matcher(remainingInput);
        if (matcher.find()) {
            addToken(Token.Type.PLACE, "place", line, column);
            position += 5;
            column += 5;
            
            if (skipWhitespace()) { /* Skip any whitespace */ }
            
            // Match place name
            matcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
            if (matcher.find()) {
                String placeName = matcher.group(0);
                addToken(Token.Type.IDENTIFIER, placeName, line, column);
                position += matcher.group(0).length();
                column += matcher.group(0).length();
                
                // Match type separator
                if (position < input.length() && input.charAt(position) == ':') {
                    addToken(Token.Type.COLON, ":", line, column);
                    position++;
                    column++;
                    
                    // Match place type
                    matcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
                    if (matcher.find()) {
                        String placeType = matcher.group(0);
                        addToken(Token.Type.IDENTIFIER, placeType, line, column);
                        position += matcher.group(0).length();
                        column += matcher.group(0).length();
                        
                        if (skipWhitespace()) { /* Skip any whitespace */ }
                        
                        // Match 'at' keyword
                        matcher = AT_PATTERN.matcher(input.substring(position));
                        if (matcher.find()) {
                            addToken(Token.Type.AT, "at", line, column);
                            position += 2;
                            column += 2;
                            
                            if (skipWhitespace()) { /* Skip any whitespace */ }
                            
                            // Match coordinates
                            matcher = COORDINATE_PATTERN.matcher(input.substring(position));
                            if (matcher.find()) {
                                String coordinates = matcher.group(0);
                                addToken(Token.Type.IDENTIFIER, coordinates, line, column);
                                position += matcher.group(0).length();
                                column += matcher.group(0).length();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean matchConnectDeclaration(String remainingInput) {
        Matcher matcher = CONNECT_PATTERN.matcher(remainingInput);
        if (matcher.find()) {
            addToken(Token.Type.CONNECT, "connect", line, column);
            position += 7;
            column += 7;
            
            if (skipWhitespace()) { /* Skip any whitespace */ }
            
            // Match first place name
            matcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
            if (matcher.find()) {
                String fromPlace = matcher.group(0);
                addToken(Token.Type.IDENTIFIER, fromPlace, line, column);
                position += matcher.group(0).length();
                column += matcher.group(0).length();
                
                if (skipWhitespace()) { /* Skip any whitespace */ }
                
                // Match 'to' keyword
                matcher = TO_PATTERN.matcher(input.substring(position));
                if (matcher.find()) {
                    addToken(Token.Type.TO, "to", line, column);
                    position += 2;
                    column += 2;
                    
                    if (skipWhitespace()) { /* Skip any whitespace */ }
                    
                    // Match second place name
                    matcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
                    if (matcher.find()) {
                        String toPlace = matcher.group(0);
                        addToken(Token.Type.IDENTIFIER, toPlace, line, column);
                        position += matcher.group(0).length();
                        column += matcher.group(0).length();
                        
                        if (skipWhitespace()) { /* Skip any whitespace */ }
                        
                        // Match 'with' keyword
                        matcher = WITH_PATTERN.matcher(input.substring(position));
                        if (matcher.find()) {
                            addToken(Token.Type.WITH, "with", line, column);
                            position += 4;
                            column += 4;
                            
                            if (skipWhitespace()) { /* Skip any whitespace */ }
                            
                            // Match connection type (string)
                            matcher = STRING_PATTERN.matcher(input.substring(position));
                            if (matcher.find()) {
                                String connectionType = matcher.group(1);
                                addToken(Token.Type.STRING, connectionType, line, column);
                                position += matcher.group(0).length();
                                column += matcher.group(0).length();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean matchObjectDeclaration(String remainingInput) {
        Matcher matcher = OBJECT_PATTERN.matcher(remainingInput);
        if (matcher.find()) {
            addToken(Token.Type.OBJECT, "object", line, column);
            position += 6;
            column += 6;
            
            if (skipWhitespace()) { /* Skip any whitespace */ }
            
            // Match object name (string)
            matcher = STRING_PATTERN.matcher(input.substring(position));
            if (matcher.find()) {
                String objectName = matcher.group(1);
                addToken(Token.Type.STRING, objectName, line, column);
                position += matcher.group(0).length();
                column += matcher.group(0).length();
                
                // Match type separator
                if (position < input.length() && input.charAt(position) == ':') {
                    addToken(Token.Type.COLON, ":", line, column);
                    position++;
                    column++;
                    
                    // Match object type
                    matcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
                    if (matcher.find()) {
                        String objectType = matcher.group(0);
                        addToken(Token.Type.IDENTIFIER, objectType, line, column);
                        position += matcher.group(0).length();
                        column += matcher.group(0).length();
                        
                        if (skipWhitespace()) { /* Skip any whitespace */ }
                        
                        // Match 'at' keyword
                        matcher = AT_PATTERN.matcher(input.substring(position));
                        if (matcher.find()) {
                            addToken(Token.Type.AT, "at", line, column);
                            position += 2;
                            column += 2;
                            
                            if (skipWhitespace()) { /* Skip any whitespace */ }
                            
                            // Two possible 'at' locations: a place or coordinates
                            Matcher placeMatcher = IDENTIFIER_PATTERN.matcher(input.substring(position));
                            Matcher coordMatcher = COORDINATE_PATTERN.matcher(input.substring(position));
                            
                            if (placeMatcher.find()) {
                                String location = placeMatcher.group(0);
                                addToken(Token.Type.IDENTIFIER, location, line, column);
                                position += placeMatcher.group(0).length();
                                column += placeMatcher.group(0).length();
                                return true;
                            } else if (coordMatcher.find()) {
                                String coordinates = coordMatcher.group(0);
                                addToken(Token.Type.IDENTIFIER, coordinates, line, column);
                                position += coordMatcher.group(0).length();
                                column += coordMatcher.group(0).length();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean matchSpecialChars(String remainingInput) {
        if (remainingInput.isEmpty()) return false;
        
        char currentChar = remainingInput.charAt(0);
        Token.Type type = null;
        
        switch (currentChar) {
            case '{': type = Token.Type.LEFT_BRACE; break;
            case '}': type = Token.Type.RIGHT_BRACE; break;
            case '(': type = Token.Type.LEFT_PAREN; break;
            case ')': type = Token.Type.RIGHT_PAREN; break;
            case ':': type = Token.Type.COLON; break;
            case ',': type = Token.Type.COMMA; break;
            default: return false;
        }
        
        addToken(type, String.valueOf(currentChar), line, column);
        position++;
        column++;
        return true;
    }
    
    private void addToken(Token.Type type, String lexeme, int line, int column) {
        tokens.add(new Token(type, lexeme, line, column));
    }
    
    private void addLexicalError(char errorChar) {
        lexicalErrors.add(String.format("%c|%d|%d", errorChar, line, column));
    }
    
    public List<MapModel> syntacticAnalysis() {
        worlds.clear();
        syntaxErrors.clear();
        
        // Reset position to start parsing tokens
        int tokenIndex = 0;
        
        while (tokenIndex < tokens.size()) {
            // Find world declaration
            if (tokens.get(tokenIndex).getType() == Token.Type.WORLD) {
                ParseResult result = parseWorld(tokenIndex);
                if (result.success && result.model != null) {
                    worlds.add(result.model);
                }
                tokenIndex = result.nextIndex;
            } else {
                tokenIndex++;
            }
        }
        
        return worlds;
    }
    
    // Helper class for parsing results
    private static class ParseResult {
        boolean success;
        MapModel model;
        int nextIndex;
        
        ParseResult(boolean success, MapModel model, int nextIndex) {
            this.success = success;
            this.model = model;
            this.nextIndex = nextIndex;
        }
    }
    
    private ParseResult parseWorld(int startIndex) {
        if (startIndex + 2 >= tokens.size()) {
            syntaxErrors.add("Incomplete world declaration at token " + startIndex);
            return new ParseResult(false, null, startIndex + 1);
        }
        
        // World name is the string after 'world'
        String worldName = tokens.get(startIndex + 1).getLexeme();
        MapModel world = new MapModel(worldName);
        
        int tokenIndex = startIndex + 3; // Skip 'world', name, and '{'
        
        while (tokenIndex < tokens.size()) {
            Token currentToken = tokens.get(tokenIndex);
            
            if (currentToken.getType() == Token.Type.RIGHT_BRACE) {
                // End of world definition
                return new ParseResult(true, world, tokenIndex + 1);
            }
            
            if (currentToken.getType() == Token.Type.PLACE) {
                ParseResult placeResult = parsePlace(world, tokenIndex);
                if (!placeResult.success) {
                    String errorMsg = "Invalid place declaration at line " + 
                                    tokens.get(tokenIndex).getLine() + 
                                    ", column " + tokens.get(tokenIndex).getColumn();
                    syntaxErrors.add(errorMsg);
                }
                tokenIndex = placeResult.nextIndex;
            } else if (currentToken.getType() == Token.Type.CONNECT) {
                ParseResult connectResult = parseConnect(world, tokenIndex);
                if (!connectResult.success) {
                    String errorMsg = "Invalid connection declaration at line " + 
                                    tokens.get(tokenIndex).getLine() + 
                                    ", column " + tokens.get(tokenIndex).getColumn();
                    syntaxErrors.add(errorMsg);
                }
                tokenIndex = connectResult.nextIndex;
            } else if (currentToken.getType() == Token.Type.OBJECT) {
                ParseResult objectResult = parseObject(world, tokenIndex);
                if (!objectResult.success) {
                    String errorMsg = "Invalid object declaration at line " + 
                                    tokens.get(tokenIndex).getLine() + 
                                    ", column " + tokens.get(tokenIndex).getColumn();
                    syntaxErrors.add(errorMsg);
                }
                tokenIndex = objectResult.nextIndex;
            } else {
                // Unexpected token, report error but continue parsing
                syntaxErrors.add("Unexpected token '" + currentToken.getLexeme() + 
                               "' at line " + currentToken.getLine() + 
                               ", column " + currentToken.getColumn());
                tokenIndex++;
            }
        }
        
        // If we got here, we're missing a closing brace
        syntaxErrors.add("Missing closing brace for world '" + worldName + "'");
        return new ParseResult(false, world, tokenIndex);
    }
    
    private ParseResult parsePlace(MapModel world, int startIndex) {
        // Ensure we have enough tokens for a complete place declaration
        if (startIndex + 5 >= tokens.size()) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        // Check expected token types
        if (tokens.get(startIndex + 1).getType() != Token.Type.IDENTIFIER ||
            tokens.get(startIndex + 2).getType() != Token.Type.COLON ||
            tokens.get(startIndex + 3).getType() != Token.Type.IDENTIFIER ||
            tokens.get(startIndex + 4).getType() != Token.Type.AT ||
            tokens.get(startIndex + 5).getType() != Token.Type.IDENTIFIER) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        String placeName = tokens.get(startIndex + 1).getLexeme();
        String placeType = tokens.get(startIndex + 3).getLexeme();
        String coordinates = tokens.get(startIndex + 5).getLexeme();
        
        // Extract x and y from coordinates
        Matcher coordMatcher = COORDINATE_PATTERN.matcher(coordinates);
        if (coordMatcher.matches()) {
            int x = Integer.parseInt(coordMatcher.group(1));
            int y = Integer.parseInt(coordMatcher.group(2));
            
            world.addPlace(placeName, placeType, x, y);
            return new ParseResult(true, null, startIndex + 6);
        }
        
        return new ParseResult(false, null, startIndex + 6);
    }
    
    private ParseResult parseConnect(MapModel world, int startIndex) {
        // Ensure we have enough tokens for a complete connection declaration
        if (startIndex + 6 >= tokens.size()) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        // Check expected token types
        if (tokens.get(startIndex + 1).getType() != Token.Type.IDENTIFIER ||
            tokens.get(startIndex + 2).getType() != Token.Type.TO ||
            tokens.get(startIndex + 3).getType() != Token.Type.IDENTIFIER ||
            tokens.get(startIndex + 4).getType() != Token.Type.WITH ||
            tokens.get(startIndex + 5).getType() != Token.Type.STRING) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        String fromPlace = tokens.get(startIndex + 1).getLexeme();
        String toPlace = tokens.get(startIndex + 3).getLexeme();
        String connectionType = tokens.get(startIndex + 5).getLexeme();
        
        world.addConnection(fromPlace, toPlace, connectionType);
        return new ParseResult(true, null, startIndex + 6);
    }
    
    private ParseResult parseObject(MapModel world, int startIndex) {
        // Ensure we have enough tokens for a complete object declaration
        if (startIndex + 5 >= tokens.size()) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        // Check expected token types
        if (tokens.get(startIndex + 1).getType() != Token.Type.STRING ||
            tokens.get(startIndex + 2).getType() != Token.Type.COLON ||
            tokens.get(startIndex + 3).getType() != Token.Type.IDENTIFIER ||
            tokens.get(startIndex + 4).getType() != Token.Type.AT ||
            tokens.get(startIndex + 5).getType() != Token.Type.IDENTIFIER) {
            return new ParseResult(false, null, startIndex + 1);
        }
        
        String objectName = tokens.get(startIndex + 1).getLexeme();
        String objectType = tokens.get(startIndex + 3).getLexeme();
        String location = tokens.get(startIndex + 5).getLexeme();
        
        // Check if location is coordinates or a place name
        Matcher coordMatcher = COORDINATE_PATTERN.matcher(location);
        if (coordMatcher.matches()) {
            int x = Integer.parseInt(coordMatcher.group(1));
            int y = Integer.parseInt(coordMatcher.group(2));
            world.addObject(objectName, objectType, x, y);
        } else {
            // Assume it's a place name
            world.addObject(objectName, objectType, location);
        }
        
        return new ParseResult(true, null, startIndex + 6);
    }
    
    // Getters
    public List<Token> getTokens() { return tokens; }
    public List<String> getLexicalErrors() { return lexicalErrors; }
    public List<String> getSyntaxErrors() { return syntaxErrors; }
    public List<MapModel> getWorlds() { return worlds; }
}