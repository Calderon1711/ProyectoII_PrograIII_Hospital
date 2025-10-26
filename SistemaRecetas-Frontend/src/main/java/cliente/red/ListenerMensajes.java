package cliente.red;

import java.io.ObjectInputStream;

//Escucha mensajes entrantes del servidor en un hilo independiente.

public class ListenerMensajes implements Runnable {

    private final ClienteSocket cliente;
    private final ObjectInputStream entrada;

    public ListenerMensajes(ClienteSocket cliente, ObjectInputStream entrada) {
        this.cliente = cliente;
        this.entrada = entrada;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Escucha indefinidamente mensajes del servidor
                Mensaje respuesta = (Mensaje) entrada.readObject();
                cliente.entregarRespuesta(respuesta);
            }
        } catch (Exception e) {
            System.err.println("Error en ListenerMensajes: " + e.getMessage());
        }
    }
}
