package servidor.red;

import java.io.Serializable;
import java.util.UUID;

/**
 * Mensaje que viaja entre cliente y servidor (DEBE ser igual al del cliente).
 */
public class Mensaje implements Serializable {

    private String id;
    private String comando;
    private Object datos;
    private boolean exito;
    private Object resultado;
    private String mensaje;

    public Mensaje() {
        this.id = UUID.randomUUID().toString();
    }

    public Mensaje(String comando, Object datos) {
        this();
        this.comando = comando;
        this.datos = datos;
    }

    // Getters & setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public Object getResultado() {
        return resultado;
    }

    public void setResultado(Object resultado) {
        this.resultado = resultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "id='" + id + '\'' +
                ", comando='" + comando + '\'' +
                ", exito=" + exito +
                ", mensaje='" + mensaje + '\'' +
                ", datos=" + datos +
                ", resultado=" + resultado +
                '}';
    }
}
