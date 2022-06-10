package ra5.eurovision.controlador;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ra5.eurovision.modelo.Festival;
import ra5.eurovision.modelo.PaisExcepcion;

import java.io.File;
import java.io.IOException;

//Antonio
public class FestivalController {
    private boolean importado;
    @FXML
    private CheckBox checkBox;

    @FXML
    private TextArea txtArea;

    @FXML
    private TextField txtField;

    private Festival fest;

    public FestivalController() {
        fest = new Festival();
        importado = false;
    }
    @FXML
    void leerVotaciones(ActionEvent event){
        FileChooser selector = new FileChooser();
        selector.setTitle("Abrir fichero de datos");
        selector.setInitialDirectory(new File("."));
        selector.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("puntuaciones",
                        "*.txt"));
        File f = selector.showOpenDialog(null);
        if (f != null) {
            int errores = fest.leerPuntuaciones(f.getAbsolutePath());
            txtArea.setText("Los errores son" + errores);
            importado = true;
        }
    }

    @FXML
    void mostrarPuntosPais(ActionEvent event){
        if(importado){
            try {
                txtArea.setText(fest.puntuacionDe(txtField.getText())+"");
            } catch (PaisExcepcion e) {
                txtArea.setText(e.getMessage());
            }
        }
        else{
            txtArea.setText("Hay que hacer la importacion previamente");
        }

    }
    @FXML
    void mostrarGanador(ActionEvent event){
        if(importado){
            txtArea.setText("El ganador es " + fest.ganador());
            if(checkBox.isSelected()){
                try {
                    fest.guardarResultados();
                    txtArea.appendText("\nGuardado resultados en fichero");
                } catch (IOException e) {
                    txtArea.setText("Error al escribir en fichero");
                }
            }
        }
        else{
            txtArea.setText("Hay que hacer la importacion previamente");
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
        txtArea.setText("");
        cogerFoco();

    }

}

