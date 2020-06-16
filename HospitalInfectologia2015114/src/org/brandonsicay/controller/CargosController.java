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
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Cargo;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class CargosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Cargo> listaCargo;
    @FXML private TextField txtNombre; 
    @FXML private TableView tblCargos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colCargo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
 

    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblCargos.setItems(getCargo());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer>("codigoCargo"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String>("nombreCargo"));
        
    }
    
    public void seleccionarElementos(){
        if(tblCargos.getSelectionModel().getSelectedItem() != null)
        txtNombre.setText(((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getNombreCargo());
        else{
            tblCargos.getSelectionModel().clearSelection();
        }       
        
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
                tblCargos.getSelectionModel().clearSelection();
                tblCargos.setOnKeyReleased(null);
                tblCargos.setOnMouseClicked(null);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
               if(txtNombre.getText().isEmpty())
                  JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
               else{                
                guardar();
                desactivarControles();
                limpiarControles();
                tblCargos.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblCargos.setOnMouseClicked((event) -> {seleccionarElementos();
                });                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
               }
        }
    }
    
    public void guardar(){
        Cargo registro = new Cargo();
        if(txtNombre.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "¡¡UNO O MÁS CAMPOS ESTÁN VACIOS!!");
        }else{
            registro.setNombreCargo(txtNombre.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCargos(?)}");
                procedimiento.setString(1, registro.getNombreCargo());
                procedimiento.execute();
                listaCargo.add(registro);
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
                tblCargos.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblCargos.setOnMouseClicked((event) -> {seleccionarElementos();
                });                

                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ElimiarCargo(?)}");
                        procedimiento.setInt(1, ((Cargo)tblCargos.getSelectionModel().getSelectedItem()).getCodigoCargo());
                        procedimiento.execute();
                        listaCargo.remove(tblCargos.getSelectionModel().getSelectedItem());
                        limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un registro");
                }
        }
    }
    
     public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCargos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
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
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();        
        
        }
    } 
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarCargo(?,?)}");
            Cargo registro = (Cargo)tblCargos.getSelectionModel().getSelectedItem();
            registro.setNombreCargo(txtNombre.getText());
            procedimiento.setString(1, registro.getNombreCargo());
            procedimiento.setInt(2, registro.getCodigoCargo());
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
                 tblCargos.getSelectionModel().clearSelection();
                 tipoDeOperacion = CargosController.operaciones.NINGUNO;
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