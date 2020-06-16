
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Especialidad;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;



public class EspecialidadesController implements Initializable {
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Especialidad> listaEspecialidad;
    @FXML private TextField txtNombre; 
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

    }
    
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidades());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Especialidad, Integer>("codigoEspecialidad"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<Especialidad, String>("nombreEspecialidad"));
        
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
    
    public void seleccionarElementos(){
        if(tblEspecialidades.getSelectionModel().getSelectedItem() != null)
            txtNombre.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getNombreEspecialidad());
        else{
            tblEspecialidades.getSelectionModel().clearSelection();
        }
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

    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 activarControles();
                 limpiarControles();
                 tblEspecialidades.getSelectionModel().clearSelection();
                 tblEspecialidades.setOnKeyReleased(null);
                 tblEspecialidades.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                tblEspecialidades.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblEspecialidades.setOnMouseClicked((event) -> {seleccionarElementos();
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
    
    public void guardar(){
        Especialidad registro = new Especialidad();
        if(txtNombre.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");

        }else{
        registro.setNombreEspecialidad(txtNombre.getText());;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
            procedimiento.setString(1, registro.getNombreEspecialidad());
            procedimiento.execute();
            listaEspecialidad.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    }
    

    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 tblEspecialidades.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblEspecialidades.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eiliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}");
                      procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                      procedimiento.execute();
                      listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedItem());
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
                if(tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                activarControles();
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
   
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                desactivarControles();
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();     
                break;
        }
    }
    
    public void actualizar(){
        try{
           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarEspecialidad(?,?)}");
           Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
           registro.setNombreEspecialidad(txtNombre.getText());
           procedimiento.setString(1, registro.getNombreEspecialidad());
           procedimiento.setInt(2, registro.getCodigoEspecialidad());
           procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                 desactivarControles();
                 limpiarControles();     
                 btnReporte.setText("Reporte");                
                 btnEditar.setText("Editar");                 
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false); 
                 tblEspecialidades.getSelectionModel().clearSelection();
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;               
        }
    }
    

    public void desactivarControles(){
        txtNombre.setEditable(false);
    }
        
    public void activarControles(){
        txtNombre.setEditable(true);
    }
    
    public void limpiarControles(){
        txtNombre.setText("");
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
