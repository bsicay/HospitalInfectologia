
package org.brandonsicay.bean;

import java.sql.Time;
import java.util.Date;


public class Usuarios{
    private int codigoUsuario; 
    private String usuarioLogin;
    private String usuarioContraseña; 
    private boolean usuarioEstado;
    private Date usuarioFecha; 
    private Time usuarioHora;
    private int codigoTipoUsuario;
    private int codUsuario;

    public Usuarios() {
    }

    public Usuarios(int codigoUsuario, String usuarioLogin, String usuarioContraseña, boolean usuarioEstado, Date usuarioFecha, Time usuarioHora, int codigoTipoUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.usuarioLogin = usuarioLogin;
        this.usuarioContraseña = usuarioContraseña;
        this.usuarioEstado = usuarioEstado;
        this.usuarioFecha = usuarioFecha;
        this.usuarioHora = usuarioHora;
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getUsuarioLogin() {
        return usuarioLogin;
    }

    public void setUsuarioLogin(String usuarioLogin) {
        this.usuarioLogin = usuarioLogin;
    }

    public String getUsuarioContraseña() {
        return usuarioContraseña;
    }

    public void setUsuarioContraseña(String usuarioContraseña) {
        this.usuarioContraseña = usuarioContraseña;
    }

    public boolean isUsuarioEstado() {
        return usuarioEstado;
    }

    public void setUsuarioEstado(boolean usuarioEstado) {
        this.usuarioEstado = usuarioEstado;
    }

    public Date getUsuarioFecha() {
        return usuarioFecha;
    }

    public void setUsuarioFecha(Date usuarioFecha) {
        this.usuarioFecha = usuarioFecha;
    }

    public Time getUsuarioHora() {
        return usuarioHora;
    }

    public void setUsuarioHora(Time usuarioHora) {
        this.usuarioHora = usuarioHora;
    }

    public int getCodigoTipoUsuario() {
        return codigoTipoUsuario;
    }

    public void setCodigoTipoUsuario(int codigoTipoUsuario) {
        this.codigoTipoUsuario = codigoTipoUsuario;
    }

    public int getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        this.codUsuario = codUsuario;
    }




    
}
