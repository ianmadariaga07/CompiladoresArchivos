package fes.aragon.controller;

import fes.aragon.modelo.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

public class InicioController implements Initializable {
    @FXML
    private BorderPane btnPrincipal;

    @FXML
    private ToggleGroup grupoAFD;

    @FXML
    private Button idCargarAuto;

    @FXML
    private Button idAbrir;

    @FXML
    private Button idCrearArchivo;

    @FXML
    private Button idEliminar;

    @FXML
    private Button idGuardar;

    @FXML
    private Button idGuardarComo;

    @FXML
    private Button idLimpiar;

    @FXML
    private Button idQuitar;

    @FXML
    private Label lblEstado;

    @FXML
    private TextArea txtAreaContenido;

    @FXML
    private TextArea txtAreaResultado;

    private File archivoAbierto;

    private AutomataConfigurable automataConfigurable = new AutomataConfigurable();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarBotones(false);
        idCargarAuto.setDisable(true);

        grupoAFD.selectedToggleProperty().addListener((observable, viejoToggle, nuevoToggle) -> {
            if (nuevoToggle != null) {
                ToggleButton botonSeleccionado = (ToggleButton) nuevoToggle;
                if (!botonSeleccionado.getText().equals("AB AUTO")) {
                    idCargarAuto.setDisable(true);
                }
            }
        });
    }

    @FXML
    void accionIdentificador(ActionEvent event){
        String texto = txtAreaContenido.getText();
        Toggle toggle = grupoAFD.getSelectedToggle();

        if(texto == null || texto.trim().isEmpty()){
            mostrarAlerta("Aviso", "No hay texto para validar. Escribe algo o carga un archivo");
            return;
        }

        if(toggle == null){
            mostrarAlerta("Aviso", "Debes seleccionar un tipo de autómata (AFD) antes de validar");
            return;
        }

        ToggleButton botonSeleccionado = (ToggleButton) toggle;
        String tipoValidacion = botonSeleccionado.getText();

        //separamos el texto usando espacios o saltos de línea como delimitador, \\s+ significa uno o más espacios, tabuladores o saltos de línea
        //StringBuilder es mutable, String es inmutable. Nos ayauda con el metodo append, para hacer cadenas modificables
        String[] palabras = texto.split("\\s+");
        StringBuilder resultados = new StringBuilder();

        for(String palabra : palabras){
            if (!palabra.trim().isEmpty()){
                try{
                    switch (tipoValidacion){
                        case "CEROS v1":
                            identificadorCerosV1(palabra);
                            break;
                        case "AA v3":
                            identificadorSubcadenaAA(palabra);
                            break;
                        case "ID v1":
                            identificadorVariablesV1(palabra);
                            break;
                        case "ID v3":
                            identificadorVariablesV3(palabra);
                            break;
                        case "CEROS v3":
                            identificadorCerosV3(palabra);
                            break;
                        case "EXP REG":
                            identificadorExpresionRegularV2(palabra);
                            break;
                        case "NOTA CIENT.":
                            identificadorNotacionCientifica(palabra);
                            break;
                        case "AB AUTO":
                            identificadorAutoconfigurable(palabra);
                            break;
                        default:
                            throw new Exception("Opción no reconocida");
                    }
                    //identificadorValido(palabra);
                    resultados.append(palabra).append(" ----------> [VALIDO]\n");
                }catch (Exception exception){
                    resultados.append(palabra).append(" ----------> [INVALIDO]\n");
                }
            }
        }
        txtAreaResultado.setText(resultados.toString());
    }

    private void identificadorCerosV1(String cadena) throws Exception{
        AutomataCeros cerosV1 = new AutomataCeros(cadena);
        cerosV1.validarV1();
    }

    private void identificadorSubcadenaAA(String cadena) throws Exception{
        AutomataSubcadenaAA subcadenaAA =  new AutomataSubcadenaAA(cadena);
        subcadenaAA.validar();
    }

    private void identificadorVariablesV1(String cadena) throws Exception{
        IdentificadorVariables identificador = new IdentificadorVariables(cadena);
        identificador.validarV1();
    }

    private void identificadorVariablesV3(String cadena) throws Exception{
        IdentificadorVariables identificador = new IdentificadorVariables(cadena);
        identificador.validarV3();
    }

    private void identificadorCerosV3(String cadena) throws Exception{
        AutomataCeros cerosV3 = new AutomataCeros(cadena);
        cerosV3.validarV3();
    }

    private void identificadorExpresionRegularV2(String cadena) throws Exception{
        ExpresionRegularV2 validador = new ExpresionRegularV2(cadena);
        validador.validar();
    }

    private void identificadorNotacionCientifica(String cadena) throws Exception{
        NotacionCientifica notacionCientifica = new NotacionCientifica(cadena);
        notacionCientifica.validar();
    }

    private void identificadorAutoconfigurable(String cadena) throws Exception{
        if(automataConfigurable.isConfigurado()) {
            automataConfigurable.validar(cadena);
        } else {
            throw new Exception("");
        }
    }

    @FXML
    void accionSeleccionarAutoconfi(ActionEvent event) {
        idCargarAuto.setDisable(false);

        mostrarAlerta(
                "Instrucciones Autoconfigurable",
                "Formato de la Matriz de Transiciones:\n\n" +
                        "Fila 1: [Núm. Estados],[Núm. Columnas]\n" +
                        "Fila 2: [Alfabeto separado por comas, ej: a,b,er]\n" +
                        "Fila 3 en adelante: [Estado Destino 1],[Estado Destino 2],[Es Final? 0 o 1]\n\n" +
                        "Una vez escrito, presiona el botón 'Cargar Config.' para guardarlo en memoria."
        );
    }

    @FXML
    void accionCargarAuto(ActionEvent event){
        String texto = txtAreaContenido.getText();

        if(texto == null || texto.trim().isEmpty()){
            mostrarAlerta("Aviso", "No hay texto para cargar. Escribe la configuración primero.");
            return;
        }
        try{
            automataConfigurable.cargarConfiguracion(texto);
            txtAreaContenido.setText("");
            mostrarAlerta("ÉXITO", "Configuracion cargada de manera exitosa en memoria");
        }catch (Exception exception){
            mostrarAlerta("Error de Formato", "Hubo un error al leer la matriz. Revisa los espacios y los numeros.");
        }
    }

    @FXML
    void accionAbrirArchivo(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
                //new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        Stage stage = (Stage) idAbrir.getScene().getWindow();
        File archivoSeleccionado = fileChooser.showOpenDialog(stage);

        if(archivoSeleccionado != null){
            configurarBotones(true);
            idAbrir.setDisable(true);
            idCrearArchivo.setDisable(true);
            this.archivoAbierto = archivoSeleccionado;
            lblEstado.setText("Editando: " + archivoSeleccionado.getName());
            leerContenidoArchivo(archivoSeleccionado);
        }
    }

    @FXML
    void crearArchivo(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Crear Archivo");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
        );

        Stage stage = (Stage) idCrearArchivo.getScene().getWindow();
        File archivoParaGuardar = fileChooser.showSaveDialog(stage);

        if(archivoParaGuardar != null){
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
    void eliminarArchivo(ActionEvent event){
        if(archivoAbierto != null){
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar Archivo");
            confirmacion.setHeaderText("Estas seguro de eliminar " + archivoAbierto.getName() + "?");
            confirmacion.setContentText("Esta accion no se puede deshacer.");

            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if(resultado.isPresent() && resultado.get() == ButtonType.OK){
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
    void guardarArchivoComo(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo Como...");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Texto", "*.txt", "*.java", "*.cpp", "*.xml", "*.c")
        );

        Stage stage = (Stage) idGuardarComo.getScene().getWindow();
        File archivoParaGuardar = fileChooser.showSaveDialog(stage);

        if(archivoParaGuardar != null){
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
    void guardarEdicionArchivo(ActionEvent event){
        if(archivoAbierto != null){
            try {
                Path ruta = archivoAbierto.toPath();
                String contenido = txtAreaContenido.getText();
                Files.writeString(ruta, contenido);

                lblEstado.setText("Cambios guardados correctamente.");
                mostrarAlerta("Éxito", "Archivo guardado correctamente en " + archivoAbierto.getName());
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo guardar el archivo: " + e.getMessage());
            }
        }else{
            mostrarAlerta("Aviso", "No hay ningún archivo abierto para guardar.");
        }
    }

    @FXML
    void limpiarAreaTexto(ActionEvent event){
        txtAreaContenido.clear();
        lblEstado.setText("Nuevo archivo sin título.");
    }

    @FXML
    void quitarArchivo(ActionEvent event){
        configurarBotones(false);
        idAbrir.setDisable(false);
        idCrearArchivo.setDisable(false);
        txtAreaContenido.clear();
        archivoAbierto = null;
        lblEstado.setText("Nuevo archivo sin título.");
    }

    private void leerContenidoArchivo(File archivo){
        try {
            Path ruta = archivo.toPath();
            String contenido = Files.readString(ruta);
            txtAreaContenido.setText(contenido);
        } catch (IOException e) {
            txtAreaContenido.setText("Error al leer el archivo: " + e.getMessage());
        }
    }

    private void configurarBotones(boolean hayArchivoAbierto){
        idGuardar.setDisable(!hayArchivoAbierto);
        idGuardarComo.setDisable(!hayArchivoAbierto);
        idQuitar.setDisable(!hayArchivoAbierto);
        idLimpiar.setDisable(!hayArchivoAbierto);
        idEliminar.setDisable(!hayArchivoAbierto);
        //txtAreaContenido.setDisable(!hayArchivoAbierto);
    }

    private void mostrarAlerta(String titulo, String mensaje){
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}

