package ra5.eurovision.controlador;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import ra5.eurovision.modelo.Festival;
import ra5.eurovision.modelo.PaisExcepcion;

import java.io.File;
import java.io.IOException;

public class FestivalController {
    Festival festival;

    public FestivalController() {
        festival = new Festival();
    }

    private boolean fichero = false;

    @FXML
    private Button clear;

    @FXML
    private Button btnMostrarGanador; // OK

    @FXML
    private Button btnMostrarPuntos; // OK

    @FXML
    private CheckBox chkGuardarFichero; // OK

    @FXML
    private MenuItem menuLeer; // OK

    @FXML
    private MenuItem salir; //

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtPais;



    @FXML
    void setMenuLeer(){
        FileChooser selector = new FileChooser();
        selector.setTitle("Abrir fichero de datos");
        selector.setInitialDirectory(new File("."));
        selector.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("txt",
                        "*.txt"));
        File f = selector.showOpenDialog(null);
        if (f != null) {
            txtArea.setText("Había " + festival.leerPuntuaciones(f.getAbsolutePath()) + "errores");
            fichero = true;
        }
    }

    @FXML
    void setTxtPais(){
        txtPais.getText();
    }

    @FXML
    void salvarEnFichero() throws IOException {
        if (!fichero){
            txtArea.setText("No se ha cargado el fichero");
        }
        else {
            festival.guardarResultados();
            txtArea.setText("El fichero se ha guardado correctamente");
        }
    }

    @FXML
    void setBtnMostrarGanador(){
        if (!fichero){
            txtArea.setText("No ha cargado el fichero de puntuaciones");
        } else {
            txtArea.setText("El ganador es: " + festival.ganador().toUpperCase());
        }
    }

    @FXML
    void setBtnMostrarPuntos()  {
        if (!fichero){
            txtArea.setText("No ha cargado el fichero de puntuaciones");
        }
        else {
            try {
                txtArea.setText("País: " + txtPais.getText().toUpperCase() +
                        "; Puntos: " + festival.puntuacionDe(txtPais.getText().toUpperCase()));
            } catch (PaisExcepcion e) {
                txtArea.setText(e.getMessage());
            }
        }
    }

    private void cogerFoco() {
        txtPais.requestFocus();
        txtPais.selectAll();
    }

    @FXML
    void clear(ActionEvent event) {
        txtArea.setText("");
        cogerFoco();

    }

    @FXML
    void salir(ActionEvent event) {
        Platform.exit();
    }

}

