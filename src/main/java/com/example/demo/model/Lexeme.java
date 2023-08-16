package com.example.demo.model;

import javafx.beans.property.*;

public class Lexeme {

    private ObjectProperty<Token> token = new SimpleObjectProperty<>();
    private IntegerProperty line = new SimpleIntegerProperty();
    private IntegerProperty col = new SimpleIntegerProperty();
    private StringProperty value = new SimpleStringProperty();

    public Lexeme(Token token, String value, Integer line, Integer col){
        setToken(token);
        setValue(value);
        setLine(line);
        setCol(col);
    }

    public int getCol() {
        return col.get();
    }

    public IntegerProperty colProperty() {
        return col;
    }

    public void setCol(int col) {
        this.col.set(col);
    }

    public Token getToken() {
        return token.get();
    }

    public ObjectProperty<Token> tokenProperty() {
        return token;
    }

    public void setToken(Token token) {
        this.token.set(token);
    }

    public int getLine() {
        return line.get();
    }

    public IntegerProperty lineProperty() {
        return line;
    }

    public void setLine(int line) {
        this.line.set(line);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
