package cliente.proxy;

import cliente.red.ClienteSocket;
import cliente.red.Mensaje;

public class ProxyService {

    protected final ClienteSocket clienteSocket;

    public ProxyService() {
        this.clienteSocket = ClienteSocket.getInstance();
    }

    protected Mensaje enviar(String accion, Object datos, long timeoutMillis) {
        try {
            if (!clienteSocket.isConnected()) {
                // ajusta host/puerto desde configuración si tienes ConfiguracionCliente
                clienteSocket.connect("localhost", 5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Mensaje req = new Mensaje(accion, datos);
        return clienteSocket.sendSync(req, timeoutMillis);
    }
}
