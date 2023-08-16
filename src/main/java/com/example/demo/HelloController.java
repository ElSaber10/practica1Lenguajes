package com.example.demo;
import com.example.demo.model.Lexeme;
import com.example.demo.model.Lexical_Analyzer;
import com.example.demo.model.Token;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class HelloController implements Initializable {
    @FXML private MenuBar menubar;
    @FXML private JFXTextArea codeTextArea; //Codigo donde va estar el Area del texto
    @FXML private TextFlow textflow; //Codigo donde va estar el Area del texto
    @FXML private JFXButton btnAnalyze;
    @FXML private JFXButton btnClear;
    @FXML private JFXButton btnGrafic;
    @FXML private TableView<Lexeme> tokensTable; //Tabla donde va presentarse la tabla
    @FXML private TableColumn<Lexeme, Integer> Col;
    @FXML private TableColumn<Lexeme, Integer> lineCol;
    @FXML private TableColumn<Lexeme, Token> tokenCol;
    @FXML private TableColumn<Lexeme, String> atributoCol;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*lineCol.setCellValueFactory(new PropertyValueFactory<>("line"));
        tokenCol.setCellValueFactory(new PropertyValueFactory<>("token"));
        atributoCol.setCellValueFactory(new PropertyValueFactory<>("value"));*/

        lineCol.setCellValueFactory(cellData -> cellData.getValue().lineProperty().asObject());
        tokenCol.setCellValueFactory(cellData -> cellData.getValue().tokenProperty());
        atributoCol.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        Col.setCellValueFactory(cellData -> cellData.getValue().colProperty().asObject());


        btnAnalyze.disableProperty().bind(codeTextArea.textProperty().isEmpty());
        btnClear.disableProperty().bind(codeTextArea.textProperty().isEmpty());
        btnGrafic.disableProperty().bind(codeTextArea.textProperty().isEmpty());
        //tokensTable.getColumns().addAll(lineCol,tokenCol,atributoCol);
    }

    @FXML
    private void loadFile() throws IOException {
        //File file = new File("/home/daniel/Descargas/demo/src/main/resources/testfiles/TestFile.txt");
       FileChooser fileChooser = new FileChooser();
       // fileChooser.setInitialDirectory(file);
       // FileChooser.ExtensionFilter ex = new FileChooser.ExtensionFilter("*.txt");
       // fileChooser.getExtensionFilters().add(ex);
        //listo para usar
        fileChooser.setTitle("Elija el Archivo ");
        String userHome = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(userHome));
        //listo para usar
        File file = fileChooser.showOpenDialog(HelloApplication.stage);
        //File file1 = fileChooser.setInitialDirectory();
        if(file != null){
            Files.lines(file.toPath(), Charset.forName("UTF-8"))
                    .forEach(line -> codeTextArea.appendText(line.concat("\n")));
        }
    }

    @FXML
    private void clearCodeArea(){
        codeTextArea.setText("");
        tokensTable.getItems().clear();
        textflow.getChildren().clear();
    }
    @FXML
    private void graficar() throws IOException {
        //tokensTable.getItems();
        //System.out.println(tokensTable.getSelectionModel().selectedIndexProperty().get());
       /* tokensTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Token nombreSeleccionado = newValue.getToken();
                System.out.println("Fila seleccionada - Nombre: " + nombreSeleccionado);
            }
        });*/
        String palabra="";
        String selectedPerson = tokensTable.getSelectionModel().getSelectedItem().getValue();
        if (selectedPerson != null) {
            palabra = selectedPerson;
            System.out.println("Fila seleccionada - Nombre: " + palabra);
        }

        // Crear nodos con etiquetas
        Node[] nodes = new Node[palabra.length()];
        for (int i = 0; i < palabra.length(); i++) {
            nodes[i] = Factory.node(Label.of(String.valueOf(palabra.charAt(i))));
        }

        // Crear aristas
        Node[] edges = new Node[palabra.length() - 1];
        for (int i = 0; i < palabra.length() - 1; i++) {
            edges[i] = nodes[i].link(nodes[i + 1]);
        }

        // Crear el grafo
        Graph graph = Factory.graph("wordGraph").directed().with(nodes).with(edges);

        // Generar el archivo DOT
        String dotFilePath = "word_graph.dot";
        Graphviz.fromGraph(graph).render(Format.DOT).toFile(new File(dotFilePath));

        // Generar la imagen usando Graphviz
        String outputImagePath = "word_graph.png";
        Graphviz.fromFile(new File(dotFilePath)).render(Format.PNG).toFile(new File(outputImagePath));





        String imageFilePath = "word_graph.png"; // Ruta de la imagen generada
        String htmlFilePath = "graph.html"; // Ruta del archivo HTML a generar

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Grafo Generado</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Grafo Generado</h1>\n" +
                "    <img src=\"" + imageFilePath + "\" alt=\"Grafo\">\n" +
                "</body>\n" +
                "</html>";
/*
        FileWriter writer = new FileWriter(htmlFilePath);
        writer.write(htmlContent);
        writer.close();
        // Abrir el archivo HTML en el navegador predeterminado
        File htmlFile = new File(htmlFilePath);
        writer.write(htmlContent);*/
        try {
            Process process = Runtime.getRuntime().exec("xdg-open " + htmlFilePath); // Cambia por "start" en Windows
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void analyze(){
        String [] lines = codeTextArea.getText().split("\n");
        Map<Integer, String> code = new HashMap<>();
        boolean lock = true; // Para ignorar comentarios de varias líneas

        for(int i = 0; i < lines.length; i++){
            String line = lines[i];
            if(!line.strip().startsWith("#") && lock && !line.strip().startsWith("/*") && !line.isBlank())
                code.put(i+1, line);
            if(line.strip().startsWith("/*"))
                lock = false;
            if(line.strip().endsWith("*/"))
                lock = true;
        }
        //En una lista utilizamos analyzeCode para anilizar el Map code
        List<Lexeme> lexemes = new Lexical_Analyzer().analyzeCode(code);
        for (Lexeme numero : lexemes) {
            System.out.println(numero.getLine() +" " + numero.getToken() + " " + numero.getValue());
        }

        //Colocar con la claser FxCollections en la tabla los lexemas
        tokensTable.setItems(FXCollections.observableList(lexemes));
       // ObservableSet<Lexeme> txt = FXCollections.observableSet((Lexeme) lexemes);
        //tokensTable.setItems((ObservableList<Lexeme>) txt);
        for (Lexeme numero : lexemes) {
            String palabra = numero.getValue() + " ";
            if (numero.getToken().equals(Token.IDENTIFICADOR)){
                Text blacktext = new Text(numero.getValue() + " ");
                blacktext.setFill(Color.BLACK);
                textflow.getChildren().addAll(blacktext);
            }else if (numero.getToken().equals(Token.ARITMETICOS)){
                Text skayblueText = new Text(numero.getValue()+ " ");
                skayblueText.setFill(Color.SKYBLUE);
                textflow.getChildren().addAll(skayblueText);
            }else if (numero.getToken().equals(Token.LOGICOS)){
                Text skayblueText = new Text(numero.getValue()+ " ");
                skayblueText.setFill(Color.SKYBLUE);
                textflow.getChildren().addAll(skayblueText);
            }else if (numero.getToken().equals(Token.ASIGNACION)){
                Text skayblueText = new Text(numero.getValue()+ " ");
                skayblueText.setFill(Color.SKYBLUE);
                textflow.getChildren().addAll(skayblueText);
            }else if (numero.getToken().equals(Token.PALABRA_CLAVE)){
                Text moradotext = new Text(numero.getValue() + " ");
                moradotext.setFill(Color.VIOLET);
                textflow.getChildren().addAll(moradotext);
            }else if (numero.getToken().equals(Token.CONSTANTES)){
                Text rojotext = new Text(numero.getValue() + " ");
                rojotext.setFill(Color.RED);
                textflow.getChildren().addAll(rojotext);
            }else if (numero.getToken().equals(Token.OTROS)){
                Text verdetext = new Text(numero.getValue() + " ");
                verdetext.setFill(Color.GREEN);
                textflow.getChildren().addAll(verdetext);
            }

        }


    }


}