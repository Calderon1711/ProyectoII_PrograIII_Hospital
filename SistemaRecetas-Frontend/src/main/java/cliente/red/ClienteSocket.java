package cliente.red;

import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClienteSocket {

    private static ClienteSocket instance;

    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;

    // Para mapear id de solicitud -> respuesta
    private final ConcurrentMap<String, BlockingQueue<Mensaje>> pendientes =
            new ConcurrentHashMap<>();

    private ClienteSocket() { }

    public static synchronized ClienteSocket getInstance() {
        if (instance == null) {
            instance = new ClienteSocket();
        }
        return instance;
    }

    public synchronized void connect(String host, int puerto) throws IOException {
        if (isConnected()) {
            return;
        }

        System.out.println("Interfaz cargada correctamente. Intentando conectar al servidor...");
        socket = new Socket(host, puerto);

        salida = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
        entrada = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // Hilo que escucha todas las respuestas del servidor
        Thread listener = new Thread(new ListenerMensajes(this, entrada), "ListenerMensajes");
        listener.setDaemon(true);
        listener.start();

        System.out.println("✅ Conectado al servidor correctamente.");
    }

    public synchronized boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    public synchronized void send(Mensaje mensaje) {
        String json = ConversorJSON.serializar(mensaje);
        // System.out.println("→ Enviando: " + json);
        salida.println(json);
        salida.flush();
    }

    public Mensaje enviarYEsperar(Mensaje solicitud, long timeoutMillis) {
        try {
            BlockingQueue<Mensaje> queue = new ArrayBlockingQueue<>(1);
            pendientes.put(solicitud.getId(), queue);

            send(solicitud);

            Mensaje respuesta = queue.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            pendientes.remove(solicitud.getId());
            return respuesta;
        } catch (Exception e) {
            System.err.println("❌ Error en enviarYEsperar: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Lo llama ListenerMensajes cuando llega algo del servidor
    public void entregarRespuesta(Mensaje respuesta) {
        if (respuesta == null) return;

        String id = respuesta.getId();
        if (id == null) {
            System.out.println("⚠️ Respuesta sin id: " + respuesta);
            return;
        }

        BlockingQueue<Mensaje> q = pendientes.get(id);
        if (q != null) {
            q.offer(respuesta);
        } else {
            System.out.println("⚠️ Respuesta sin solicitud pendiente. id=" + id +
                    ", comando=" + respuesta.getComando());
        }
    }
}
