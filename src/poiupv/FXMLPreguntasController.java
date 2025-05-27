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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Toggle;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class FXMLPreguntasController implements Initializable {

    @FXML
    public Label question;

    @FXML
    public VBox respuestas;
    
    @FXML
    public Button comprobar, volver;

    private User currentUser;
    private ToggleGroup grupoRespuestas = new ToggleGroup();
    private List<Problem> problemasPendientes;
    private boolean randomMode;
    
    public int hits;
    public int faults;
    public int index;
    
    public void barajar() {
        Collections.shuffle(problemasPendientes);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void setRandomMode(boolean b) {
        this.randomMode = b;
    }
    
    public void cargarProblema(int index) {
        this.index = index;
       
        if (problemasPendientes.isEmpty()) {
            question.setText("✅ ¡Has respondido todos los problemas!");
            respuestas.getChildren().clear();
            return;
        }

        Problem problema = problemasPendientes.remove(index);

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
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione una respuesta.");
            alert.showAndWait();
            return;
        }

        Answer respuesta = (Answer) seleccionada.getUserData();
        comprobar.setDisable(true);
        if (randomMode) volver.setDisable(true);

        // Colorear respuestas según validez
        for (Toggle t : grupoRespuestas.getToggles()) {
            RadioButton b = (RadioButton) t;
            boolean esCorrecta = ((Answer) b.getUserData()).getValidity();
            b.setTextFill(Paint.valueOf(esCorrecta ? "GREEN" : "RED"));
            b.setDisable(true);
        }

        // Mostrar mensaje de resultado
        boolean esCorrecta = respuesta.getValidity();
        if (esCorrecta) {
            PoiUPVApp.setAciertos(PoiUPVApp.getAciertos() + 1);
            question.setTextFill(Paint.valueOf("GREEN"));
            question.setText("¡RESPUESTA CORRECTA! " + (randomMode ? "Próxima pregunta en 3..." : ""));
        } else {
            PoiUPVApp.setFallos(PoiUPVApp.getFallos() + 1);
            question.setTextFill(Paint.valueOf("RED"));
            question.setText("¡RESPUESTA INCORRECTA! " + (randomMode ? "Próxima pregunta en 3..." : ""));
        }
        question.setFont(new Font(40));

        if (randomMode) {
            final int[] segundos = {3};
            Timeline countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {
                    segundos[0]--;
                    if (segundos[0] > 0 && randomMode) {
                        question.setText((esCorrecta ? "¡RESPUESTA CORRECTA!" : "¡RESPUESTA INCORRECTA!") + " Próxima pregunta en " + segundos[0] + "...");
                    } else {
                        question.setTextFill(Paint.valueOf("BLACK"));
                        question.setFont(new Font(12));
                        cargarProblema(index + 1);
                        comprobar.setDisable(false);
                        volver.setDisable(false);
                    }
                })
            );
            countdown.setCycleCount(3); 
            countdown.play();
        }
    }


    @FXML
    private void handleVolver(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPreguntasLista.fxml"));
            Parent root = loader.load();
            
            stage.setScene(new Scene(root));
            stage.setTitle("Preguntas");
            FXMLPreguntasListaController controller = loader.getController();
            controller.setCurrentUser(currentUser);
        } catch (IOException ex) {
            Logger.getLogger(FXMLPreguntasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (problemasPendientes == null) {
            try {
                problemasPendientes = new ArrayList<>(Navigation.getInstance().getProblems());
            } catch (NavDAOException ex) {
                Logger.getLogger(FXMLPreguntasController.class.getName()).log(Level.SEVERE, null, ex);
                question.setText("Error al cargar problemas.");
                return;
            }
        }
    }
}