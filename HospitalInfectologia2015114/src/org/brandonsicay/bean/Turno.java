
package org.brandonsicay.bean;

import java.util.Date;





public class Turno {
    private int codigoTurno;
    private Date fechaTurno;
    private Date fechaCita;
    private float valorCita;
    private int codigoMedicoEspecialidad;
    private int codigoTurnoResponsable;
    private int codigoPaciente;

    public Turno() {
    }

    public Turno(int codigoTurno, Date fechaTurno, Date fechaCita, float valorCita, int codigoMedicoEspecialidad, int codigoTurnoResponsable, int codigoPaciente) {
        this.codigoTurno = codigoTurno;
        this.fechaTurno = fechaTurno;
        this.fechaCita = fechaCita;
        this.valorCita = valorCita;
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
        this.codigoTurnoResponsable = codigoTurnoResponsable;
        this.codigoPaciente = codigoPaciente;
    }

    public int getCodigoTurno() {
        return codigoTurno;
    }

    public void setCodigoTurno(int codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    public Date getFechaTurno() {
        return fechaTurno;
    }

    public void setFechaTurno(Date fechaTurno) {
        this.fechaTurno = fechaTurno;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public float getValorCita() {
        return valorCita;
    }

    public void setValorCita(float valorCita) {
        this.valorCita = valorCita;
    }

    public int getCodigoMedicoEspecialidad() {
        return codigoMedicoEspecialidad;
    }

    public void setCodigoMedicoEspecialidad(int codigoMedicoEspecialidad) {
        this.codigoMedicoEspecialidad = codigoMedicoEspecialidad;
    }

    public int getCodigoTurnoResponsable() {
        return codigoTurnoResponsable;
    }

    public void setCodigoTurnoResponsable(int codigoTurnoResponsable) {
        this.codigoTurnoResponsable = codigoTurnoResponsable;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }
    
}
