package fes.aragon.controller;

import fes.aragon.modelo.*;
import fes.aragon.token.TokensMike;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class InicioController implements Initializable {
    @FXML private ToggleGroup grupoAFD;

    @FXML private Button idCargarAuto;
    @FXML private Button idAbrir;
    @FXML private Button idCrearArchivo;
    @FXML private Button idEliminar;
    @FXML private Button idGuardar;
    @FXML private Button idGuardarComo;
    @FXML private Button idLimpiar;
    @FXML private Button idQuitar;

    @FXML private Label lblEstado;

    @FXML private TextArea txtAreaContenido;
    @FXML private TextArea txtAreaResultado;

    @FXML private TableView<TokenModelo> tablaTokens;
    @FXML private TableColumn<TokenModelo, String> colToken;
    @FXML private TableColumn<TokenModelo, String> colLexema;
    @FXML private TableColumn<TokenModelo, Integer> colLinea;
    @FXML private TableColumn<TokenModelo, Integer> colColumna;

    private File archivoAbierto;
    private AutomataConfigurable automataConfigurable = new AutomataConfigurable();
    private final Map<String, String[]> infoAutomatas = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarBotones(false);

        colToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        colLexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        colLinea.setCellValueFactory(new PropertyValueFactory<>("linea"));
        colColumna.setCellValueFactory(new PropertyValueFactory<>("columna"));

        tablaTokens.setVisible(false);

        infoAutomatas.put("1. MIKE#", new String[]{"Analizador Léxico Mike#", "Desglosa el código fuente en tokens."});
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

        //nuevo mike#
        if(tipoValidacion.equals("1. MIKE#")){
            tablaTokens.setVisible(true);
            txtAreaResultado.setVisible(false);
            try {
                lexicoMike(texto);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error en el analizador léxico: " + e.getMessage());
            }
            return;
        } else {
            tablaTokens.setVisible(false);
            txtAreaResultado.setVisible(true);
        }
        //separamos el texto usando espacios o saltos de línea como delimitador, \\s+ significa uno o más espacios, tabuladores o saltos de línea
        //StringBuilder es mutable, String es inmutable. Nos ayauda con el metodo append, para hacer cadenas modificables
        String[] palabras = texto.split("\\s+");
        StringBuilder resultados = new StringBuilder();

        for(String palabra : palabras){
            if (!palabra.trim().isEmpty()){
                try{
                    switch (tipoValidacion){
                        case "1. MIKE#":
                            lexicoMike(palabra);
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

    private void lexicoMike(String cadena) throws Exception{
        Reader rd=new BufferedReader(new StringReader(cadena));
        LexicoMike lexico = new LexicoMike(rd);
        TokensMike resultado;

        ObservableList<TokenModelo> listaTokens = FXCollections.observableArrayList();

        do {
            resultado = lexico.yylex();
            if (resultado != null) {
                listaTokens.add(new TokenModelo(
                        resultado.toString(),
                        lexico.lexema,
                        lexico.getYyline(),
                        lexico.getYycolumn()
                ));

                if (resultado == TokensMike.ERROR) {
                    mostrarAlerta("Error Léxico", "Símbolo no reconocido: '" + lexico.lexema +
                            "' en la línea " + lexico.getYyline() +
                            " columna " + lexico.getYycolumn());
                    break;
                }
            }
        } while (resultado != null);

        tablaTokens.setItems(listaTokens);
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

