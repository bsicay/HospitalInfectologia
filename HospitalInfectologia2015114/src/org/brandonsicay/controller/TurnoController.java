package org.brandonsicay.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import groovy.lang.PropertyValue;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.brandonsicay.bean.MedicoEspecialidad;
import org.brandonsicay.bean.Paciente;
import org.brandonsicay.bean.ResponsableTurno;
import org.brandonsicay.bean.Turno;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;




public class TurnoController implements Initializable{
    
    public Principal escenarioPrincipal;
    private ObservableList listaTurno;
    private ObservableList listaTurnoResponsable;
    private ObservableList listaPaciente;
    private ObservableList listaMedicoEspecialidad;
    private DatePicker fechaCita;
    private DatePicker fechaTurno;
    @FXML private ComboBox cmbCodigoMedicoEspecialidad; 
    @FXML private ComboBox cmbCodigoTurnoResponsable;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TableView tblTurnos;
    @FXML private TableColumn colCodigo; 
    @FXML private TableColumn colFechaTurno; 
    @FXML private TableColumn colFechaCita; 
    @FXML private TableColumn colValorCita; 
    @FXML private TableColumn colCodigoMedicoEspecialidad;
    @FXML private TableColumn colCodigoTurnoResponsable; 
    @FXML private TableColumn colCodigoPaciente;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte; 
    @FXML private GridPane grpFechaTurno; 
    @FXML private GridPane grpFechaCita;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fechaTurno = new DatePicker(Locale.ENGLISH);
        fechaTurno.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaTurno.getCalendarView().todayButtonTextProperty().set("Today");
        fechaTurno.getStylesheets().add("/org/brandonsicay/resource/DatePicker.css");
        grpFechaTurno.add(fechaTurno, 0, 0);
        grpFechaTurno.setDisable(true);
        fechaCita = new DatePicker(Locale.ENGLISH);
        fechaCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaCita.getCalendarView().todayButtonTextProperty().set("Today");
        fechaCita.getStylesheets().add("/org/brandonsicay/resource/DatePicker.css");
        grpFechaCita.add(fechaCita, 0, 0);
        grpFechaCita.setDisable(true);
    }
    
    public void cargarDatos(){
        tblTurnos.setItems(getTurno());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurno"));
        colFechaTurno.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Turno, Date>("fechaTurno"));
        colValorCita.setCellValueFactory(new PropertyValueFactory<Turno, Float>("valorCita"));
        colCodigoMedicoEspecialidad.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoMedicoEspecialidad"));
        colCodigoTurnoResponsable.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoTurnoResponsable"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Turno, Integer>("codigoPaciente"));
    }
    
    public ObservableList<Turno> getTurno(){
        ArrayList<Turno> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Turno(resultado.getInt("codigoTurno"),
                                resultado.getDate("fechaCita"),
                                resultado.getDate("fechaTurno"),
                                resultado.getFloat("valorCita"),
                                resultado.getInt("codigoMedicoEspecialidad"),
                                resultado.getInt("codigoTurnoResponsable"),
                                resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTurno = FXCollections.observableList(lista);
    }
    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarMedicoEspecialidad");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new MedicoEspecialidad(resultado.getInt("codigoMedicoEspecialidad"),
                                    resultado.getInt("codigoMedico"),
                                    resultado.getInt("codigoEspecialidad"),
                                    resultado.getInt("codigoHorario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicoEspecialidad = FXCollections.observableList(lista);
        
    }
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                    resultado.getString("nombreResponable"),
                                    resultado.getString("apellidoResponsable"),
                                    resultado.getString("telefonoPersonal"),
                                    resultado.getInt("codigoArea"),
                                    resultado.getInt("codigoCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTurnoResponsable = FXCollections.observableList(lista);
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarPaciente}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("idPaciente"),
                                resultado.getString("DPI"),
                                resultado.getString("apellido"),
                                resultado.getString("nombre"),
                                resultado.getDate("fechaNacimiento"),
                                resultado.getInt("edad"),
                                resultado.getString("direccion"),
                                resultado.getString("ocupacion"),
                                resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
   public void modulos(){
       escenarioPrincipal.Modulos();
   }
 
    
}
