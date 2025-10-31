package servidor.red;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Clase Notificador
 * -----------------
 * Permite al servidor enviar mensajes a todos los clientes conectados.
 * Se usa, por ejemplo, cuando una acción como agregar una receta o medicamento
 * debe reflejarse en los frontends sin que ellos tengan que volver a consultar manualmente.
 */
public class Notificador {

    // Lista segura para hilos, almacena los sockets de los clientes conectados
    private static final List<Socket> clientesConectados = new CopyOnWriteArrayList<>();


    public static void registrarCliente(Socket cliente) {
        clientesConectados.add(cliente);
        System.out.println(" Cliente registrado: " + cliente.getInetAddress());
    }

    public static void eliminarCliente(Socket cliente) {
        clientesConectados.remove(cliente);
        System.out.println(" Cliente desconectado: " + cliente.getInetAddress());
    }

    public static void notificarATodos(Mensaje mensaje) {
        for (Socket cliente : clientesConectados) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
                out.writeObject(mensaje);
                out.flush();
                System.out.println(" Notificación enviada a " + cliente.getInetAddress());
            } catch (IOException e) {
                System.err.println(" No se pudo notificar al cliente " + cliente.getInetAddress());
                eliminarCliente(cliente);
            }
        }
    }

    public static void notificarACliente(Socket cliente, Mensaje mensaje) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
            out.writeObject(mensaje);
            out.flush();
            System.out.println(" Notificación enviada a cliente específico: " + cliente.getInetAddress());
        } catch (IOException e) {
            System.err.println(" No se pudo enviar notificación al cliente " + cliente.getInetAddress());
            eliminarCliente(cliente);
        }
    }
}
