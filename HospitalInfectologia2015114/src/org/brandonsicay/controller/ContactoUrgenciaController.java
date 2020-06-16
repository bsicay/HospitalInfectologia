
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
import org.brandonsicay.bean.ContactoUrgencia;
import org.brandonsicay.bean.Paciente;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class ContactoUrgenciaController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ContactoUrgencia> listaContactoUrgencia;
    private ObservableList<Paciente> listaPaciente;
    @FXML private TextField txtApellidos; 
    @FXML private TextField txtNombres; 
    @FXML private TextField txtNumeroContacto;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private TableView tblContactosUrgencia;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colNumeroContacto; 
    @FXML private TableColumn colCodigoPaciente; 
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPaciente.setItems(getPacientes());
        cmbCodigoPaciente.setDisable(true);
    }
    
    public void cargarDatos(){
        tblContactosUrgencia.setItems(getContactoUrgencia());
        colCodigo.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoContactoUrgencia"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("apellidos"));
        colNombres.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("nombres"));
        colNumeroContacto.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, String>("numeroContacto"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<ContactoUrgencia, Integer>("codigoPaciente"));
        
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
    
   public void telefono(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla) || Character.isSpaceChar(tecla) || txtNumeroContacto.getText().length() >= 9)
            event.consume();
        else if(txtNumeroContacto.getText().length()==4){
            txtNumeroContacto.appendText("-");
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

    
    
    public ObservableList<ContactoUrgencia> getContactoUrgencia(){
        ArrayList<ContactoUrgencia> lista = new ArrayList<ContactoUrgencia>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarContactoUrgencia}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new ContactoUrgencia(resultado.getInt("codigoContactoUrgencia"),
                                resultado.getString("apellidos"),
                                resultado.getString("nombres"),
                                resultado.getString("numeroContacto"),
                                resultado.getInt("codigoPaciente")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaContactoUrgencia = FXCollections.observableList(lista);
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
        if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
        txtApellidos.setText(((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getApellidos());
        txtNombres.setText(((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getNombres());
        txtNumeroContacto.setText(((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getNumeroContacto());
        cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
        }else{
            tblContactosUrgencia.getSelectionModel().clearSelection();
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
                 tblContactosUrgencia.getSelectionModel().clearSelection();
                 tblContactosUrgencia.setOnKeyReleased(null);
                 tblContactosUrgencia.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 cmbCodigoPaciente.setDisable(false);
                 limpiarControles();
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
                if(txtApellidos.getText().isEmpty() || txtNombres.getText().isEmpty() || cmbCodigoPaciente.getSelectionModel().isEmpty()){
                   JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
                }else{        
                guardar();
                desactivarControles();
                limpiarControles();
                tblContactosUrgencia.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblContactosUrgencia.setOnMouseClicked((event) -> {seleccionarElementos();
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
        ContactoUrgencia registro = new ContactoUrgencia();
        registro.setApellidos(txtApellidos.getText());
        registro.setNombres(txtNombres.getText());
        registro.setNumeroContacto(txtNumeroContacto.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getIdPaciente());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarContactoUrgencia(?,?,?,?)}");
            procedimiento.setString(1, registro.getApellidos());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoPaciente());
            procedimiento.execute();
            listaContactoUrgencia.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                 desactivarControles();
                 limpiarControles();
                 tblContactosUrgencia.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblContactosUrgencia.setOnMouseClicked((event) -> {seleccionarElementos();
                 });                 
                 btnNuevo.setText("Nuevo");
                 btnEliminar.setText("Eliminar");
                 btnEditar.setDisable(false);
                 btnReporte.setDisable(false);  
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eiliminar el registro?", "Eliminar registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  if(respuesta == JOptionPane.YES_OPTION){
                      try{
                      PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarContactoUrgencia(?)}");
                      procedimiento.setInt(1, ((ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem()).getCodigoContactoUrgencia());
                      procedimiento.execute();
                      listaContactoUrgencia.remove(tblContactosUrgencia.getSelectionModel().getSelectedItem());
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

    // https://dzone.com/articles/javafx-numbertextfield-and
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblContactosUrgencia.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    cmbCodigoPaciente.setDisable(true);
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
                    cmbCodigoPaciente.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();        
        
        }
    } 
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarContactoUrgencia(?,?,?,?)}");
            ContactoUrgencia registro = (ContactoUrgencia)tblContactosUrgencia.getSelectionModel().getSelectedItem();
            registro.setApellidos(txtApellidos.getText());
            registro.setNombres(txtNombres.getText());
            registro.setNumeroContacto(txtNumeroContacto.getText());
          // registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getIdPaciente());
            procedimiento.setString(1, registro.getApellidos());
            procedimiento.setString(2, registro.getNombres());
            procedimiento.setString(3, registro.getNumeroContacto());
            procedimiento.setInt(4, registro.getCodigoContactoUrgencia());
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
                 cmbCodigoPaciente.setDisable(true);
                 tblContactosUrgencia.getSelectionModel().clearSelection();
                 tipoDeOperacion = ContactoUrgenciaController.operaciones.NINGUNO;
                 break;                
        }
                
    }
    

    public void desactivarControles(){
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtNumeroContacto.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
    }
        
    public void activarControles(){
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtNumeroContacto.setEditable(true);
        
    }
    
    
    public void limpiarControles(){
        txtApellidos.setText("");
        txtNombres.setText("");
        txtNumeroContacto.setText("");
        cmbCodigoPaciente.setValue(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void pacientes(){
        escenarioPrincipal.Pacientes();
    }
}
   