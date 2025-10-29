package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import cliente.modelo.DetalleMedicamento;
import java.util.List;

public class ProxyDetalleMedicamento extends ProxyService {

    // Obtener todas las recetas
    public ObservableList<DetalleMedicamento> obtenerDetalleMedicamentos() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_DETALLEMEDICAMENTOS, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<DetalleMedicamento> listaNormal = ConversorJSON.deserializarLista(json, DetalleMedicamento.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener los detalles de los medicamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    // Agregar una receta nueva → devuelve la receta creada
    public DetalleMedicamento agregarReceta(DetalleMedicamento detalleMedicamento) {
        try {
            String jsonReceta = ConversorJSON.serializar(detalleMedicamento);
            Mensaje solicitud = new Mensaje(Comandos.AGREGAR_DETALLEMEDICAMENTO, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, DetalleMedicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar DetalleMedicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Consultar una receta por ID → devuelve la receta encontrada
    public DetalleMedicamento consultarDetalleMedicamento(int idDetalleMedicamento) {
        try {
            // Convertimos el ID a JSON para mantener coherencia
            String jsonId = ConversorJSON.serializar(idDetalleMedicamento);
            Mensaje solicitud = new Mensaje(Comandos.CONSULTAR_DETALLEMEDICAMENTO, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, DetalleMedicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Detalle Medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Actualizar una receta → devuelve la receta actualizada
    public DetalleMedicamento actualizarDetalleMedicamento(DetalleMedicamento detalleMedicamento) {
        try {
            String jsonReceta = ConversorJSON.serializar(detalleMedicamento);
            Mensaje solicitud = new Mensaje(Comandos.ACTUALIZAR_DETALLEMEDICAMENTO, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, DetalleMedicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar detalle medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar una receta → devuelve la receta eliminada (si el backend la devuelve)
    public DetalleMedicamento eliminarDetalleMedicamento(int idDetalleMedicamento) {
        try {
            // Igual que consultar, enviamos el ID como JSON
            String jsonId = ConversorJSON.serializar(idDetalleMedicamento);
            Mensaje solicitud = new Mensaje(Comandos.ELIMINAR_DETALLEMEDICAMENTO, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, DetalleMedicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar detalle medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
