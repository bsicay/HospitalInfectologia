
package org.brandonsicay.controller;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
import javax.swing.JOptionPane;
import org.brandonsicay.bean.TipoUsuario;
import org.brandonsicay.bean.Usuarios;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;

public class UsuarioController implements Initializable{
    private enum operaciones{NUEVO, NINGUNO, EDITAR, GUARDAR, ELIMINAR}
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Usuarios> listaUsuario;
    private ObservableList<TipoUsuario> listaTipoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private ComboBox cmbTipoUsuario;
    @FXML private TableView tblUsuarios;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colUsuario;
    @FXML private TableColumn colContraseña;
    @FXML private TableColumn colEstado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colTipoUsuario;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbTipoUsuario.setItems(getTipoUsuario());
        cmbTipoUsuario.setDisable(true);
        btnCancelar.setVisible(false);
    }
    
    public void cargarDatos(){
        tblUsuarios.setItems(getUsuarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Usuarios, Integer>("codigoUsuario"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("usuarioLogin"));
        colContraseña.setCellValueFactory(new PropertyValueFactory<Usuarios, String>("usuarioContraseña"));
        colEstado.setCellValueFactory(new PropertyValueFactory<Usuarios, Boolean>("usuarioEstado"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Usuarios, Date>("usuarioFecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Usuarios, Time>("usuarioHora"));
        colTipoUsuario.setCellValueFactory(new PropertyValueFactory<Usuarios, Integer>("codigoTipoUsuario"));
    }

    
    public ObservableList<Usuarios>getUsuarios(){
        ArrayList<Usuarios> lista = new ArrayList<Usuarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuarios(resultado.getInt("codigoUsuario"),
                                resultado.getString("usuarioLogin"),
                                resultado.getString("usuarioContraseña"),
                                resultado.getBoolean("usuarioEstado"),
                                resultado.getDate("usuarioFecha"),
                                resultado.getTime("usuarioHora"),
                                resultado.getInt("codigoTipoUsuario")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableList(lista);
    } 
    
    public ObservableList<TipoUsuario>getTipoUsuario(){
        ArrayList<TipoUsuario> lista = new ArrayList<TipoUsuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTipoUsuario}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoUsuario(resultado.getInt("codigoTipoUsuario"),
                                resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoUsuario = FXCollections.observableList(lista);
    
    }
    
    
    public void seleccionarDatos(){
        if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
            txtUsuario.setText(((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioLogin());
            txtPassword.setText(((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem()).getUsuarioContraseña());
            cmbTipoUsuario.getSelectionModel().select(buscarTipoUsuario(((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario()));
        }else{
            tblUsuarios.getSelectionModel().clearSelection();
        }
    }    
    
    public TipoUsuario buscarTipoUsuario(int codigoTipo){
        TipoUsuario resultado = new TipoUsuario();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTipoUsuario(?)}");
            procedimiento.setInt(1, codigoTipo);
            ResultSet dato = procedimiento.executeQuery();
            while(dato.next()){
                resultado = new TipoUsuario(dato.getInt("codigoTipoUsuario"),
                                    dato.getString("descripcion"));
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
                 tblUsuarios.getSelectionModel().clearSelection();
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 cmbTipoUsuario.setDisable(false);
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtUsuario.getText().isEmpty() || txtPassword.getText().isEmpty() || cmbTipoUsuario.getSelectionModel().isEmpty()){
                   JOptionPane.showMessageDialog(null, "Llene todos los datos");
                }else{        
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                }
        }
    }
    
    
    public void guardar(){
        Usuarios registro = new Usuarios();
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setUsuarioContraseña(txtPassword.getText());
        registro.setCodigoTipoUsuario(((TipoUsuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?)}");
            procedimiento.setString(1, registro.getUsuarioLogin());
            procedimiento.setString(2, registro.getUsuarioContraseña());
            procedimiento.setInt(3, registro.getCodigoTipoUsuario());
            procedimiento.execute();
            listaUsuario.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }   

    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                tblUsuarios.getSelectionModel().clearSelection();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;                
            default:
                if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                   int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el usuario?", "ELIMINAR USUARIO", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                   if(respuesta == JOptionPane.YES_OPTION){
                       try{
                           PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarUsuarios(?)}");
                           procedimiento.setInt(1, ((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
                           procedimiento.execute();
                           cargarDatos();
                       }catch(Exception e){
                           e.printStackTrace();
                       }
                   }
                }else{
                    JOptionPane.showMessageDialog(null,"Seleccione un usuario");
                }
        }
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case EDITAR:
                tblUsuarios.getSelectionModel().clearSelection();
                limpiarControles();
                desactivarControles();
                btnEditar.setText("Editar");
                btnCancelar.setVisible(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;                
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion ){
            case NINGUNO:
                if(tblUsuarios.getSelectionModel().getSelectedItem() != null){
                    cmbTipoUsuario.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnCancelar.setVisible(true);
                    activarControles();
                    tipoDeOperacion = operaciones.EDITAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione un usuario");
                }
                break;
            case EDITAR:   
                    actualizar();
                    cmbTipoUsuario.setDisable(true);
                    btnEditar.setText("Editar");
                    btnCancelar.setVisible(false);
                    desactivarControles(); 
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
        }
    }
    
    public void actualizar(){
        try{
        Usuarios registro = ((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setUsuarioContraseña(txtPassword.getText());
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarUsuario(?,?,?)}");
        procedimiento.setString(1, registro.getUsuarioLogin());
        procedimiento.setString(2, registro.getUsuarioContraseña());
        procedimiento.setInt(3,((Usuarios)tblUsuarios.getSelectionModel().getSelectedItem()).getCodigoUsuario());
        procedimiento.execute();
        cargarDatos();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void activarControles(){
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
    }  
    
    public void limpiarControles(){
        txtUsuario.setText("");
        txtPassword.setText("");
        cmbTipoUsuario.setValue(null);
    }
    
    public void desactivarControles(){
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        cmbTipoUsuario.setDisable(true);
    }
      
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void login(){
        escenarioPrincipal.login();
    }

    
}
