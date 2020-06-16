
package org.brandonsicay.bean;

import java.util.Date;



public class Paciente {
    private int idPaciente;
    private String DPI;
    private String apellido;
    private String nombre;
    private Date fechaNacimiento;
    private int edad;
    private String direccion;
    private String ocupacion;
    private String sexo;

    public Paciente() {
    }

    public Paciente(int idPaciente, String DPI, String apellido, String nombre, Date fechaNacimiento, int edad, String direccion, String ocupacion, String sexo) {
        this.idPaciente = idPaciente;
        this.DPI = DPI;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.direccion = direccion;
        this.ocupacion = ocupacion;
        this.sexo = sexo;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getDPI() {
        return DPI;
    }

    public void setDPI(String DPI) {
        this.DPI = DPI;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String toString(){
        return getIdPaciente() + " | " + getApellido()+ " , " + getNombre();
    }
    
 

 
    

}
