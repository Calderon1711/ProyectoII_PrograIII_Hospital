package cliente.proxy;

import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;

public class ProxyService {

    protected final ClienteSocket clienteSocket;

    public ProxyService() {
        this.clienteSocket = ClienteSocket.getInstance();
    }


    protected Mensaje enviar(String accion, Object datos, long timeoutMillis) {
        try {
            // Verifica si hay conexión activa, si no, la crea
            if (!clienteSocket.isConnected()) {
                clienteSocket.connect(ConfiguracionCliente.getHost(), ConfiguracionCliente.getPuerto());
            }

            // Crear el mensaje y enviarlo
            Mensaje solicitud = new Mensaje(accion, datos);
            return clienteSocket.enviarYEsperar(solicitud, timeoutMillis);

        } catch (Exception e) {
            System.err.println("Error en ProxyService.enviar(): " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    protected Mensaje enviar(String accion, Object datos) {
        return enviar(accion, datos, ConfiguracionCliente.getTimeout());
    }
}
