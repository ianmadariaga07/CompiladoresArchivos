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
    private Button btnLectura;
    @FXML
    private BorderPane btnPrincipal;

    @FXML
    void abrirArchivo(ActionEvent event) {
        ventana("/fes.aragon/xml/archivo.fxml");
    }

    @FXML
    void abrirLectura(ActionEvent event) {
        ventana("/fes.aragon/xml/lecturaArchivo.fxml");
    }

    @FXML
    void accionIdentificador(ActionEvent event) {
        ventana("/fes.aragon/xml/identificador.fxml");
    }

    private void ventana(String ruta){
        try {
            Contenido contenido = new Contenido(ruta);
            btnPrincipal.setCenter(contenido);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
