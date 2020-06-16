
package org.brandonsicay.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.brandonsicay.sistema.Principal;

/**
 *
 * @author programacion
 */
public class MenuPrincipalController1 implements Initializable{
    @FXML private StackPane contect;
    @FXML private AnchorPane contenedor; 
    private Principal escenarioPrincipal;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void MainScene(){
     //  Parent root = FXMLLoader.load(getClass().getResource("/org/brandonsicay/view/MenuPrincipalView.fxml"));
       
       /*
       Timeline timeline = new Timeline();
       KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
       KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
       timeline.getKeyFrames().add(kf);
       timeline.play();*/
       escenarioPrincipal.menuPrincipal();
   }
      
}