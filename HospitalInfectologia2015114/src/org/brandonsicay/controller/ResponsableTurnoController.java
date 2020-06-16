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
import org.brandonsicay.bean.Area;
import org.brandonsicay.bean.Cargo;
import org.brandonsicay.bean.ResponsableTurno;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class ResponsableTurnoController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ResponsableTurno> listaResponsableTurno;
    private ObservableList<Area> listaArea;
    private ObservableList<Cargo> listaCargo;
    @FXML private TextField txtApellidos; 
    @FXML private TextField txtNombres; 
    @FXML private TextField txtTelefono;
    @FXML private ComboBox cmbArea;
    @FXML private ComboBox cmbCargo;
    @FXML private TableView tblResponsablesTurno;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colTelefono; 
    @FXML private TableColumn colArea;
    @FXML private TableColumn colCargo; 
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbArea.setItems(getAreas());
        cmbArea.setDisable(true);
        cmbCargo.setItems(getCargo());
        cmbCargo.setDisable(true);

    }
    
    public void cargarDatos(){
        tblResponsablesTurno.setItems(getResponsableTurno());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoResponsableTurno"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("nombreReponsable"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("apellidosResponsable"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, String>("telefonoPersonal"));
        colArea.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoArea"));
        colCargo.setCellValueFactory(new PropertyValueFactory<ResponsableTurno, Integer>("codigoCargo"));
        
    }
    
    
    public void SoloNumerosEnteros(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (!Character.isDigit(key))
            keyEvent.consume();

    } catch (Exception e){ 
        e.printStackTrace();
    }
}
    
   public void hora_Telefono(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla) || Character.isSpaceChar(tecla))
            event.consume();
        
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   }     
 
   public void soloLetras(KeyEvent event){
       try{
           char tecla = event.getCharacter().charAt(0);
           if(!(Character.isLetter(tecla) || Character.isSpaceChar(tecla))){
               event.consume();
           }
       }catch(Exception e){
           e.printStackTrace();
       }
   
   }

    
    
    public ObservableList<ResponsableTurno> getResponsableTurno(){
        ArrayList<ResponsableTurno> lista = new ArrayList<ResponsableTurno>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarResponsableTurno}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ResponsableTurno(resultado.getInt("codigoResponsableTurno"),
                                resultado.getString("nombreResponsable"),
                                resultado.getString("apellidosResponsable"),
                                resultado.getString("telefonoPersonal"),
                                resultado.getInt("codigoArea"),
                                resultado.getInt("codigoCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaResponsableTurno = FXCollections.observableList(lista);
    }
    
    public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarAreas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Area(resultado.getInt("codigoArea"),
                                resultado.getString("nombreArea")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaArea = FXCollections.observableList(lista);
    }
    
     public ObservableList<Cargo> getCargo(){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCargos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Cargo(resultado.getInt("codigoCargo"), 
                                resultado.getString("nombreCargo")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    
        return listaCargo = FXCollections.observableList(lista);
    }   
    
    
    public void seleccionarElementos(){   
        if(tblResponsablesTurno.getSelectionModel().getSelectedItem() != null){
        txtNombres.setText(((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getNombreReponsable());
        txtApellidos.setText(((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getApellidosResponsable());
        txtTelefono.setText(((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getTelefonoPersonal());
        cmbArea.getSelectionModel().select(buscarArea(((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getCodigoArea()));
        cmbCargo.getSelectionModel().select(buscarCargo(((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getCodigoCargo()));
        }else{
            tblResponsablesTurno.getSelectionModel().clearSelection();
        }   
    }

    public Area buscarArea(int codigoArea){
        Area resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarArea(?)}");
            procedimiento.setInt(1, codigoArea);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Area(registro.getInt("codigoArea"),
                            registro.getString("nombreArea"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return resultado;
    }
    
        public Cargo buscarCargo(int codigoCargo){
        Cargo resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCargo(?)}");
            procedimiento.setInt(1, codigoCargo);
            ResultSet registro = procedimiento.executeQuery(); 
            while(registro.next()){
                resultado = new Cargo(registro.getInt("codigoCargo"),
                            registro.getString("nombreCargo"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
       
        return resultado;
    }
    
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 activarControles();
                 limpiarControles();
                 tblResponsablesTurno.getSelectionModel().clearSelection();
                 tblResponsablesTurno.setOnKeyReleased(null);
                 tblResponsablesTurno.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 cmbArea.setDisable(false);
                 cmbCargo.setDisable(false);
                 limpiarControles();
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtApellidos.getText().isEmpty() || txtNombres.getText().isEmpty() || txtTelefono.getText().isEmpty() || cmbArea.getSelectionModel().isEmpty() || cmbCargo.getSelectionModel().isEmpty()){
                   JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{        
                guardar();
                desactivarControles();
                limpiarControles();
                tblResponsablesTurno.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblResponsablesTurno.setOnMouseClicked((event) -> {seleccionarElementos();
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
        ResponsableTurno registro = new ResponsableTurno();
        registro.setNombreReponsable(txtNombres.getText());
        registro.setApellidosResponsable(txtApellidos.getText());
        registro.setTelefonoPersonal(txtTelefono.getText());
        registro.setCodigoArea(((Area)cmbArea.getSelectionModel().getSelectedItem()).getCodigoArea());
        registro.setCodigoCargo(((Cargo)cmbCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarResponsableTurno(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreReponsable());
            procedimiento.setString(2, registro.getApellidosResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getCodigoArea());
            procedimiento.setInt(5, registro.getCodigoCargo());
            procedimiento.execute();
            listaResponsableTurno.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 tblResponsablesTurno.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblResponsablesTurno.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                 
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblResponsablesTurno.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eiliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarResponsableTurno(?)}");
                      procedimiento.setInt(1, ((ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem()).getCodigoResponsableTurno());
                      procedimiento.execute();
                      listaResponsableTurno.remove(tblResponsablesTurno.getSelectionModel().getSelectedItem());
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


    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblResponsablesTurno.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbArea.setDisable(true);
                    cmbCargo.setDisable(true);
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
                    cmbArea.setDisable(true);
                    cmbCargo.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();        
        
        }
    } 
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarResponsableTurno(?,?,?,?)}");
            ResponsableTurno registro = (ResponsableTurno)tblResponsablesTurno.getSelectionModel().getSelectedItem();
            registro.setNombreReponsable(txtNombres.getText());            
            registro.setApellidosResponsable(txtApellidos.getText());
            registro.setApellidosResponsable(txtTelefono.getText());
            procedimiento.setString(1, registro.getNombreReponsable());
            procedimiento.setString(2, registro.getApellidosResponsable());
            procedimiento.setString(3, registro.getTelefonoPersonal());
            procedimiento.setInt(4, registro.getCodigoResponsableTurno());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
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
                 cmbArea.setDisable(true);
                 cmbCargo.setDisable(true);
                 tblResponsablesTurno.getSelectionModel().clearSelection();
                 tipoDeOperacion = ResponsableTurnoController.operaciones.NINGUNO;
                 break;                
        }
                
    }
 
     public void modulos(){
        escenarioPrincipal.Modulos();
    }   
    

    public void desactivarControles(){
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtTelefono.setEditable(false);
        cmbArea.setDisable(true);
        cmbCargo.setDisable(true);
    }
        
    public void activarControles(){
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtTelefono.setEditable(true);
        
    }
    
    
    public void limpiarControles(){
        txtApellidos.setText("");
        txtNombres.setText("");
        txtTelefono.setText("");
        cmbArea.setValue(null);
        cmbCargo.setValue(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void Modulos(){
        escenarioPrincipal.Modulos();
    }
   
}