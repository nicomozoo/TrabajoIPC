/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Session;
import model.User;

/**
 *
 * @author pocky
 */
public class FXMLSesionesController implements Initializable{

    @FXML
    private TableView<Session> sessionTable;
    private TableColumn<Session,String> fechaCol;
    private TableColumn<Session,Integer> aciertosCol;
    private TableColumn<Session,Integer> fallosCol;
    
    
     private User currentUser;
    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private TableColumn<?, ?> hitsCol;
    @FXML
    private TableColumn<?, ?> faultsCol;
    @FXML
    private Button botVolver;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        mostrarSesiones();
    }

    public void initialize(URL url, ResourceBundle rb) {
        // Vincular columnas con propiedades de la clase Session
        fechaCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        aciertosCol.setCellValueFactory(new PropertyValueFactory<>("hits"));
        fallosCol.setCellValueFactory(new PropertyValueFactory<>("faults"));
    }

    private void mostrarSesiones() {
        if (currentUser != null) {
            List<Session> sesiones = currentUser.getSessions();
            sessionTable.getItems().setAll(sesiones);
        }
    }

    @FXML
    private void handleBotVolver(ActionEvent event) {
        try {
            // Cargar la nueva interfaz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mapa");
            stage.show();

            // (Opcional) Cerrar la ventana actual
            // ((Node)(event.getSource())).getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
