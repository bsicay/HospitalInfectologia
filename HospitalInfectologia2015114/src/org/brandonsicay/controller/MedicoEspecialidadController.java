package org.brandonsicay.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;;
import org.brandonsicay.bean.Especialidad;
import org.brandonsicay.bean.Horarios;
import org.brandonsicay.bean.Medico;
import org.brandonsicay.bean.MedicoEspecialidad;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class MedicoEspecialidadController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<MedicoEspecialidad> listaMedicoEspecialidad;
    private ObservableList<Medico> listaMedico;
    private ObservableList<Especialidad> listaEspecialidad;
    private ObservableList<Horarios> listaHorario;;
    @FXML private ComboBox cmbCodigoMedico;
    @FXML private ComboBox cmbCodigoEspecialidad;
    @FXML private ComboBox cmbCodigoHorario;
    @FXML private TableView tblMedicosEspecialidad;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colMedico;
    @FXML private TableColumn colEspecialidad;
    @FXML private TableColumn colHorario;  
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoMedico.setItems(getMedicos());
        cmbCodigoMedico.setDisable(true);
        cmbCodigoEspecialidad.setItems(getEspecialidades());
        cmbCodigoEspecialidad.setDisable(true);
        cmbCodigoHorario.setItems(getHorarios());
        cmbCodigoHorario.setDisable(true);
    }
    
    public void cargarDatos(){
        tblMedicosEspecialidad.setItems(getMedicoEspecialidad());
        colCodigo.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedicoEspecialidad"));
        colMedico.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoMedico"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoEspecialidad"));
        colHorario.setCellValueFactory(new PropertyValueFactory<MedicoEspecialidad, Integer>("codigoHorario"));

        
    }
    

    
    public ObservableList<MedicoEspecialidad> getMedicoEspecialidad(){
        ArrayList<MedicoEspecialidad> lista = new ArrayList<MedicoEspecialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedicoEspecialidad}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
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
    
    public ObservableList<Medico> getMedicos(){
        ArrayList<Medico> lista = new ArrayList<Medico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMedico}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Medico(resultado.getInt("codigoMedico"),
                                resultado.getInt("licenciaMedica"),
                                resultado.getString("nombres"),
                                resultado.getString("apellidos"),
                                resultado.getString("horaEntrada"),
                                resultado.getString("horaSalida"),
                                resultado.getInt("turnoMaximo"),
                                resultado.getString("sexo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaMedico = FXCollections.observableList(lista);
    }
    
    public ObservableList<Especialidad> getEspecialidades(){
        ArrayList<Especialidad> lista = new ArrayList<Especialidad>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarEspecialidad");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Especialidad(resultado.getInt("codigoEspecialidad"),
                                resultado.getString("nombreEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEspecialidad = FXCollections.observableList(lista);
    }
    
    public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorario}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                                resultado.getString("horarioInicio"),
                                resultado.getString("horarioSalida"),
                                resultado.getBoolean("lunes"),
                                resultado.getBoolean("martes"),
                                resultado.getBoolean("miercoles"),
                                resultado.getBoolean("jueves"),
                                resultado.getBoolean("viernes")));
                                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaHorario = FXCollections.observableList(lista);
    }    
    
    
    public void seleccionarElementos(){   
        if(tblMedicosEspecialidad.getSelectionModel().getSelectedItem() != null){
        cmbCodigoMedico.getSelectionModel().select(buscarMedico(((MedicoEspecialidad)tblMedicosEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        cmbCodigoEspecialidad.getSelectionModel().select(buscarEspecialidad(((MedicoEspecialidad)tblMedicosEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad()));
        cmbCodigoHorario.getSelectionModel().select(buscarHorario(((MedicoEspecialidad)tblMedicosEspecialidad.getSelectionModel().getSelectedItem()).getCodigoHorario()));
        }else{
            tblMedicosEspecialidad.getSelectionModel().clearSelection();
        }   
    }

    public Medico buscarMedico(int codigoMedico){
        Medico resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMedico(?)}");
            procedimiento.setInt(1, codigoMedico);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medico(registro.getInt("codigoMedico"),
                        registro.getInt("licenciaMedica"), 
                        registro.getString("nombres"),
                        registro.getString("apellidos"),
                        registro.getString("horaEntrada"),
                        registro.getString("horaSalida"), 
                        registro.getInt("turnoMaximo"),
                        registro.getString("sexo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public Especialidad buscarEspecialidad(int codigoEspecialidad){
        Especialidad resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEspecialidad(?)}");
            procedimiento.setInt(1, codigoEspecialidad);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Especialidad(registro.getInt("codigoEspecialidad"),
                            registro.getString("nombreEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
     public Horarios buscarHorario(int codigoHorario){
        Horarios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorariohorarioInicio(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horarios(registro.getInt("codigoHorario"),
                            registro.getString("horarioInicio"),
                            registro.getString("horarioSalida"),
                            registro.getBoolean("lunes"),
                            registro.getBoolean("martes"),
                            registro.getBoolean("miercoles"),
                            registro.getBoolean("jueves"),
                            registro.getBoolean("viernes"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return resultado;
    }   
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 limpiarControles();
                 tblMedicosEspecialidad.getSelectionModel().clearSelection();
                 tblMedicosEspecialidad.setOnKeyReleased(null);
                 tblMedicosEspecialidad.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 cmbCodigoMedico.setDisable(false);
                 cmbCodigoEspecialidad.setDisable(false);
                 cmbCodigoHorario.setDisable(false);
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(cmbCodigoMedico.getSelectionModel().isEmpty() || cmbCodigoEspecialidad.getSelectionModel().isEmpty() || cmbCodigoHorario.getSelectionModel().isEmpty()){
                   JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{        
                guardar();
                desactivarControles();
                limpiarControles();
                tblMedicosEspecialidad.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblMedicosEspecialidad.setOnMouseClicked((event) -> {seleccionarElementos();
                });
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                }
        }
    }
    
    
    public void guardar(){
        MedicoEspecialidad registro = new MedicoEspecialidad();
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        registro.setCodigoEspecialidad(((Especialidad)cmbCodigoEspecialidad.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
        registro.setCodigoHorario(((Horarios)cmbCodigoHorario.getSelectionModel().getSelectedItem()).getCodigoHorario());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedicoEspecialidad(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoMedico());
            procedimiento.setInt(2, registro.getCodigoEspecialidad());
            procedimiento.setInt(3, registro.getCodigoHorario());
            procedimiento.execute();
            listaMedicoEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 tblMedicosEspecialidad.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblMedicosEspecialidad.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                 
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar"); 
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblMedicosEspecialidad.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eiliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ElminarMedicoEspecialidad(?)}");
                      procedimiento.setInt(1, ((MedicoEspecialidad)tblMedicosEspecialidad.getSelectionModel().getSelectedItem()).getCodigoMedicoEspecialidad());
                      procedimiento.execute();
                      listaMedicoEspecialidad.remove(tblMedicosEspecialidad.getSelectionModel().getSelectedItem());
                      limpiarControles();
                      }catch(Exception e){
                          e.printStackTrace();
                      }
                  }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe de seleccionar un registro");
                }
        }
    } 

  
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                 desactivarControles();
                 limpiarControles();                                 // CANCELAR
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false); 
                 cmbCodigoMedico.setDisable(true);
                 cmbCodigoEspecialidad.setDisable(true);
                 cmbCodigoHorario.setDisable(true);
                 tblMedicosEspecialidad.getSelectionModel().clearSelection();
                 tipoDeOperacion = MedicoEspecialidadController.operaciones.NINGUNO;
                 break;                
        }
                
    }
 

    public void desactivarControles(){
        cmbCodigoMedico.setDisable(true);
        cmbCodigoEspecialidad.setDisable(true);
        cmbCodigoHorario.setDisable(true);
    }
        

    
    public void limpiarControles(){
        cmbCodigoMedico.setValue(null);
        cmbCodigoEspecialidad.setValue(null);
        cmbCodigoHorario.setValue(null);
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