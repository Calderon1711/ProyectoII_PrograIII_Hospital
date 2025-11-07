package cliente.red;

import java.io.Serializable;

public class Mensaje implements Serializable {

    private String comando;
    private Object objeto;
    private boolean exito;
    private String mensaje;
    private Object resultado;

    public Mensaje() {
    }

    public Mensaje(String comando, Object objeto) {
        this.comando = comando;
        this.objeto = objeto;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "comando='" + comando + '\'' +
                ", exito=" + exito +
                ", mensaje='" + mensaje + '\'' +
                ", objeto=" + objeto +
                ", resultado=" + resultado +
                '}';
    }
}