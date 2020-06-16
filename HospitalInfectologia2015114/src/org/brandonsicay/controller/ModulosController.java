

package org.brandonsicay.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.brandonsicay.sistema.Principal;

/**
 *
 * @author programacion
 */
public class ModulosController implements Initializable{
    private Principal escenarioPrincipal;
    @FXML private ImageView pacientes; 
    @FXML private ImageView medicos; 
    @FXML private ImageView especialidades; 
    @FXML private ImageView areas;
    @FXML private ImageView cargos;
    @FXML private ImageView medicoEspecialidad;
    @FXML private ImageView responsableTurno;
    @FXML private ImageView turno;
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
    
    public void medicoEspecialidad(){
        escenarioPrincipal.medicoEspecialidad();
    }   
    
    public void ResponsableTurno(){
        escenarioPrincipal.ResponsableTurno();
    }
    
    public void turno(){
        escenarioPrincipal.turno();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void pacientesZoom(){
        pacientes.setImage(new Image("/org/brandonsicay/images/png/pacientesZoom.png"));
    }
    
    public void pacientesNormal(){
        pacientes.setImage(new Image("/org/brandonsicay/images/png/pacientes.png"));
    }    
    
    public void medicosZoom(){
        medicos.setImage(new Image("/org/brandonsicay/images/png/medicosZoom.png"));
    }
    
    public void medicosNormal(){
        medicos.setImage(new Image("/org/brandonsicay/images/png/medicos.png"));
    }   
    
    public void especialidadesZoom(){
        especialidades.setImage(new Image("/org/brandonsicay/images/png/especialidadesZoom.png"));
    }
    
    public void especialidadesNormal(){
        especialidades.setImage(new Image("/org/brandonsicay/images/png/especialidades.png"));
    }     
    
    public void areasZoom(){
        areas.setImage(new Image("/org/brandonsicay/images/png/areasZoom.png"));
    }
    
    public void areasNormal(){
        areas.setImage(new Image("/org/brandonsicay/images/png/areas.png"));
    }     
    
    public void cargosZoom(){
        cargos.setImage(new Image("/org/brandonsicay/images/png/cargosZoom.png"));
    }
    
    public void cargosNormal(){
        cargos.setImage(new Image("/org/brandonsicay/images/png/cargos.png"));
    }     
    
    public void medicoEspecialidadZoom(){
        medicoEspecialidad.setImage(new Image("/org/brandonsicay/images/png/medicoEspecialidadZoom.png"));
    }
    
    public void medicoEspecialidadNormal(){
        medicoEspecialidad.setImage(new Image("/org/brandonsicay/images/png/medicoEspecialidad.png"));
    }    
    
    public void responsableTurnoNormal(){
        responsableTurno.setImage(new Image("/org/brandonsicay/images/png/responsableTurno.png"));
    }
    
    public void responsableTurnoZoom(){
        responsableTurno.setImage(new Image("/org/brandonsicay/images/png/responsableTurnoZoom.png"));
    }    
    
    public void turnoZoom(){
        turno.setImage(new Image("/org/brandonsicay/images/png/TurnoZoom.png"));
    }
    
    public void turnoNormal(){
        turno.setImage(new Image("/org/brandonsicay/images/png/TurnoNormal.png"));
    }    
    
    
}
