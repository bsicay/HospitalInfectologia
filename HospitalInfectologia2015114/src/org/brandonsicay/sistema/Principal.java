
package org.brandonsicay.sistema;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.brandonsicay.controller.AreasController;
import org.brandonsicay.controller.CargosController;
import org.brandonsicay.controller.ContactoUrgenciaController;
import org.brandonsicay.controller.EspecialidadesController;
import org.brandonsicay.controller.HorariosController;
import org.brandonsicay.controller.LoginController;
import org.brandonsicay.controller.MedicoController;
import org.brandonsicay.controller.MedicoEspecialidadController;
import org.brandonsicay.controller.MenuPrincipalController;
import org.brandonsicay.controller.MenuPrincipalController1;
import org.brandonsicay.controller.ModulosController;
import org.brandonsicay.controller.PacienteController;
import org.brandonsicay.controller.ReportesController;
import org.brandonsicay.controller.ResponsableTurnoController;
import org.brandonsicay.controller.TelefonoMedicoController;
import org.brandonsicay.controller.TurnoController;
import org.brandonsicay.controller.UsuarioController;
import org.brandonsicay.controller.VentanaAcercaDeController;
import org.brandonsicay.controller.VentanaProgramadorController;

/**
 *
 * @author programacion
 */

public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/brandonsicay/view/";
    private Stage escenarioPrincipal;
    private Scene escena; 



    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        escenarioPrincipal.setTitle("Hospital de Infectología");
        escenarioPrincipal.getIcons().add(new Image("/org/brandonsicay/images/icono.png"));
        login();
        escenarioPrincipal.show();

    }


    public void login(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml", 760, 459);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void registrar(){
        try{
            UsuarioController registro = (UsuarioController)cambiarEscena("RegistrarView.fxml", 684, 497);
            registro.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }

    
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 850, 671);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void acercaDe(){
        try{
            VentanaAcercaDeController acercaDe = (VentanaAcercaDeController)cambiarEscena("VentanaAcercaDe.fxml", 512, 544); 
            acercaDe.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicos(){
        try{
            MedicoController ventanaMedicos = (MedicoController)cambiarEscena("MedicoView.fxml", 718, 539);
            ventanaMedicos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void telefonoMedicos(){
        try{
            TelefonoMedicoController telefonoMedico = (TelefonoMedicoController)cambiarEscena("TelefonoMedicoView.fxml", 725, 507);
            telefonoMedico.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
        
    public void Pacientes(){
        try{
            PacienteController paciente = (PacienteController)cambiarEscena("PacientesView.fxml", 826, 629);
            paciente.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
            
    public void ContactoUrgencia(){
        try{
            ContactoUrgenciaController contactoUrgencia = (ContactoUrgenciaController)cambiarEscena("ContactoUrgenciaView.fxml", 788, 534);
            contactoUrgencia.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    
    public void Especialidades(){
        try{
            EspecialidadesController especialidad = (EspecialidadesController)cambiarEscena("EspecialidadesView.fxml", 782, 523);
            especialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }
    
    public void Areas(){
        try{
            AreasController area = (AreasController)cambiarEscena("AreasView.fxml", 739, 511);
            area.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }   
    
    public void Cargos(){
        try{
            CargosController area = (CargosController)cambiarEscena("CargosView.fxml", 761, 510);
            area.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }  
    }   
    
    public void ResponsableTurno(){
         try{
            ResponsableTurnoController responsableTurno = (ResponsableTurnoController)cambiarEscena("ResponsableTurnoView.fxml", 820, 568);
            responsableTurno.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }         
    }
    
    public void Modulos(){
         try{
            ModulosController modulos = (ModulosController)cambiarEscena("VentanaModulosView.fxml", 800, 531);
            modulos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }         
    }   
    
    public void ayuda(){
         try{
            VentanaProgramadorController ayuda = (VentanaProgramadorController)cambiarEscena("VentanaAyudaView.fxml", 800, 531);
            ayuda.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }         
    }     

    public void horarios(){
         try{
            HorariosController horarios = (HorariosController)cambiarEscena("HorariosView.fxml", 820, 568);
            horarios.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }         
    }       
    
    public void reportes(){
        try{
            ReportesController reportes = (ReportesController)cambiarEscena("ReportesView.fxml", 820, 568);
            reportes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }           
    }
    
    public void medicoEspecialidad(){
        try{
            MedicoEspecialidadController medicoEspecialidad = (MedicoEspecialidadController)cambiarEscena("MedicoEspecialidadView.fxml", 799, 550);
            medicoEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }           
    }  
    
     public void turno(){
        try{
            TurnoController turno = (TurnoController)cambiarEscena("TurnoView.fxml", 886, 629);
            turno.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }           
    }     
    
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null; 
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml); 
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));  
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado; 
            
    }  

    public static void main(String[] args) {
        launch(args);
    }


    
}
