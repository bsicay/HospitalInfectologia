
package org.brandonsicay.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import org.brandonsicay.sistema.Principal;



public class VentanaAcercaDeController implements Initializable {
    
    @FXML private MenuItem menuAcercaDe;


    
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
    
    public void ayuda(){
        escenarioPrincipal.ayuda();
    }    
    
}
