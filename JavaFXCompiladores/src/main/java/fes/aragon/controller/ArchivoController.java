package fes.aragon.controller;

import fes.aragon.modelo.Archivos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ArchivoController implements Initializable{
    @FXML
    private TableColumn<Archivos, String> clmNombre;

    @FXML
    private TableColumn<Archivos, String> clmRuta;

    @FXML
    private Button idAbrir;

    @FXML
    private TableView<Archivos> tblTabla;

    private ObservableList<Archivos> listaGeneral;

    @FXML
    void accionAbrirDirectorio(ActionEvent event) {
        DirectoryChooser fileChooser = new DirectoryChooser();
        File f = fileChooser.showDialog(idAbrir.getScene().getWindow());
        if (f != null) {
            listaGeneral = listaArchivos(f.getAbsoluteFile());
            tblTabla.setItems(listaGeneral);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.clmRuta.setCellValueFactory(new PropertyValueFactory<>("ruta"));
        this.clmNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    }
    private ObservableList<Archivos> listaArchivos(File f) {
        ObservableList<Archivos> lista = FXCollections.observableArrayList();
        ArrayList<File> directorios = new ArrayList<>();
        directorios.add(f);
        while(!directorios.isEmpty()){
            File actual = directorios.remove(0);
            if (actual.listFiles() != null) {
                for (File dato:actual.listFiles()){
                    if (dato.isDirectory()) {
                        directorios.add(dato.getAbsoluteFile());
                    }else{
                        Archivos archivos = new Archivos(dato.getAbsolutePath(),dato.getName());
                        lista.add(archivos);
                    }
                }

            }
        }
        return lista;
    }
}
