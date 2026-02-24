package fes.aragon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class InicioController {
    @FXML
    private Button btnArchivo;

    @FXML
    private Button btnIdentificador;

    @FXML
    private Button btnIdentificador3;

    @FXML
    private Button btnCeros;

    @FXML
    private Button btnCerosV3;

    @FXML
    private Button btnAB;

    @FXML
    private Button btnLectura;
    @FXML
    private BorderPane btnPrincipal;

    //@FXML
    //void abrirArchivo(ActionEvent event) {ventana("/fes.aragon/xml/archivo.fxml");}

    //@FXML
    //void abrirLectura(ActionEvent event) {ventana("/fes.aragon/xml/lecturaArchivo.fxml");}

    @FXML
    void abrirIdentificador(ActionEvent event) {
        ventana("/fes.aragon/xml/identificador.fxml");
    }

    @FXML
    void abrirIdentificador3(ActionEvent event) {
        ventana("/fes.aragon/xml/identificadorV3.fxml");
    }

    @FXML
    void abrirCeros(ActionEvent event) {ventana("/fes.aragon/xml/cerosAFD.fxml");}

    @FXML
    void abrirCerosV3(ActionEvent event) {ventana("/fes.aragon/xml/cerosAFDV3.fxml");}

    @FXML
    void abrirAB(ActionEvent event) {ventana("/fes.aragon/xml/aabAFD.fxml");}

    private void ventana(String ruta){
        try {
            Contenido contenido = new Contenido(ruta);
            btnPrincipal.setCenter(contenido);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
