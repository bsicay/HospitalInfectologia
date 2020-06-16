
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
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Medico;
import org.brandonsicay.bean.TelefonoMedico;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class TelefonoMedicoController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoMedico> listaTelefonoMedico;
    private ObservableList<Medico> listaMedico;
    @FXML private TextField txtTelefonoPersonal; 
    @FXML private TextField txtTelefonoTrabajo; 
    @FXML private ComboBox cmbCodigoMedico; 
    @FXML private TableView tblTelefonoMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colTelefonoPersonal;
    @FXML private TableColumn colTelefonoTrabajo;
    @FXML private TableColumn colCodigoMedico;   
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
      cargarDatos();
      cmbCodigoMedico.setItems(getMedicos());
      cmbCodigoMedico.setDisable(true);

    }
    
    public void cargarDatos(){
            tblTelefonoMedicos.setItems(getTelefonoMedicos());
            colCodigo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoTelefonoMedico"));
            colTelefonoPersonal.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoPersonal"));
            colTelefonoTrabajo.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, String>("telefonoTrabajo"));
            colCodigoMedico.setCellValueFactory(new PropertyValueFactory<TelefonoMedico, Integer>("codigoMedico"));
    }
    
    public void soloNumerosEnteros(KeyEvent event) {
    try{
        char tecla = event.getCharacter().charAt(0);
        if(!Character.isDigit(tecla))
            event.consume();
        
    }catch (Exception e){ 
        e.printStackTrace();
    }
} 
   
   public void telefonoPersonal(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla) || Character.isSpaceChar(tecla) || txtTelefonoPersonal.getText().length() >= 9)
            event.consume();
        else if(txtTelefonoPersonal.getText().length()==4){
            txtTelefonoPersonal.appendText("-");
        }
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   } 
      
   public void telefonoTrabajo(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla) || Character.isSpaceChar(tecla) || txtTelefonoTrabajo.getText().length() >= 9)
            event.consume();
        else if(txtTelefonoTrabajo.getText().length()==4){
            txtTelefonoTrabajo.appendText("-");
        }
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   }    
    
     public ObservableList<TelefonoMedico> getTelefonoMedicos(){
        ArrayList<TelefonoMedico> lista = new ArrayList<TelefonoMedico>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoMedico}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new TelefonoMedico(resultado.getInt("codigoTelefonoMedico"),
                                resultado.getString("telefonoPersonal"),
                                resultado.getString("telefonoTrabajo"),
                                resultado.getInt("codigoMedico")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaTelefonoMedico = FXCollections.observableList(lista);
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
   
   
    public void seleccionarElementos(){   
        if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
        txtTelefonoPersonal.setText(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        txtTelefonoTrabajo.setText(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getTelefonoTrabajo());
        cmbCodigoMedico.getSelectionModel().select(buscarMedico(((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico()));
        }else{
            tblTelefonoMedicos.getSelectionModel().clearSelection();
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
        }return resultado;
        
    }

    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 activarControles();
                 limpiarControles();
                 tblTelefonoMedicos.getSelectionModel().clearSelection();
                 tblTelefonoMedicos.setOnKeyReleased(null);
                 tblTelefonoMedicos.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 cmbCodigoMedico.setDisable(false);
                 limpiarControles();
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtTelefonoPersonal.getText().isEmpty() || txtTelefonoTrabajo.getText().isEmpty() || cmbCodigoMedico.getSelectionModel().isEmpty()){
                   JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{        
                guardar();
                desactivarControles();
                limpiarControles();
                tblTelefonoMedicos.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblTelefonoMedicos.setOnMouseClicked((event) -> {seleccionarElementos();
                });
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;       
                break;
                }
        }
    }
    
    public void guardar(){
        TelefonoMedico registro = new TelefonoMedico();
        registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
        registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
        registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoMedico(?,?,?)}");
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoMedico());
            procedimiento.execute();
            listaTelefonoMedico.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoMedico.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
                break;
            case ACTUALIZAR:
                    actualizar();
                    desactivarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    cmbCodigoMedico.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();        
        
        }
    } 
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarTelefonoMedico(?,?,?)}");
            TelefonoMedico registro = (TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem();
            registro.setTelefonoPersonal(txtTelefonoPersonal.getText());
            registro.setTelefonoTrabajo(txtTelefonoTrabajo.getText());
            registro.setCodigoMedico(((Medico)cmbCodigoMedico.getSelectionModel().getSelectedItem()).getCodigoMedico());
            procedimiento.setString(1, registro.getTelefonoPersonal());
            procedimiento.setString(2, registro.getTelefonoTrabajo());
            procedimiento.setInt(3, registro.getCodigoTelefonoMedico());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 tblTelefonoMedicos.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblTelefonoMedicos.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                 
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblTelefonoMedicos.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eiliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoMedico(?)}");
                      procedimiento.setInt(1, ((TelefonoMedico)tblTelefonoMedicos.getSelectionModel().getSelectedItem()).getCodigoTelefonoMedico());
                      procedimiento.execute();
                      listaTelefonoMedico.remove(tblTelefonoMedicos.getSelectionModel().getSelectedItem());
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
                 btnReporte.setText("Reporte");                
                 btnEditar.setText("Editar");
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);  
                 cmbCodigoMedico.setDisable(true);
                 tblTelefonoMedicos.getSelectionModel().clearSelection();
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;                
        }
                
    }    
   
    public void desactivarControles(){
        txtTelefonoPersonal.setEditable(false);
        txtTelefonoTrabajo.setEditable(false);
        cmbCodigoMedico.setDisable(true);
    }
        
    public void activarControles(){
        txtTelefonoPersonal.setEditable(true);
        txtTelefonoTrabajo.setEditable(true);

    }
    
    public void limpiarControles(){
        txtTelefonoPersonal.setText("");
        txtTelefonoTrabajo.setText("");
        cmbCodigoMedico.setValue(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void medicos(){
        escenarioPrincipal.ventanaMedicos();
    } 
}
   
