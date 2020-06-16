

package org.brandonsicay.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.brandonsicay.bean.Usuarios;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class MenuPrincipalController implements Initializable{
    private Principal escenarioPrincipal;
    @FXML private ImageView cuadro;
    @FXML private ImageView ayuda;
    @FXML private ImageView reportes;
    @FXML private ImageView sesion;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void VentanaAcercaDeController(){
        escenarioPrincipal.acercaDe();
    }
    
    public void ventanaMedicos(){
        escenarioPrincipal.ventanaMedicos();
    }
    

    public void Pacientes(){
        escenarioPrincipal.Pacientes();
    }
  
    public void ContactoUrgencia(){
        escenarioPrincipal.ContactoUrgencia();  
    }
  
    public void Especialidades(){
        escenarioPrincipal.Especialidades();
    }
    
    public void Areas(){
        escenarioPrincipal.Areas();
    }    
    
    public void Cargos(){
        escenarioPrincipal.Cargos();
    }
    
    public void ResponsableTurno(){
        escenarioPrincipal.ResponsableTurno();
    }
    
    public void Modulos(){
        escenarioPrincipal.Modulos();
    }    
      
    public void ayuda(){
        escenarioPrincipal.ayuda();
    }
    
    public void reportes(){
        escenarioPrincipal.reportes();  
    }
    
    public void cerrarSesion(){
        LoginController registro = new LoginController();
        escenarioPrincipal.login();
    }
    
    public void sesionNormal(){
        sesion.setImage(new Image("/org/brandonsicay/images/png/cerrarSesionNormal.png"));
    }
    
    public void sesionZoom(){
        sesion.setImage(new Image("/org/brandonsicay/images/png/cerrarSesionZoom.png"));
    }   
    
    public void zoom(){
        cuadro.setImage(new Image("/org/brandonsicay/images/png/zoom1.png"));
    }
    
    public void normal(){
        cuadro.setImage(new Image("/org/brandonsicay/images/png/modulos.png"));
    }
    
    public void zoomReporte(){
        reportes.setImage(new Image("/org/brandonsicay/images/png/reportesZoom.png"));
    }
    
    public void normalReporte(){
        reportes.setImage(new Image("/org/brandonsicay/images/png/reportes.png"));
    }  
    
    public void ayudaZoom(){
        ayuda.setImage(new Image("/org/brandonsicay/images/png/ayudaZoom.png"));
    }
    
    public void ayudaNormal(){
        ayuda.setImage(new Image("/org/brandonsicay/images/png/ayuda.png"));
    } 
       
}
