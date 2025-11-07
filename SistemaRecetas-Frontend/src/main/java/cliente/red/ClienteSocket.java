package cliente.red;

import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteSocket {

    private static ClienteSocket instance;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;

    private ClienteSocket() {}

    public static synchronized ClienteSocket getInstance() {
        if (instance == null) {
            instance = new ClienteSocket();
        }
        return instance;
    }

    public synchronized void connect(String host, int puerto) throws Exception {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return;
        }

        System.out.println("Interfaz cargada correctamente. Intentando conectar al servidor...");

        socket = new Socket(host, puerto);
        salida = new PrintWriter(socket.getOutputStream(), true);
        entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.println("✅ Conectado al servidor correctamente.");
    }

    public synchronized boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    // Enviar sin esperar (por si lo necesitas)
    public synchronized void send(Mensaje mensaje) throws IOException {
        String json = ConversorJSON.serializar(mensaje);
        salida.println(json);
    }

    // Enviar y esperar respuesta (lo usan tus proxys)
    public synchronized Mensaje enviarYEsperar(Mensaje solicitud, long timeoutMillis) {
        try {
            // enviar
            String json = ConversorJSON.serializar(solicitud);
            salida.println(json);

            // timeout opcional
            socket.setSoTimeout((int) timeoutMillis);

            // leer respuesta
            String respJson = entrada.readLine();
            if (respJson == null) {
                System.err.println("Respuesta nula del servidor.");
                return null;
            }

            return ConversorJSON.deserializar(respJson, Mensaje.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}