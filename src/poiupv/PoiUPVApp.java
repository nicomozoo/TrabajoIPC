/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    public static int fallos;
    public static int aciertos;
    private User currentUser;
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    public static void setAciertos(int n){
        aciertos = n;
    }
    public static void setFallos(int n){
        fallos = n;
    }
    public static int getAciertos(){
        return aciertos;
    }
    public static int getFallos(){
        return fallos;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLInicio.fxml"));
    Parent root = loader.load();

    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setTitle("NAVALTEST - Inicio");
    stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
