package cliente.red;

import java.io.Serializable;
import java.util.UUID;

//Representa la estructura de comunicación entre cliente y servidor.
//Se envía por red (serializable).


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

    // Getters y setters...
    public String getId() { return id; }
    public String getComando() { return comando; }
    public Object getDatos() { return datos; }
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public Object getResultado() { return resultado; }
    public void setResultado(Object resultado) { this.resultado = resultado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}

