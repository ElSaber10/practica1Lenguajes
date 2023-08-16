package com.example.demo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexical_Analyzer {

    private Map<String, Token> keywordsAndOperatorsMap;
    private Integer col = 1;
    List<String> identificadorlexemes4 = new ArrayList<String>();
    public Lexical_Analyzer() {
        this.keywordsAndOperatorsMap = new HashMap<>();

        keywordsAndOperatorsMap.put("for", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("and", Token.LOGICOS);
        keywordsAndOperatorsMap.put("or", Token.LOGICOS);
        keywordsAndOperatorsMap.put("not", Token.LOGICOS);
        keywordsAndOperatorsMap.put("as", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("assert", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("break", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("class", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("continue", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("def", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("del", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("elif", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("else", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("except", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("False", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("finaly", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("from", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("global", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("in", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("is", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("lambda", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("None", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("nonlocal", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("not", Token.LOGICOS);
        keywordsAndOperatorsMap.put("or", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("pass", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("raise", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("return", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("true", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("try", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("with", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("yield", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("while", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("do", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("if", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("else", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("print", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("switch", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("case", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("default", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("null", Token.PALABRA_CLAVE);
        keywordsAndOperatorsMap.put("true", Token.CONSTANTES);
        keywordsAndOperatorsMap.put("false", Token.CONSTANTES);
        keywordsAndOperatorsMap.put("+", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("-", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("*", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("**", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("/", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("%", Token.ARITMETICOS);
        keywordsAndOperatorsMap.put("..", Token.OTROS);
        keywordsAndOperatorsMap.put(".", Token.PUTNO);
        keywordsAndOperatorsMap.put(",", Token.OTROS);
        keywordsAndOperatorsMap.put("=", Token.ASIGNACION);
        keywordsAndOperatorsMap.put(":", Token.OTROS);
        keywordsAndOperatorsMap.put(";", Token.OTROS);
        keywordsAndOperatorsMap.put("(", Token.OTROS);
        keywordsAndOperatorsMap.put(")", Token.OTROS);
        keywordsAndOperatorsMap.put(">=", Token.COMPARACION);
        keywordsAndOperatorsMap.put("<=", Token.COMPARACION);
        keywordsAndOperatorsMap.put(">", Token.COMPARACION);
        keywordsAndOperatorsMap.put("<", Token.COMPARACION);
        keywordsAndOperatorsMap.put("!<", Token.COMPARACION);
        keywordsAndOperatorsMap.put("==", Token.COMPARACION);
        keywordsAndOperatorsMap.put("@", Token.OTROS);
        keywordsAndOperatorsMap.put("{", Token.OTROS);
        keywordsAndOperatorsMap.put("}", Token.OTROS);
    }

    public List<Lexeme> analyzeCode(Map<Integer, String> lines) {

        //Creamos una Lista para agregar los lexemas encontrados
        //Con la expresion lambda
        List<Lexeme> lexemes2 = new ArrayList<>();
        lines.forEach((nLine, line) -> {
            Map<String, Token> lexLine = analyseLine(line);
            lexLine.forEach((value, token) -> lexemes2.add(new Lexeme(token, value, nLine, col++)));
        });
        /*for (Lexeme numero : lexemes2) {
            System.out.println(numero.getToken());
        }*/
            return lexemes2;
    }

    //analiza la linea que manda el analyzeCode
    private Map<String, Token> analyseLine(String line) {
        //Creamos un Mao para las lineas que recibimos del analyzeCode
        Map<String, Token> lineTokens = new HashMap<>();
        Automaton automaton = new Automaton();
        //Iteramos para encontrar los espacios y verificar palabra por palabra
        for (String str : line.split(" ")) {
            //System.out.println(lineTokens);
            if (keywordsAndOperatorsMap.containsKey(str.toLowerCase())) {
                lineTokens.put(str, keywordsAndOperatorsMap.get(str.toLowerCase()));
                col=1;
            }
            else
                lineTokens.put(str, automaton.evaluate(str));
        }
        //Retorna los tokens conforme va evaluando
        return lineTokens;
    }




    //PRIMERO TENEMOS QUE BUSCAR LOS IDENTIFICADORES
    private Map<String, Token> analyzeNormal(String line) {
        String data = line.trim();
        //Creamos un Mao para las lineas que recibimos del analyzeCode
        Map<String, Token> lineTokens = new HashMap<>();
        Automaton automaton = new Automaton();
        //Iteramos para versi hay espacios
        for (int str = 0; str < data.length(); str++) {
            {
                //System.out.println(lineTokens);
                if (identificador(data.charAt(str)))
                    lineTokens.put(String.valueOf(data.charAt(str)), keywordsAndOperatorsMap.get(data.toLowerCase()));
                else
                    lineTokens.put(String.valueOf(str), automaton.evaluate(line));
                //System.out.println(lineTokens);
            }
        }
        //Retorna los tokens conforme va evaluando
        return lineTokens;
    }
    //para mostrar pero no lo uso
    public void mostrar (){
        for (String numero : identificadorlexemes4) {
            System.out.println(numero.toString());
        }
    }

    public boolean identificador(char entry) {
        if ((entry >= 'A' && entry <= 'Z') || (entry >= 'a' && entry <= 'z' || entry == 95)) {
            identificadorlexemes4.add(String.valueOf(entry));
            if (entry == '\n') {
                analyseLine(identificadorlexemes4.toString());
                 col = 0;
            }
            System.out.println("es una letra " + entry);
            return true;
        }else
            System.out.println("no es letra");
            identificadorlexemes4.add(" ");
         return false;

    }

}
