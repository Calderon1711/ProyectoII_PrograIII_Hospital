package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.modelo.MensajeChat;
import cliente.modelo.Personal;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProxyUsuario extends ProxyService {

    public boolean iniciarSesion(Personal usuario) {
        try {
            String json = ConversorJSON.serializar(usuario);
            Mensaje solicitud = new Mensaje(Comandos.LOGIN, json);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            return respuesta != null && respuesta.isExito();

        } catch (Exception e) {
            System.err.println("Error en iniciarSesion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void cerrarSesion(Personal usuario) {
        try {
            String json = ConversorJSON.serializar(usuario);
            Mensaje solicitud = new Mensaje(Comandos.LOGOUT, json);
            ClienteSocket.getInstance().send(solicitud);
        } catch (Exception e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
    }

    // Obtener lista de usuarios activos (pacientes, médicos, farmacéuticos, etc.)

    public ObservableList<Personal> obtenerUsuariosActivos() {
        try {
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_USUARIOS_ACTIVOS, null);
            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Personal> lista = ConversorJSON.deserializarLista(json, Personal.class);
                return FXCollections.observableArrayList(lista);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener usuarios activos: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    public boolean enviarMensaje(Personal remitente, Personal destinatario, String contenido) {
        try {
            MensajeChat msg = new MensajeChat(remitente.getNombre(), destinatario.getNombre(), contenido);
            String json = ConversorJSON.serializar(msg);

            Mensaje solicitud = new Mensaje(Comandos.ENVIAR_MENSAJE, json);
            ClienteSocket.getInstance().send(solicitud);

            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
            return false;
        }
    }
}
