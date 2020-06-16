
package org.brandonsicay.controller;

import static java.lang.String.format;
import static java.lang.String.format;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.converter.DateTimeStringConverter;
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Medico;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.report.GenerarReporte;
import org.brandonsicay.sistema.Principal;


public class MedicoController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medico> listaMedico;
    @FXML private TextField txtLicenciaMedica;
    @FXML private TextField txtNombres; 
    @FXML private TextField txtApellidos;
    @FXML private TextField txtHoraEntrada;
    @FXML private TextField txtHoraSalida;
    @FXML private TextField txtTurno;
    @FXML private TextField txtSexo;
    @FXML private TableView tblMedicos;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colEntrada;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colTurno;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();     
    }
    
    public void cargarDatos(){
        tblMedicos.setItems(getMedicos());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("codigoMedico"));
        colLicenciaMedica.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("licenciaMedica"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Medico, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Medico, String>("apellidos"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Medico, String>("horaSalida"));
        colTurno.setCellValueFactory(new PropertyValueFactory<Medico, Integer>("turnoMaximo"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Medico, String>("sexo"));
        
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
   
   public void horaEntrada(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla)|| Character.isSpaceChar(tecla) || txtHoraEntrada.getText().length()>=8)
            event.consume();
        else if(txtHoraEntrada.getText().length()==2 || txtHoraEntrada.getText().length()==5){
            txtHoraEntrada.appendText(":");
        }
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   }
   
   public void horaSalida(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla)|| Character.isSpaceChar(tecla) || txtHoraSalida.getText().length()>=8)
            event.consume();
        else if(txtHoraSalida.getText().length()==2 || txtHoraSalida.getText().length()==5 ){
            txtHoraSalida.appendText(":");
        }
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
        if(tblMedicos.getSelectionModel().getSelectedItem() != null ){
        txtLicenciaMedica.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getLicenciaMedica()));
        txtNombres.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getNombres());
        txtApellidos.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getApellidos());
        txtHoraEntrada.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraEntrada());
        txtHoraSalida.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getHoraSalida());
        txtTurno.setText(String.valueOf(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getTurnoMaximo()));
        txtSexo.setText(((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getSexo());
        }else{
            tblMedicos.getSelectionModel().clearSelection();
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
    
    
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                 activarControles();
                 limpiarControles();
                 tblMedicos.setOnKeyReleased(null);
                 tblMedicos.setOnMouseClicked(null);
                 tblMedicos.getSelectionModel().clearSelection();
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtNombres.getText().isEmpty() || txtApellidos.getText().isEmpty() || txtHoraEntrada.getText().isEmpty() || txtHoraSalida.getText().isEmpty() || txtSexo.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{              
                guardar();
                desactivarControles();
                limpiarControles();
                tblMedicos.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblMedicos.setOnMouseClicked((event) -> {seleccionarElementos();
                });
                tblMedicos.setOnMouseClicked(null);                
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                }
        }
    }
    
    public void guardar(){
        Medico registro = new Medico();
        registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
        registro.setNombres(txtNombres.getText());
        registro.setApellidos(txtApellidos.getText());
        registro.setHoraEntrada(txtHoraEntrada.getText());
        registro.setHoraSalida(txtHoraSalida.getText());
        registro.setSexo(txtSexo.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMedico(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getHoraEntrada());
            procedimiento.setString(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo()); 
            procedimiento.execute();
            listaMedico.add(registro);
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
                 btnEliminar.setText("Eliminar");                   // CANCELAR
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tblMedicos.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblMedicos.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Eliminar Medico", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMedico(?)}");
                            procedimiento.setInt(1, ((Medico)tblMedicos.getSelectionModel().getSelectedItem()).getCodigoMedico());
                            procedimiento.execute();
                            listaMedico.remove(tblMedicos.getSelectionModel().getSelectedItem());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                
        }
    }

    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblMedicos.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarMedico(?,?,?,?,?,?,?)}");
            Medico registro = (Medico)tblMedicos.getSelectionModel().getSelectedItem();
            registro.setLicenciaMedica(Integer.parseInt(txtLicenciaMedica.getText()));
            registro.setNombres(txtNombres.getText());
            registro.setApellidos(txtApellidos.getText());
            registro.setHoraEntrada(txtHoraEntrada.getText());
            registro.setHoraSalida(txtHoraSalida.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setInt(1, registro.getLicenciaMedica());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getApellidos());
            procedimiento.setString(4, registro.getHoraEntrada());
            procedimiento.setString(5, registro.getHoraSalida());
            procedimiento.setString(6, registro.getSexo());
            procedimiento.setInt(7, registro.getCodigoMedico());
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
                 tblMedicos.getSelectionModel().clearSelection();
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;       
            case  NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
        }
                
    }
    
    public void imprimirReporte(){    
        Map parametros = new HashMap();
      //  parametros.clear();
      //  parametros.put("logo", this.getClass().getResourceAsStream(logo));
        parametros.put("codigoMedico", null);
        GenerarReporte.mostrarReporte("ReporteMedicos.jasper", "Reporte de Medicos", parametros);
    }
    

    public void desactivarControles(){
        txtLicenciaMedica.setEditable(false);
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtHoraEntrada.setEditable(false);
        txtHoraSalida.setEditable(false);
        txtTurno.setEditable(false);
        txtSexo.setEditable(false);
    }
        
    public void activarControles(){
        txtLicenciaMedica.setEditable(true);
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtHoraEntrada.setEditable(true);
        txtHoraSalida.setEditable(true);
        txtTurno.setEditable(false);
        txtSexo.setEditable(true);
    }
    
    public void limpiarControles(){
        txtLicenciaMedica.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtTurno.setText("");
        txtSexo.setText("");
        txtHoraEntrada.setText("");
        txtHoraSalida.setText("");
    }
    
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal){
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void Modulos(){
        escenarioPrincipal.Modulos();
    }
    
    public void telefonoMedicos(){
        escenarioPrincipal.telefonoMedicos();
    }   
    
    public void horarios(){
        escenarioPrincipal.horarios();
    }
   
}
