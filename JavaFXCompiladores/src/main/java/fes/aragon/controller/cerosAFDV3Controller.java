package fes.aragon.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

public class cerosAFDV3Controller implements Initializable {

    @FXML
    private Button idAbrir;

    @FXML
    private Button idCrearArchivo;

    @FXML
    private Button idGuardar;

    @FXML
    private Button idGuardarComo;

    @FXML
    private Button idLimpiar;

    @FXML
    private Button idQuitar;

    @FXML
    private Button idEliminar;

    @FXML
    private Button idValidar;

    @FXML
    private Label lblEstado;

    @FXML
    private Label lblNota;

    @FXML
    private Label lblNota1;

    @FXML
    private Label lblNota11;

    @FXML
    private Label lblNota111;

    @FXML
    private TextArea txtAreaContenido;

    @FXML
    private TextArea txtAreaResultado;

    private File archivoAbierto;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarBotones(false);
    }

    @FXML
    void accionAbrirArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
                //new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage) idAbrir.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoSeleccionado != null) {
            configurarBotones(true);
            idAbrir.setDisable(true);
            idCrearArchivo.setDisable(true);
            this.archivoAbierto = archivoSeleccionado;
            lblEstado.setText("Editando: " + archivoSeleccionado.getName());
            leerContenidoArchivo(archivoSeleccionado);
        }
    }

    @FXML
    void crearArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Crear Archivo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
        );

        Stage stage = (Stage) idCrearArchivo.getScene().getWindow();
        File archivoParaGuardar = fileChooser.showSaveDialog(stage);

        if (archivoParaGuardar != null) {
            try {
                Files.writeString(archivoParaGuardar.toPath(), txtAreaContenido.getText());

                configurarBotones(true);
                idAbrir.setDisable(true);
                idCrearArchivo.setDisable(true);
                lblEstado.setText("Editando: " + archivoParaGuardar.getName());
                this.archivoAbierto = archivoParaGuardar;
                mostrarAlerta("Éxito", "Archivo creado como: " + archivoParaGuardar.getName());
                lblEstado.setText("Archivo creado correctamente.");
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo crear: " + e.getMessage());
                lblEstado.setText("Archivo no creado.");
            }
        }
    }

    @FXML
    void guardarEdicionArchivo(ActionEvent event) {
        if (archivoAbierto != null) {
            try {
                Path ruta = archivoAbierto.toPath();
                String contenido = txtAreaContenido.getText();
                Files.writeString(ruta, contenido);

                lblEstado.setText("Cambios guardados correctamente.");
                mostrarAlerta("Éxito", "Archivo guardado correctamente en " + archivoAbierto.getName());
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo guardar el archivo: " + e.getMessage());
                e.getMessage();
            }
        } else {
            mostrarAlerta("Aviso", "No hay ningún archivo abierto para guardar.");
        }
    }

    @FXML
    void guardarArchivoComo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo Como...");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
        );

        Stage stage = (Stage) idGuardarComo.getScene().getWindow();
        File archivoParaGuardar = fileChooser.showSaveDialog(stage);

        if (archivoParaGuardar != null) {
            try {
                Files.writeString(archivoParaGuardar.toPath(), txtAreaContenido.getText());

                configurarBotones(false);
                lblEstado.setText("Editando: " + archivoParaGuardar.getName());
                this.archivoAbierto = archivoParaGuardar;
                idGuardarComo.setDisable(false);
                idGuardar.setDisable(false);
                mostrarAlerta("Éxito", "Archivo guardado como: " + archivoParaGuardar.getName());
                lblEstado.setText("Cambios guardados correctamente.");
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo guardar: " + e.getMessage());
                lblEstado.setText("NO se pudo guardar el archivo.");
            }
        }
    }

    @FXML
    void limpiarAreaTexto(ActionEvent event) {
        txtAreaContenido.clear();
        lblEstado.setText("Nuevo archivo sin título.");
    }

    @FXML
    void quitarArchivo(ActionEvent event) {
        configurarBotones(false);
        idAbrir.setDisable(false);
        idCrearArchivo.setDisable(false);
        txtAreaContenido.clear();
        archivoAbierto = null;
        lblEstado.setText("Nuevo archivo sin título.");
    }

    @FXML
    void eliminarArchivo(ActionEvent event) {
        if (archivoAbierto != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar Archivo");
            confirmacion.setHeaderText("Estas seguro de eliminar " + archivoAbierto.getName() + "?");
            confirmacion.setContentText("Esta accion no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    Files.delete(archivoAbierto.toPath());
                    mostrarAlerta("Eliminado", "El archivo" + archivoAbierto.getName() + "ha sido eliminado del disco.");
                    configurarBotones(false);
                    idAbrir.setDisable(false);
                    idCrearArchivo.setDisable(false);
                    limpiarAreaTexto(event);
                } catch (IOException e) {
                    mostrarAlerta("Error", "No se pudo eliminar el archivo: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void validarIdentificadorCeros(ActionEvent event){
        String texto = txtAreaContenido.getText();

        if (texto == null || texto.trim().isEmpty()) {
            mostrarAlerta("Aviso", "No hay texto para validar. Escribe algo o carga un archivo");
            return;
        }

        //separamos el texto usando espacios o saltos de línea como delimitador, \\s+ significa uno o más espacios, tabuladores o saltos de línea
        //StringBuilder es mutable, String es inmutable. Nos ayauda con el metodo append, para hacer cadenas modificables
        String[] palabras = texto.split("\\s+");
        StringBuilder resultados = new StringBuilder();

        for (String palabra : palabras) {
            try{
                if (!palabra.isEmpty()) {
                    identificadorValido(palabra);
                    resultados.append(palabra).append(" ----------> [VALIDO]\n");
                }
            }catch (Exception exception){
                resultados.append(palabra).append(" ----------> [INVALIDO]\n");
            }
        }
        txtAreaResultado.setText(resultados.toString());
    }

    //PSEUDOCODIGO VERSION 3
    private void identificadorValido(String cadena) throws Exception {
        int estado = 0;
        int contador = 0;
        int entrada = 0;
        int[][] tabla = {
                {1, 5, 0}, //q0
                {2, 5, 0}, //q1
                {2, 3,-1}, //q2 aceptacion
                {4, 3, 0}, //q3
                {2, 3, 0}, //q4
                {5, 5, 0}  //qm muerto
        };

        do {
            if(contador == cadena.length()){
                entrada = 2; //fin de cadena
            } else {
                char simbolo = cadena.charAt(contador);
                if (simbolo == '0') {
                    entrada = 0;
                } else if (simbolo == '1') {
                    entrada = 1;
                } else {
                    throw new Exception("Error: El alfabeto solo permite 0 y 1. Simbolo invalido:  " + simbolo);
                }
            } contador++;
            estado = tabla[estado][entrada];

            if (estado == 5) {
                throw new Exception("Error, la cadena no empieza con 00");
            }

            if (entrada == 2 && estado == 0) {
                throw new Exception("Error: La cadena no termina con '00'.");
            }
        } while (estado != tabla[2][2]);
    }

    private void leerContenidoArchivo(File archivo) {
        try {
            Path ruta = archivo.toPath();
            String contenido = Files.readString(ruta);
            txtAreaContenido.setText(contenido);
        } catch (IOException e) {
            txtAreaContenido.setText("Error al leer el archivo: " + e.getMessage());
            e.getMessage();
        }
    }

    private void configurarBotones(boolean hayArchivoAbierto) {
        idGuardar.setDisable(!hayArchivoAbierto);
        idGuardarComo.setDisable(!hayArchivoAbierto);
        idQuitar.setDisable(!hayArchivoAbierto);
        idLimpiar.setDisable(!hayArchivoAbierto);
        idEliminar.setDisable(!hayArchivoAbierto);
        //txtAreaContenido.setDisable(!hayArchivoAbierto);
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
