package cliente.red;

import cliente.util.ConversorJSON;
import java.io.BufferedReader;

public class ListenerMensajes implements Runnable {

    private final ClienteSocket clienteSocket;
    private final BufferedReader entrada;

    public ListenerMensajes(ClienteSocket clienteSocket, BufferedReader entrada) {
        this.clienteSocket = clienteSocket;
        this.entrada = entrada;
    }

    @Override
    public void run() {
        try {
            String linea;
            while ((linea = entrada.readLine()) != null) {
                // System.out.println("← Recibido del servidor: " + linea);
                Mensaje respuesta = ConversorJSON.deserializar(linea, Mensaje.class);
                if (respuesta != null) {
                    clienteSocket.entregarRespuesta(respuesta); // 👈 AQUÍ EL CAMBIO
                }
            }
            System.out.println("Conexión cerrada por el servidor.");
        } catch (Exception e) {
            System.out.println("❌ Error en ListenerMensajes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
