package cliente;

import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;

/**
 * Prueba de conexión del cliente con el servidor.
 * Ejecuta una verificación básica usando ClienteSocket y muestra el resultado en consola.
 */
public class MainCliente {

    public static void main(String[] args) {
        try {
            System.out.println("🔹 Iniciando prueba de conexión con el servidor...");

            // Instancia del socket
            ClienteSocket cliente = ClienteSocket.getInstance();

            // Conectarse al servidor
            cliente.connect(ConfiguracionCliente.getHost(), ConfiguracionCliente.getPuerto());
            System.out.println("✅ Cliente conectado a " +
                    ConfiguracionCliente.getHost() + ":" + ConfiguracionCliente.getPuerto());

            // Crear un mensaje de prueba
            Mensaje solicitud = new Mensaje("test_conexion", null);

            // Enviar mensaje y esperar respuesta
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            // Revisar resultado
            if (respuesta != null && respuesta.isExito()) {
                System.out.println("✅ Conexión exitosa con el servidor.");
                System.out.println("📩 Respuesta del servidor: " + respuesta.getResultado());
            } else {
                System.out.println("⚠️ No se recibió respuesta del servidor.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error al conectar con el servidor:");
            e.printStackTrace();
        }
    }
}
