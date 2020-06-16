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
import org.brandonsicay.bean.Area;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class AreasController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Area> listaArea;
    @FXML private TextField txtNombre; 
    @FXML private TableView tblAreas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colArea;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
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
   


    
    public void cargarDatos(){
        tblAreas.setItems(getAreas());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Area, Integer>("codigoArea"));
        colArea.setCellValueFactory(new PropertyValueFactory<Area, String>("nombreArea"));
        
    }
    
    public ObservableList<Area> getAreas(){
        ArrayList<Area> lista = new ArrayList<Area>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarAreas}");
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
    
    public void seleccionarElementos(){
        if(tblAreas.getSelectionModel().getSelectedItem() != null)
            txtNombre.setText(((Area)tblAreas.getSelectionModel().getSelectedItem()).getNombreArea());
        else{
            tblAreas.getSelectionModel().clearSelection();
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
        
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 activarControles();
                 limpiarControles();
                 tblAreas.getSelectionModel().clearSelection();
                 tblAreas.setOnKeyReleased(null);
                 tblAreas.setOnMouseClicked(null);
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
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tblAreas.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblAreas.setOnMouseClicked((event) -> {seleccionarElementos();
                });                
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                 break;
               }
        }
    }
    
    public void guardar(){
        Area registro = new Area();
        registro.setNombreArea(txtNombre.getText());;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarAreas(?)}");
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.execute();
            listaArea.add(registro);   
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    

    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tblAreas.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblAreas.setOnMouseClicked((event) -> {seleccionarElementos();
                 });
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ElimiarAreas(?)}");
                        procedimiento.setInt(1, ((Area)tblAreas.getSelectionModel().getSelectedItem()).getCodigoArea());
                        procedimiento.execute();
                        listaArea.remove(tblAreas.getSelectionModel().getSelectedItem());
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
                if(tblAreas.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarAreas(?,?)}");
            Area registro = (Area)tblAreas.getSelectionModel().getSelectedItem();
            registro.setNombreArea(txtNombre.getText());
            procedimiento.setString(1, registro.getNombreArea());
            procedimiento.setInt(2, registro.getCodigoArea());
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
                 tblAreas.getSelectionModel().clearSelection();
                 tipoDeOperacion = AreasController.operaciones.NINGUNO;
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
