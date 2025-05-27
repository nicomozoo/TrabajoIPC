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
import java.util.ResourceBundle;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;

public class FXMLRegisterController implements Initializable {

    @FXML
    private Label emailError;
    @FXML
    private TextField emailField;
 
    //properties to control valid fields values. 
    private BooleanProperty validEmail;
    private BooleanProperty validUsername;
    private BooleanProperty validPassword;
    private BooleanProperty validDate;
 
    
    // listener to register on textProperty() or valueProperty()
    private ChangeListener<String> listenerEmail;
    private ChangeListener<String> listenerUsername;
    private ChangeListener<String> listenerPassword;
    private ChangeListener<LocalDate> listenerBirthDate;
    
    
    
    
    @FXML
    private TextField usernameField;
    @FXML
    private Label usernameError;
    @FXML
    private TextField passwordField;
    @FXML
    private Label passwordError;
    @FXML
    private DatePicker birthField;
    @FXML
    private Label birthError;
    @FXML
    private Button botAccept;
    @FXML
    private Button botCancel;
    @FXML
    private Button botFoto;
    @FXML
    private ImageView imagenFotoPerfil;
    @FXML
    private Pane pane;
    @FXML
    private ImageView imagenFondo;
    @FXML
    private VBox VBox;

    
    private void checkEmail() {
        String email = emailField.getText();
//        boolean isValid = email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        validEmail.set(isValid); //actualiza la property asociada
        showError(isValid, emailField, emailError); //muestra o esconde el mensaje de error
    }
    
    private void checkUsername(){
        String username = usernameField.getText();
        boolean isValid = username.matches("^[a-zA-Z0-9_-]{6,15}$");
        validUsername.set(isValid);
        showError(isValid, usernameField, usernameError);
    }
    
    private void checkPassword(){
        String password = passwordField.getText();
        boolean isValid = password.matches("^[a-zA-Z\\d!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]{8,20}$");
        validPassword.set(isValid);
        showError(isValid, passwordField, passwordError);
    }

    private void checkDate(){
        LocalDate value = birthField.getValue();
        boolean isValid = value.isBefore(LocalDate.now().minus(16, YEARS));
        validDate.set(isValid);
        showError(isValid, birthField, birthError);
    }
    
    private void showError(boolean isValid, Node field, Node errorMessage){
        errorMessage.setVisible(!isValid);
        field.setStyle(((isValid) ? "" : "-fx-background-color: #FCE5E0"));
    }

    //=========================================================
    // you must initialize here all related with the object 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        imagenFondo.fitWidthProperty().bind(pane.widthProperty());
        imagenFondo.fitHeightProperty().bind(pane.heightProperty());
        
        
        
        validEmail = new SimpleBooleanProperty(false);
        
        validUsername = new SimpleBooleanProperty(false);
        
        validPassword = new SimpleBooleanProperty(false);
        
        validDate = new SimpleBooleanProperty(false);

        //When the field loses focus, the field is validated. 
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkEmail();
                if (!validEmail.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerEmail == null) {
                        listenerEmail = (a, b, c) -> checkEmail();
                        emailField.textProperty().addListener(listenerEmail);
                    }
                }
            }
        });
        
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkUsername();
                if (!validUsername.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerUsername == null) {
                        listenerUsername = (a, b, c) -> checkUsername();
                        usernameField.textProperty().addListener(listenerUsername);
                    }
                }
            }
        });
        
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkPassword();
                if (!validPassword.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerPassword == null) {
                        listenerPassword = (a, b, c) -> checkPassword();
                        passwordField.textProperty().addListener(listenerPassword);
                    }
                }
            }
        });
        
        birthField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                checkDate();
                if (!validDate.get()) {
                    //If it is not correct, a listener is added to the text or value 
                    //so that the field is validated while it is being edited.
                    if (listenerBirthDate == null) {
                        listenerBirthDate = (a, b, c) -> checkDate();
                        birthField.valueProperty().addListener(listenerBirthDate);
                    }
                }
            }
        });
        
        BooleanBinding validFields = Bindings.and(validEmail, validUsername)
                                                                            .and(validPassword)
                                                                            .and(validDate);
        botAccept.disableProperty().bind(
        Bindings.not(validFields)
        ); 
        
       
    }

    @FXML
    private void handleBotAcceptOnAction(ActionEvent event) throws NavDAOException {
        User user = Navigation.getInstance().registerUser(usernameField.getText(), emailField.getText(), passwordField.getText(), imagenFotoPerfil.getImage(), birthField.getValue());
        
        emailField.clear();
        usernameField.clear();
        passwordField.clear();
        birthField.setValue(null);

        validEmail.setValue(Boolean.FALSE);
        validUsername.setValue(Boolean.FALSE);
        validPassword.setValue(Boolean.FALSE);
        validDate.setValue(Boolean.FALSE);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro completo");
        alert.setHeaderText("REGISTRADO CON ÉXITO");
        alert.setContentText("Ha sido registrado con éxito en NAVALTEST");

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/resources/tick.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);  // reemplaza el icono de exclamación

         // 2. 🖋️ Estilo de la ventana
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10; -fx-border-radius: 10;");

        dialogPane.lookup(".header-panel").setStyle("-fx-background-color: #A259FF;");
        dialogPane.lookup(".header-panel .label").setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        dialogPane.lookup(".content.label").setStyle("-fx-text-fill: #333333; -fx-font-size: 15px; -fx-font-weight: bold;");

        alert.showAndWait();
        
        
        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLConfirmRegister.fxml"));
            Parent root = loader.load();

            
            Stage stage = (Stage) pane.getScene().getWindow();;
            stage.setScene(new Scene(root));
            stage.setTitle("Register Confirmado");
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBotFotoOnAction(ActionEvent event) {
         FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar imagen de perfil");
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        imagenFotoPerfil.setImage(new Image(fc.showOpenDialog(new Stage()).toURI().toString()));    
    }

    void setCurrentUser(User currentUser) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void handleBotCancel(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml")); // o FXMLMain.fxml
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Inicio");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }
}