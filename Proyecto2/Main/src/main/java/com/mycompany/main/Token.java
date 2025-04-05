package com.mycompany.main;

public class Token {
    public enum Type {
        WORLD, PLACE, CONNECT, OBJECT, AT, TO, WITH,
        IDENTIFIER, STRING, NUMBER, 
        LEFT_BRACE, RIGHT_BRACE, LEFT_PAREN, RIGHT_PAREN,
        COLON, COMMA, UNKNOWN
    }
    
    private Type type;
    private String lexeme;
    private int line;
    private int column;
    
    public Token(Type type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }
    
    // Getters
    public Type getType() { return type; }
    public String getLexeme() { return lexeme; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    
    @Override
    public String toString() {
        return type + ": " + lexeme + " at line " + line + ", column " + column;
    }
}