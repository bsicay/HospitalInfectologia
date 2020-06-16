/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.brandonsicay.bean;

/**
 *
 * @author brand
 */
public class ResponsableTurno {
     private int codigoResponsableTurno;
     private String nombreReponsable; 
     private String apellidosResponsable; 
     private String telefonoPersonal; 
     private int codigoArea; 
     private int codigoCargo; 

    public ResponsableTurno() {
    }

    public ResponsableTurno(int codigoResponsableTurno, String nombreReponsable, String apellidosResponsable, String telefonoPersonal, int codigoArea, int codigoCargo) {
        this.codigoResponsableTurno = codigoResponsableTurno;
        this.nombreReponsable = nombreReponsable;
        this.apellidosResponsable = apellidosResponsable;
        this.telefonoPersonal = telefonoPersonal;
        this.codigoArea = codigoArea;
        this.codigoCargo = codigoCargo;
    }

    public int getCodigoResponsableTurno() {
        return codigoResponsableTurno;
    }

    public void setCodigoResponsableTurno(int codigoResponsableTurno) {
        this.codigoResponsableTurno = codigoResponsableTurno;
    }

    public String getNombreReponsable() {
        return nombreReponsable;
    }

    public void setNombreReponsable(String nombreReponsable) {
        this.nombreReponsable = nombreReponsable;
    }

    public String getApellidosResponsable() {
        return apellidosResponsable;
    }

    public void setApellidosResponsable(String apellidosResponsable) {
        this.apellidosResponsable = apellidosResponsable;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String toString(){
        return getCodigoResponsableTurno() + " | " + getNombreReponsable() + " - " + getApellidosResponsable();
    }
     
     
}
