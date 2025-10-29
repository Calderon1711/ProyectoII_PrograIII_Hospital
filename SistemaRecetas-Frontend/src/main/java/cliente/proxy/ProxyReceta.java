package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import cliente.modelo.Receta;

import java.util.List;

public class ProxyReceta extends ProxyService {

    // Obtener todas las recetas
    public ObservableList<Receta> obtenerRecetas() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_RECETAS, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Receta> listaNormal = ConversorJSON.deserializarLista(json, Receta.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener recetas: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    // Agregar una receta nueva → devuelve la receta creada
    public Receta agregarReceta(Receta receta) {
        try {
            String jsonReceta = ConversorJSON.serializar(receta);
            Mensaje solicitud = new Mensaje(Comandos.AGREGAR_RECETA, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Receta.class);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar receta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Consultar una receta por ID → devuelve la receta encontrada
    public Receta consultarReceta(int idReceta) {
        try {
            // Convertimos el ID a JSON para mantener coherencia
            String jsonId = ConversorJSON.serializar(idReceta);
            Mensaje solicitud = new Mensaje(Comandos.CONSULTAR_RECETA, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Receta.class);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar receta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Actualizar una receta → devuelve la receta actualizada
    public Receta actualizarReceta(Receta receta) {
        try {
            String jsonReceta = ConversorJSON.serializar(receta);
            Mensaje solicitud = new Mensaje(Comandos.ACTUALIZAR_RECETA, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Receta.class);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar receta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar una receta → devuelve la receta eliminada (si el backend la devuelve)
    public Receta eliminarReceta(int idReceta) {
        try {
            // Igual que consultar, enviamos el ID como JSON
            String jsonId = ConversorJSON.serializar(idReceta);
            Mensaje solicitud = new Mensaje(Comandos.ELIMINAR_RECETA, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Receta.class);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar receta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
