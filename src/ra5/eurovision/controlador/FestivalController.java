package ra5.eurovision.controlador;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ra5.eurovision.modelo.Festival;
import ra5.eurovision.modelo.PaisExcepcion;

import java.io.File;
import java.io.IOException;

public class FestivalController {

    private Festival festival;

    boolean leido=false;

    @FXML
    private TextArea areaTxt;

    @FXML
    private Button btnMostrarPuntos;

    @FXML
    private TextField txtField;

    @FXML
    private Button btnMostrarGanador;

    @FXML
    private CheckBox checkGuardar;

    @FXML
    private Button btnClear;

    public FestivalController() {
        festival=new Festival();
    }


    @FXML
    void mostrarPuntos(ActionEvent event) {
        if (!leido){
            areaTxt.setText("Lee el fichero");
        }else {
            if (txtField.getText().isBlank()){
                areaTxt.setText("Teclee país");
            }else {
                String pais = txtField.getText().toUpperCase().trim();
                try {
                    if (checkGuardar.isSelected()){
                        areaTxt.setText("Pais: " + pais + "\nPuntos: " + festival.puntuacionDe(pais) + "\nGUARDADO EN FICHERO");
                        festival.guardarResultados();
                    }
                    else {
                        areaTxt.setText("Pais: " + pais + "\nPuntos: " + festival.puntuacionDe(pais));
                    }
                } catch (PaisExcepcion e) {
                    areaTxt.setText("No existe el pais " + pais);
                    cogerFoco();
                } catch (IOException e) {
                    areaTxt.setText("No se peuede leer el archivo: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    void mostrarGanador(ActionEvent event) {
        if (!leido){
            areaTxt.setText("Lee el fichero");
        }else {
            if (checkGuardar.isSelected()){
                areaTxt.setText("El ganador es: " + festival.ganador() + "\nGUARDADO EN FICHERO");
            }
            else {
                areaTxt.setText("El ganador es: " + festival.ganador());
            }
        }
    }

    @FXML
    void leerVotaciones(ActionEvent event) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Abrir fichero de datos");
        selector.setInitialDirectory(new File("."));
        selector.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("txt",
                        "*.txt"));
        File f = selector.showOpenDialog(null);
        if (f != null) {
            areaTxt.setText("Había " + festival.leerPuntuaciones(f.getAbsolutePath()) + " errores");
            leido=true;
        }
    }

    @FXML
    void salir(ActionEvent event) {
        Platform.exit();
    }



    private void cogerFoco() {
        txtField.requestFocus();
        txtField.selectAll();

    }

    @FXML
    void clear(ActionEvent event) {
        areaTxt.setText("");
        cogerFoco();

    }

}

