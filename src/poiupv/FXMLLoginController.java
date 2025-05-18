/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;

/**
 *
 * @author pocky
 */
public class FXMLLoginController {
    
    @FXML
    private TextField passwordField;
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameError;
    @FXML
    private Label passwordError;

    private User currentUser;
    
     @FXML
    private void initialize() {
        usernameError.setVisible(false);
        passwordError.setVisible(false);

        usernameField.setOnKeyTyped(e -> usernameError.setVisible(false));
        passwordField.setOnKeyTyped(e -> passwordError.setVisible(false));
    }

    @FXML
    private void handleLoginButton(ActionEvent event) throws NavDAOException {
        String nick = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validación de campos vacíos
        if (nick.isEmpty()) {
            usernameError.setText("El nombre de usuario no puede estar vacío.");
            usernameError.setVisible(true);
            return;
        }

        if (password.isEmpty()) {
            passwordError.setText("La contraseña no puede estar vacía.");
            passwordError.setVisible(true);
            return;
        }

        // Autenticación
        User user = Navigation.getInstance().authenticate(nick, password);
        

        if (user != null) {
            currentUser = user;

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
                Parent root = loader.load();

                FXMLDocumentController controller = loader.getController();
                controller.setCurrentUser(currentUser);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Menú Principal");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            // Si la autenticación falla, no sabemos cuál está mal
            usernameError.setText("Usuario o contraseña incorrectos.");
            usernameError.setVisible(true);
            passwordError.setText("Usuario o contraseña incorrectos.");
            passwordError.setVisible(true);
        }
    }
}
