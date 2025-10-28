package servidor.red;

import java.io.Serializable;

//esta clases es el objeto que viaja entre el fronted y el backend
public class Mensaje implements Serializable {
    //Comando o accion que se desea ejecutar por ejemplo "Listar_paciente" o cosas asi
    private String comando;

    //objeto asociado al comando en este caso el ejemplo seria un paciente a agregar
    private Object objeto;

    //esto solo indica si la operacion fue existosa o no
    private boolean exito;

    // mensaje descriptivo en este caso el ejemplo seria Paciente agregado correctamenre
    private String mensaje;

    //Este es el resultado devuelto en este caso el ejkmplo seria una lista de pacientes
    private Object resultado;

    //constructores

    /**
     * Constructor usado por el servidor para responder al cliente.
     * @param exito   Indica si la operación fue exitosa
     * @param mensaje Mensaje de respuesta
     * @param resultado Resultado de la operación (puede ser null)
     */

    public Mensaje(boolean exito, String mensaje, Object resultado) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.resultado = resultado;
    }

    //getters y setters


    public Object getObjeto() {
        return objeto;
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
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
        return  "Mensaje{" +
                "comando='" + comando + '\'' +
                ", exito=" + exito +
                ", mensaje='" + mensaje + '\'' +
                ", objeto=" + objeto +
                ", resultado=" + resultado +
                '}';
    }
}
