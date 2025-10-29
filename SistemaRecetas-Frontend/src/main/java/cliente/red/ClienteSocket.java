package cliente.red;

import cliente.util.ConfiguracionCliente;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

// Maneja la conexión del cliente con el servidor.
public class ClienteSocket {

    private static ClienteSocket instance;
    private Socket socket;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;
    private final ConcurrentMap<String, BlockingQueue<Mensaje>> pendientes = new ConcurrentHashMap<>();

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

        // Crear hilo listener para recibir respuestas
        GestorHilosCliente.getInstance().ejecutar(new ListenerMensajes(this, entrada));
    }

    public synchronized void send(Mensaje mensaje) throws Exception {
        salida.writeObject(mensaje);
        salida.flush();
    }

    // Verifica si el socket está conectado y activo

    public synchronized boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public void entregarRespuesta(Mensaje respuesta) {
        BlockingQueue<Mensaje> q = pendientes.get(respuesta.getId());
        if (q != null) {
            q.offer(respuesta);
        } else {
            System.out.println("Respuesta sin solicitud pendiente: " + respuesta.getComando());
        }
    }

    // Método que envía un mensaje y espera respuesta

    public Mensaje enviarYEsperar(Mensaje solicitud, long timeoutMillis) {
        try {
            BlockingQueue<Mensaje> queue = new ArrayBlockingQueue<>(1);
            pendientes.put(solicitud.getId(), queue);

            // Enviar el mensaje
            send(solicitud);

            // Esperar la respuesta (hasta timeout)
            Mensaje respuesta = queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            pendientes.remove(solicitud.getId());
            return respuesta;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
