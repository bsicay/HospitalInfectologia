
package org.brandonsicay.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.brandonsicay.report.GenerarReporte;
import org.brandonsicay.sistema.Principal;



public class ReportesController implements Initializable{
    private Principal escenarioPrincipal;
    @FXML private ImageView pacientes; 
    @FXML private ImageView medicos;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void imprimirReporteMedico(){    
        Map parametros = new HashMap();
        parametros.put("codigoMedico", null);
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper", "Reporte de Medicos", parametros);
    }
                
    public void imprimirReportePaciente(){
        Map parametros = new HashMap();
        parametros.put("idPaciente", null); 
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
    }          
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }    
     public void principal(){
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
   
}


