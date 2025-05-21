/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;
import model.Problem;

/**
 * FXML Controller class
 *
 * @author david
 */
public class FXMLPreguntasListaController implements Initializable {

    @FXML
    private Button select, random, cancel;
    
    @FXML
    private ListView list;
    
    private User currentUser;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList items = list.getItems();
        try {
            ArrayList<Problem> al = new ArrayList<>(Navigation.getInstance().getProblems());
            for (Problem p: al) {
                items.add(p.getText());
            }
            list.setItems(items);
        } catch (NavDAOException ex) {
            Logger.getLogger(FXMLPreguntasListaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    public void select(ActionEvent event) throws IOException {
        int index = list.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("Usted no ha seleccionado ninguna pregunta");
            alert.show();
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPreguntas.fxml"));
            Parent root = loader.load();
    
            stage.setScene(new Scene(root));
            stage.setTitle("Pregunta");
            FXMLPreguntasController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.cargarProblema(index);
        }
    }
    
    @FXML
    public void random(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPreguntas.fxml"));
        Parent root = loader.load();
    
        stage.setScene(new Scene(root));
        stage.setTitle("Pregunta");
        FXMLPreguntasController controller = loader.getController();
        controller.setCurrentUser(currentUser);
        controller.barajar();
        controller.cargarProblema(0);
    }
    
    @FXML
    public void cancel(ActionEvent event) {
        currentUser.addSession(PoiUPVApp.getAciertos(), PoiUPVApp.getFallos());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();  // Cierra esta ventana
    }
}
