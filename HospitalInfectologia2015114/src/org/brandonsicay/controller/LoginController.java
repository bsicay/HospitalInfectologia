package org.brandonsicay.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.brandonsicay.bean.TipoUsuario;
import org.brandonsicay.bean.Usuarios;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.report.GenerarReporte;
import org.brandonsicay.sistema.Principal;


public class LoginController implements Initializable{
    private Principal escenarioPrincipal;
    private ObservableList<TipoUsuario>listaTipoUsuario;
    private ObservableList<Usuarios>listaUsuario;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField pswPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Button btnCancel;
    @FXML private Button btnReporte;
    @FXML private ComboBox cmbTipoDeUsuario;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoDeUsuario.setItems(getTipoUsuario());
        btnRegister.setOnMouseClicked(null);
        btnRegister.setOnMouseClicked(null);    
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
    
    public void report(){
         if(txtUsuario.getText().isEmpty() || pswPassword.getText().isEmpty() || cmbTipoDeUsuario.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");  
        }else{
            for(int i=0; i < getUsuarios().size(); i++){
             if(txtUsuario.getText().equals(getUsuarios().get(i).getUsuarioLogin()) && pswPassword.getText().equals(getUsuarios().get(i).getUsuarioContraseña()) && ((((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem())).getCodigoTipoUsuario()) == getUsuarios().get(i).getCodigoTipoUsuario() && ((((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem())).getCodigoTipoUsuario()) != 3){
                imprimirReporte();
                break;
             }else if(i >= getUsuarios().size() -1 ){
                JOptionPane.showMessageDialog(null, "El usuario no existe o no tiene permitido ingresar usuarios");
            }
        }           
        }       
    }
    
    public void register(){
        if(txtUsuario.getText().isEmpty() || pswPassword.getText().isEmpty() || cmbTipoDeUsuario.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");  
        }else{
            for(int i=0; i < getUsuarios().size(); i++){
             if(txtUsuario.getText().equals(getUsuarios().get(i).getUsuarioLogin()) && pswPassword.getText().equals(getUsuarios().get(i).getUsuarioContraseña()) && ((((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem())).getCodigoTipoUsuario()) == getUsuarios().get(i).getCodigoTipoUsuario() && ((((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem())).getCodigoTipoUsuario()) != 3){
                registrar();
                break;
             }else if(i >= getUsuarios().size() -1 ){
                JOptionPane.showMessageDialog(null, "El usuario no existe o no tiene permitido ingresar usuarios");
            }
        }           
        }
    }
    
    
    public void inicioSesion(){
        if(txtUsuario.getText().isEmpty() || pswPassword.getText().isEmpty() || cmbTipoDeUsuario.getSelectionModel().isEmpty()){
            JOptionPane.showMessageDialog(null, "Ingrese todos los datos");  
        }else{
            for(int i=0; i < getUsuarios().size(); i++){
             if(txtUsuario.getText().equals(getUsuarios().get(i).getUsuarioLogin()) && pswPassword.getText().equals(getUsuarios().get(i).getUsuarioContraseña()) && ((((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem())).getCodigoTipoUsuario()) == getUsuarios().get(i).getCodigoTipoUsuario()){
                JOptionPane.showMessageDialog(null, "BIENVENIDO");
                try{
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_Hora_Fecha(?)");
                    procedimiento.setInt(1, ((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
                    procedimiento.executeQuery();
                }catch(Exception e){
                    e.printStackTrace();
                }
                menu();
                break;
             }else if(i >= getUsuarios().size() -1 ){
                JOptionPane.showMessageDialog(null, "El usuario no existe o no tiene permitido ingresar usuarios");
            }
        }           
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoUsuario", null);
        GenerarReporte.mostrarReporte("Reporte.jasper", "Reporte Usuario", parametros);
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_Hora_Fecha(?)");
            procedimiento.setInt(1, ((TipoUsuario)cmbTipoDeUsuario.getSelectionModel().getSelectedItem()).getCodigoTipoUsuario());
            procedimiento.executeQuery();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void cancelar(){
        txtUsuario.setText("");
        pswPassword.setText("");
        cmbTipoDeUsuario.setValue(null);
    }
    
    public void registrar(){
        escenarioPrincipal.registrar();
    }
    
    public void menu(){
        escenarioPrincipal.menuPrincipal();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void registrarse(){
        escenarioPrincipal.registrar();
    }
}




   
