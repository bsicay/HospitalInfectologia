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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;
import org.brandonsicay.bean.Horarios;
import org.brandonsicay.db.Conexion;
import org.brandonsicay.sistema.Principal;


public class HorariosController implements Initializable{
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private Principal escenarioPrincipal;
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Horarios> listaHorario;
    @FXML private TextField txtInicio; 
    @FXML private TextField txtSalida; 
    @FXML private CheckBox cbLunes;
    @FXML private CheckBox cbMartes;
    @FXML private CheckBox cbMiercoles;
    @FXML private CheckBox cbJueves;
    @FXML private CheckBox cbViernes;
    @FXML private TableView tblHorarios;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn coIInicio;
    @FXML private TableColumn colSalida;
    @FXML private TableColumn colLunes;
    @FXML private TableColumn colMartes;
    @FXML private TableColumn colMiercoles;
    @FXML private TableColumn colJueves;
    @FXML private TableColumn colViernes;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos(){
        tblHorarios.setItems(getHorarios());
        colCodigo.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("codigoHorario"));
        coIInicio.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioInicio"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horarios, String>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
        
    }
    
   public void horaEntrada(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla)|| Character.isSpaceChar(tecla) || txtInicio.getText().length()>=8)
            event.consume();
        else if(txtInicio.getText().length()==2 || txtInicio.getText().length()==5){
            txtInicio.appendText(":");
        }
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   }    
   
   public void horaSalida(KeyEvent event){
     try{
        char tecla = event.getCharacter().charAt(0);
        if(Character.isLetter(tecla)|| Character.isSpaceChar(tecla) || txtSalida.getText().length()>=8)
            event.consume();
        else if(txtSalida.getText().length()==2 || txtSalida.getText().length()==5){
            txtSalida.appendText(":");
        }
    }catch (Exception e){ 
        e.printStackTrace();
    }  
   }    

    
    
    public ObservableList<Horarios> getHorarios(){
        ArrayList<Horarios> lista = new ArrayList<Horarios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarHorario}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Horarios(resultado.getInt("codigoHorario"),
                                resultado.getString("horarioInicio"),
                                resultado.getString("horarioSalida"),
                                resultado.getBoolean("lunes"),
                                resultado.getBoolean("martes"),
                                resultado.getBoolean("miercoles"),
                                resultado.getBoolean("jueves"),
                                resultado.getBoolean("viernes")));
                                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaHorario = FXCollections.observableList(lista);
    }
    
    public void seleccionarElementos(){
        if(tblHorarios.getSelectionModel().getSelectedItem() != null){
            txtInicio.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioInicio());
            txtSalida.setText(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida());
            cbLunes.setSelected(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            cbMartes.setSelected(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            cbMiercoles.setSelected(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            cbJueves.setSelected(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            cbViernes.setSelected(((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
   
    }else{
            tblHorarios.getSelectionModel().clearSelection();
        }
    }

    
    public Horarios buscarHorario(int codigoHorario){
        Horarios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarHorariohorarioInicio(?)}");
            procedimiento.setInt(1, codigoHorario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Horarios(registro.getInt("codigoHorario"),
                            registro.getString("horarioInicio"),
                            registro.getString("horarioSalida"),
                            registro.getBoolean("lunes"),
                            registro.getBoolean("martes"),
                            registro.getBoolean("miercoles"),
                            registro.getBoolean("jueves"),
                            registro.getBoolean("viernes"));
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
                 tblHorarios.getSelectionModel().clearSelection();
                 tblHorarios.setOnKeyReleased(null);
                 tblHorarios.setOnMouseClicked(null);
                 btnNuevo.setText("Guardar");
                 btnEliminar.setText("Cancelar");
                 btnEditar.setDisable(true);
                 btnReporte.setDisable(true);
                 tipoDeOperacion = operaciones.GUARDAR;
                 break;
            case GUARDAR:
               if(txtInicio.getText().isEmpty() || txtSalida.getText().isEmpty() || !cbLunes.isSelected())
                  JOptionPane.showMessageDialog(null, "UNO O MÁS CAMPOS ESTÁN VACIOS!!!");
               else{
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnReporte.setDisable(false);
                btnEditar.setDisable(false);
                tblHorarios.setOnKeyReleased((event) -> {seleccionarElementos();
                });
                tblHorarios.setOnMouseClicked((event) -> {seleccionarElementos();
                });                
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                 break;
               }
        }
    }

    public void guardar(){
        Horarios registro = new Horarios();
        registro.setHorarioInicio(txtInicio.getText());
        registro.setHorarioSalida(txtSalida.getText());
        registro.setLunes(cbLunes.isSelected());
        registro.setMartes(cbMartes.isSelected());
        registro.setMiercoles(cbMiercoles.isSelected());
        registro.setJueves(cbJueves.isSelected());
        registro.setViernes(cbViernes.isSelected());
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarHorario(?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getHorarioInicio());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.execute();
            listaHorario.add(registro);
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
                 tblHorarios.setOnKeyReleased((event) -> {seleccionarElementos();
                 });
                 tblHorarios.setOnMouseClicked((event) -> {seleccionarElementos();
                 });
                 tipoDeOperacion = operaciones.NINGUNO;
                 break;
            default:
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?", "Eliminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarHorario(?)}");
                        procedimiento.setInt(1, ((Horarios)tblHorarios.getSelectionModel().getSelectedItem()).getCodigoHorario());
                        procedimiento.execute();
                        listaHorario.remove(tblHorarios.getSelectionModel().getSelectedItem());
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
                if(tblHorarios.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EditarHorario(?,?,?,?,?,?,?,?)}");
            Horarios registro = (Horarios)tblHorarios.getSelectionModel().getSelectedItem();
            registro.setHorarioInicio(txtInicio.getText());
            registro.setHorarioSalida(txtInicio.getText());
            registro.setLunes(cbLunes.isSelected());
            registro.setMartes(cbMartes.isSelected());
            registro.setMiercoles(cbMiercoles.isSelected());
            registro.setJueves(cbJueves.isSelected());
            registro.setViernes(cbViernes.isSelected());
            procedimiento.setString(1, registro.getHorarioInicio());
            procedimiento.setString(2, registro.getHorarioSalida());
            procedimiento.setBoolean(3, registro.isLunes());
            procedimiento.setBoolean(4, registro.isMartes());
            procedimiento.setBoolean(5, registro.isMiercoles());
            procedimiento.setBoolean(6, registro.isJueves());
            procedimiento.setBoolean(7, registro.isViernes());
            procedimiento.setInt(8, registro.getCodigoHorario());
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
                 tblHorarios.getSelectionModel().clearSelection();
                 tipoDeOperacion = HorariosController.operaciones.NINGUNO;
                 break;                
        }
                
    }   

    public void desactivarControles(){
        txtInicio.setEditable(false);
        txtSalida.setEditable(false);
        cbLunes.setDisable(true);
        cbMartes.setDisable(true);
        cbMiercoles.setDisable(true);
        cbJueves.setDisable(true);
        cbViernes.setDisable(true);
    }
        
    public void activarControles(){
        txtInicio.setEditable(true);
        txtSalida.setEditable(true);
        cbLunes.setDisable(false);
        cbMartes.setDisable(false);
        cbMiercoles.setDisable(false);
        cbJueves.setDisable(false);
        cbViernes.setDisable(false);        
    }
    
    public void limpiarControles(){
        txtInicio.setText("");
        txtSalida.setText("");
        cbLunes.setSelected(false);
        cbMartes.setSelected(false);
        cbMiercoles.setSelected(false);
        cbJueves.setSelected(false);
        cbViernes.setSelected(false);         
    }   
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaMedicos(){
        escenarioPrincipal.ventanaMedicos();
    }    
   
}
