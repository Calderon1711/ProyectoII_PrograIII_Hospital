package cliente.red;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Maneja la conexión del cliente con el servidor.

public class ClienteSocket {

    private static ClienteSocket instance;
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    private ClienteSocket() {}

    public static synchronized ClienteSocket getInstance() {
        if (instance == null) instance = new ClienteSocket();
        return instance;
    }

    public synchronized void connect(String host, int puerto) throws Exception {
        if (socket != null && socket.isConnected()) return;

        socket = new Socket(host, puerto);
        salida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());

        // 🔹 En vez de crear el hilo aquí directamente,
        // lo delegamos al gestor de hilos.
        GestorHilosCliente.getInstance().ejecutar(new ListenerMensajes(this, entrada));
    }

    public synchronized void send(Mensaje mensaje) throws Exception {
        salida.writeObject(mensaje);
        salida.flush();
    }

    public void entregarRespuesta(Mensaje respuesta) {
        System.out.println("Respuesta recibida: " + respuesta.getComando());
        // Aquí se puede notificar a quien esté esperando la respuesta
    }
}
