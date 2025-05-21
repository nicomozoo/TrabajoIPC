/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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

    
    
     private User currentUser;
    @FXML
    private TableColumn<Session, String> dateCol;
    @FXML
    private TableColumn<Session, Integer> hitsCol;
    @FXML
    private TableColumn<Session, Integer> faultsCol;
    @FXML
    private Button botVolver;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button botBuscar;
    
    private List<Session> todasLasSesiones;
    private ObservableList<Session> sesionesFiltradas = FXCollections.observableArrayList();

    public void setCurrentUser(User user) {
        this.currentUser = user;
        mostrarSesiones();
        todasLasSesiones = currentUser.getSessions(); 
        sesionesFiltradas.setAll(todasLasSesiones);   
        sessionTable.setItems(sesionesFiltradas);
    }

    public void initialize(URL url, ResourceBundle rb) {
        // Vincular columnas con propiedades de la clase Session
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        hitsCol.setCellValueFactory(new PropertyValueFactory<>("hits"));
        faultsCol.setCellValueFactory(new PropertyValueFactory<>("faults"));
        
        dateCol.setCellValueFactory(cellData -> {
        LocalDateTime fecha = cellData.getValue().getTimeStamp();
        String fechaFormateada = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return new SimpleStringProperty(fechaFormateada);
    });
    }

    private void mostrarSesiones() {
        if (currentUser != null) {
            List<Session> sesiones = currentUser.getSessions();
            sessionTable.getItems().setAll(sesiones);
        }
    }

    @FXML
    private void handleBotVolver(ActionEvent event) {
        botVolver.getScene().getWindow().hide();
    }

    @FXML
    private void handleBotBuscar(ActionEvent event) {
         LocalDate desde = datePicker.getValue();

    if (desde != null) {
        sesionesFiltradas.setAll(
            todasLasSesiones.stream()
                .filter(s -> s.getTimeStamp().toLocalDate().isEqual(desde) || s.getTimeStamp().toLocalDate().isAfter(desde))
                .collect(Collectors.toList())
        );
    } else {
        // Si no hay fecha seleccionada, mostrar todas
        sesionesFiltradas.setAll(todasLasSesiones);
    }
    }
}
