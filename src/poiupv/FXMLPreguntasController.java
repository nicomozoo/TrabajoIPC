/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.fxml.FXML;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Answer;
import model.NavDAOException;
import model.Navigation;
import model.Problem;
import model.User;

public class FXMLPreguntasController {
    
    @FXML
    public Label question;
    
    @FXML
    public VBox respuestas;
    
    private User currentUser;
    
    public void cargarProblema() {
        List<Problem> problemas;
        try {
            problemas = Navigation.getInstance().getProblems();
            if (!problemas.isEmpty()) {
                Problem problema = problemas.get(new Random().nextInt(problemas.size()));
                question.setText(problema.getText());
                respuestas.getChildren().clear();
                ToggleGroup grupoRespuestas = new ToggleGroup();
                for (Answer respuesta : problema.getAnswers()) {
                    RadioButton rb = new RadioButton(respuesta.getText());
                    rb.setToggleGroup(grupoRespuestas);
                    rb.setUserData(respuesta);
                    respuestas.getChildren().add(rb);
                }
            }
        } catch (NavDAOException ex) {
            Logger.getLogger(FXMLPreguntasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    private void handleComprobar(ActionEvent event) {
        
    }
    
    @FXML
    private void handleVolver(ActionEvent event) {
        try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Parent root = loader.load();

                // FXMLDocumentController controller = loader.getController();
                // controller.setCurrentUser(currentUser);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Menú Principal");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}