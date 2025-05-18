/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;

/**
 *
 * @author pocky
 */
public class FXMLPerfilController {

    @FXML
    private ImageView imagenFotoPerfil;
    @FXML
    private MenuButton botFoto;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private DatePicker birthField;
    @FXML
    private Button botGuardar;
    @FXML
    private Button botVolver;

     private User currentUser;

    private String imagePath = null;

    public void setCurrentUser(User user) {
        this.currentUser = user;
    
        // Cargar datos actuales
        emailField.setText(user.getEmail());
        birthField.setValue(user.getBirthdate());
        imagenFotoPerfil.setImage(user.getAvatar());
    }

    
    @FXML
    private void handleBotFotoOnAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecciona imagen de perfil");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            imagePath = selectedFile.toURI().toString();
            imagenFotoPerfil.setImage(new Image(imagePath));
        }
    }

    @FXML
    private void handleBotGuardar(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate birthdate = birthField.getValue();

        // Validaciones básicas (puedes mejorarlas con regex)
        if (!email.contains("@") || password.length() < 6 || birthdate == null) {
            showAlert("Datos inválidos", "Revisa los campos. Email, contraseña y fecha obligatorios.");
            return;
        }

        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setBirthdate(birthdate);
        if (imagePath != null) {
            Image image = new Image(imagePath);
            currentUser.setAvatar(image);
        }

        showAlert("Perfil actualizado", "Los datos se han guardado correctamente.");
    }

    @FXML
    private void handleBotVolver(ActionEvent event) {
        botVolver.getScene().getWindow().hide();
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}
