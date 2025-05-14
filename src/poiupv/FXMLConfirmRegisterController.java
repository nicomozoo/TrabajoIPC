/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author pocky
 */
public class FXMLConfirmRegisterController {
    
    @FXML
    private Button botOk;
    
    private void handleBotOkOnAction(ActionEvent event) {
    // Obtener la ventana actual y ocultarla (no la destruye)
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.hide();
}
}
