/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.NavDAOException;
import model.Navigation;
import model.User;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;
    private Group drawGroup;
    private User currentUser;
    private Line linePainting;
    private Circle circlePainting;
    private double inicioXArc;
    private double inicioYArc;

    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label mousePosition;
    @FXML
    private Button botCerrarSesion;
    @FXML
    private Button butClear;
    @FXML
    private ComboBox<Integer> fontSizeBox;
    private int currentFontSize = 2;
    @FXML
    private Button buttonArco;
    @FXML
    private Button buttonLine;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Button buttonText;
    
    @FXML
    private void selectLineTool() {
        currentTool = Tool.LINE;
    }
    @FXML
    private void selectCircleTool() {
        currentTool = Tool.CIRCLE;
    }  

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la mousePosistion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la mousePosistion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    @FXML
    private void handleColorChange(ActionEvent event) {
    }
    
    private enum Tool {
    LINE, CIRCLE, TEXT
    }
    
    private void handleColorChange() {
        currentColor = colorPicker.getValue();
    }
    
    private Tool currentTool = Tool.LINE;
    private Color currentColor = Color.BLACK;

    private void initData() {
        
    }
    
    @FXML
    private void selectTextTool() {
        currentTool = Tool.TEXT;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        //Inicializamos manejadores
        zoomGroup.setOnMousePressed(this::onMousePressed);
        zoomGroup.setOnMouseDragged(this::onMouseDragged);
        
        fontSizeBox.getItems().addAll(2, 4, 6, 8, 10, 12, 14, 18, 24, 36);
        fontSizeBox.setValue(currentFontSize);

    }
    
    @FXML
    private void handleFontSizeChange() {
        Integer selectedSize = fontSizeBox.getValue();
        if (selectedSize != null) {
            currentFontSize = selectedSize;
        }
    }
    
    @FXML
    private void handleClearAll() { 
        zoomGroup.getChildren().removeIf(node ->
                node instanceof Line ||
                node instanceof Circle);
    }
    
    
    private void onMousePressed(MouseEvent e) {
    switch (currentTool) {
        case LINE -> {
            linePainting = new Line();
            linePainting.setStartX(e.getX());
            linePainting.setStartY(e.getY());
            linePainting.setEndX(e.getX());
            linePainting.setEndY(e.getY());
            linePainting.setStrokeWidth(currentFontSize);
            linePainting.setStroke(colorPicker.getValue());
            addContextMenuToLine(linePainting);
            zoomGroup.getChildren().add(linePainting);
        }
        case CIRCLE -> {
            circlePainting = new Circle(1);
            circlePainting.setStroke(colorPicker.getValue());
            circlePainting.setFill(Color.TRANSPARENT);
            circlePainting.setCenterX(e.getX());
            circlePainting.setCenterY(e.getY());
            inicioXArc = e.getX();
            circlePainting.setStrokeWidth(currentFontSize);
            //addContextMenuToCircle(circlePainting);
            zoomGroup.getChildren().add(circlePainting);
        }
        case TEXT -> {
            Text textNode = new Text(e.getX(), e.getY(), "");
            int size = fontSizeBox.getValue();
            Color color = colorPicker.getValue();
            textNode.setFont(new Font(size));
            textNode.setFill(color);
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Agregar nota");
            dialog.setHeaderText("Introduce el texto de la nota:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(inputText -> {
            textNode.setText(inputText);
            //addContextMenuToText(textNode);
            zoomGroup.getChildren().add(textNode);
            });
        }

    }
}
    
    private void onMouseDragged(MouseEvent e) {
    switch (currentTool) {
        case LINE -> {
            if (linePainting != null) {
                linePainting.setEndX(e.getX());
                linePainting.setEndY(e.getY());
            }
        }
        case CIRCLE -> {
            if (circlePainting != null) {
                double radius = Math.abs(e.getX() - inicioXArc);
                circlePainting.setRadius(radius);
            }
        }
    }
     e.consume();
    }
    
    private void addContextMenuToLine(Line line) {
    line.setOnContextMenuRequested(e -> {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem eliminar = new MenuItem("Eliminar línea");

        eliminar.setOnAction(ev -> {
            zoomGroup.getChildren().remove(line);
        });

        contextMenu.getItems().add(eliminar);
        contextMenu.show(line, e.getScreenX(), e.getScreenY());
        e.consume();
    });
}

    @FXML
    private void showPosition(MouseEvent event) {
        mousePosition.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    @FXML
    private void handleBotCerrarSesion(ActionEvent event) throws NavDAOException, IOException {
    // Obtener el usuario actual
        if(showAlert("Cerrar Sesión", "¿Estás seguro de que quieres cerrar sesión?")){
            try {
                // Volver al login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) zoom_slider.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Inicio de sesión");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            
        }
    }

    @FXML
    private void handleBotVerSesiones(ActionEvent event) {
         try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLSesiones.fxml"));
        Parent root = loader.load();

        FXMLSesionesController controller = loader.getController();
        controller.setCurrentUser(currentUser); // Pasar el usuario actual

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Historial de sesiones");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void handleBotEditarPerfil(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPerfil.fxml"));
            Parent root = loader.load();

            FXMLPerfilController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Perfil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void preguntasOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLPreguntasLista.fxml"));
            Parent root = loader.load();
            FXMLPreguntasListaController controller = loader.getController();
            controller.setCurrentUser(currentUser);
    
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Preguntas");
    
            newStage.show();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        
        
        Optional<ButtonType> resultado = alert.showAndWait();
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }

    
}
