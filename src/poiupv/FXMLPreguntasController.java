/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLPreguntasController {

    @FXML
    public Label question;

    @FXML
    public VBox respuestas;

    private User currentUser;
    private ToggleGroup grupoRespuestas = new ToggleGroup();
    private List<Problem> problemasPendientes;
    
    public int hits;
    public int faults;


    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void cargarProblema() {
        if (problemasPendientes == null) {
            try {
                problemasPendientes = new ArrayList<>(Navigation.getInstance().getProblems());
            } catch (NavDAOException ex) {
                Logger.getLogger(FXMLPreguntasController.class.getName()).log(Level.SEVERE, null, ex);
                question.setText("Error al cargar problemas.");
                return;
            }
        }

        if (problemasPendientes.isEmpty()) {
            question.setText("✅ ¡Has respondido todos los problemas!");
            respuestas.getChildren().clear();
            return;
        }

        Problem problema = problemasPendientes.remove(new Random().nextInt(problemasPendientes.size()));

        question.setText(problema.getText());

        respuestas.getChildren().clear();
        grupoRespuestas = new ToggleGroup();

        for (Answer respuesta : problema.getAnswers()) {
            RadioButton rb = new RadioButton(respuesta.getText());
            rb.setUserData(respuesta);
            rb.setToggleGroup(grupoRespuestas);
            respuestas.getChildren().add(rb);
        }
    }

    @FXML
    private void handleComprobar(ActionEvent event) {
        RadioButton seleccionada = (RadioButton) grupoRespuestas.getSelectedToggle();

        if (seleccionada == null) {
            question.setText("⚠️ Selecciona una respuesta.");
            return;
        }

        Answer respuesta = (Answer) seleccionada.getUserData();
        String actualText = seleccionada.getText();
        
        if (actualText.contains("✅")) actualText = actualText.split(" ✅")[0];
        if (actualText.contains("❌")) actualText = actualText.split(" ❌")[0];

        if (respuesta.getValidity()) {
            hits++;
            seleccionada.setText(actualText + " ✅ ¡Correcto! Cargando siguiente...");
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                javafx.application.Platform.runLater(() -> cargarProblema());
            }).start();
        } else {
            faults++;
            seleccionada.setText(actualText + " ❌ Incorrecto.");
        }
    }

    @FXML
    private void handleVolver(ActionEvent event) {
        currentUser.addSession(hits, faults);
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();  // Cierra esta ventana
    }    
}