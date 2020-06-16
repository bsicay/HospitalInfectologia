
package org.brandonsicay.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.brandonsicay.sistema.Principal;



public class VentanaProgramadorController implements Initializable {
    
    @FXML private MenuItem menuAcercaDe;
    @FXML private ImageView programador;


    
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
   
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }   
    
    public void programador(){
        escenarioPrincipal.acercaDe();
    }  

    public void programadorNormal(){
        programador.setImage(new Image("/org/brandonsicay/images/png/programador.png"));
    }
    
     public void programadorZoom(){
        programador.setImage(new Image("/org/brandonsicay/images/png/programadorZoom1.png"));
    }   
}
