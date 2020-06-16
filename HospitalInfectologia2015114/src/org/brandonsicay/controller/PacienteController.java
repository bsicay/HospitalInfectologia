
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
import javafx.scene.layout.GridPane;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Paciente;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.report.GenerarReporte;
import org.brandonsicay.sistema.Principal;


public class PacienteController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Paciente> listaPaciente;
    private DatePicker fecha;
    @FXML private TextField txtDPI;
    @FXML private TextField txtApellido; 
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtOcupacion;
    @FXML private TextField txtSexo;
    @FXML private TableView tblPacientes;
    @FXML private GridPane grpFecha;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colLicenciaMedica;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colApellido;
    @FXML private TableColumn colEdad;
    @FXML private TableColumn colFechaNacimiento;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colOcupacion;
    @FXML private TableColumn colSexo;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/brandonsicay/resource/DatePicker.css");
        grpFecha.add(fecha, 0, 0);
        fecha.setDisable(true);
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
    
    public void DPI(KeyEvent keyEvent) {
    try{
        char key = keyEvent.getCharacter().charAt(0);

        if (txtDPI.getText().length() >= 13 || !Character.isDigit(key))
            keyEvent.consume();

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
    

    
    
    public void cargarDatos(){
        tblPacientes.setItems(getPacientes());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("idPaciente"));
        colDPI.setCellValueFactory(new PropertyValueFactory<Paciente, String>("DPI"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Paciente, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Paciente, String>("apellido"));
        colFechaNacimiento.setCellValueFactory(new PropertyValueFactory<Paciente, Date>("fechaNacimiento"));
        colEdad.setCellValueFactory(new PropertyValueFactory<Paciente, Integer>("edad"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("Direccion"));
        colOcupacion.setCellValueFactory(new PropertyValueFactory<Paciente, String>("ocupacion"));
        colSexo.setCellValueFactory(new PropertyValueFactory<Paciente, String>("sexo"));
        
    }
    
    public ObservableList<Paciente> getPacientes(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarPaciente");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
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
    
    
    public void seleccionarElementos(){
        if(tblPacientes.getSelectionModel().getSelectedItem() != null){
        txtDPI.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDPI());
        txtApellido.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getApellido());
        txtNombre.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getNombre());
        fecha.selectedDateProperty().set(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getFechaNacimiento());
        txtDireccion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getDireccion());
        txtOcupacion.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getOcupacion());
        txtSexo.setText(((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getSexo());
        }else{
            tblPacientes.getSelectionModel().clearSelection();
        }
        
    }

    public Paciente buscarPaciente(int idPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, idPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("idPaciente"),
                            registro.getString("DPI"),
                            registro.getString("apellido"),
                            registro.getString("nombre"),
                            registro.getDate("fechaNacimiento"),
                            registro.getInt("edad"),
                            registro.getString("direccion"),
                            registro.getString("ocupacion"),
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
                 tblPacientes.getSelectionModel().clearSelection();
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperacion = PacienteController.operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtDPI.getText().isEmpty() || txtNombre.getText().isEmpty() || txtApellido.getText().isEmpty() || txtDireccion.getText().isEmpty() || txtOcupacion.getText().isEmpty() || txtSexo.getText().isEmpty()){
                   JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{                
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                cargarDatos();
                tipoDeOperacion = PacienteController.operaciones.NINGUNO;
                escenarioPrincipal.ContactoUrgencia();
                 break;
                } 
        }
    }
    
    public void guardar(){
        Paciente registro = new Paciente();
        registro.setDPI(txtDPI.getText());
        registro.setApellido(txtApellido.getText());
        registro.setNombre(txtNombre.getText());
        registro.setFechaNacimiento(fecha.getSelectedDate());
        registro.setDireccion(txtDireccion.getText());
        registro.setOcupacion(txtOcupacion.getText());
        registro.setSexo(txtSexo.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarPaciente(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellido());
            procedimiento.setString(3, registro.getNombre());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo()); 
            procedimiento.execute();
            listaPaciente.add(registro);
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
                 tipoDeOperacion = PacienteController.operaciones.NINGUNO;
                 break;
            default:
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar este registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarPaciente(?)}");
                        procedimiento.setInt(1, ((Paciente)tblPacientes.getSelectionModel().getSelectedItem()).getIdPaciente());
                        procedimiento.execute();
                        listaPaciente.remove(tblPacientes.getSelectionModel().getSelectedItem());
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
                if(tblPacientes.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarPaciente(?,?,?,?,?,?,?,?)}");
            Paciente registro = (Paciente)tblPacientes.getSelectionModel().getSelectedItem();
            registro.setDPI(txtDPI.getText());
            registro.setApellido(txtApellido.getText());
            registro.setNombre(txtNombre.getText());
            registro.setFechaNacimiento(fecha.getSelectedDate());
            registro.setDireccion(txtDireccion.getText());
            registro.setOcupacion(txtOcupacion.getText());
            registro.setSexo(txtSexo.getText());
            procedimiento.setString(1, registro.getDPI());
            procedimiento.setString(2, registro.getApellido());
            procedimiento.setString(3, registro.getNombre());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaNacimiento().getTime()));
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getOcupacion());
            procedimiento.setString(7, registro.getSexo());
            procedimiento.setInt(8, registro.getIdPaciente());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }  
    
   
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                 desactivarControles();
                 limpiarControles();                // CANCELAR
                 btnReporte.setText("Reporte");                
                 btnEditar.setText("Editar");
                 btnNuevo.setDisable(false);
                 btnEliminar.setDisable(false);  
                 tblPacientes.getSelectionModel().clearSelection();
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
        parametros.put("idPaciente", null); 
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de Pacientes", parametros);
    }    

    public void desactivarControles(){
        txtDPI.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtOcupacion.setEditable(false);
        txtDireccion.setEditable(false);
        txtSexo.setEditable(false);
        fecha.setDisable(true);
    }
        
    public void activarControles(){
        txtDPI.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtOcupacion.setEditable(true);
        txtDireccion.setEditable(true);
        txtSexo.setEditable(true);
        fecha.setDisable(false);
    }
    
    public void limpiarControles(){
        txtDPI.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtOcupacion.setText("");
        txtDireccion.setText("");
        txtSexo.setText("");
        fecha.setSelectedDate(null);
    }   

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void contactoUrgencia(){
        escenarioPrincipal.ContactoUrgencia();
    }
    
    public void modulos(){
        escenarioPrincipal.Modulos();
    }    
    
   
}
   
