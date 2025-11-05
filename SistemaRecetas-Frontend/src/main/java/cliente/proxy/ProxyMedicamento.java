package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.modelo.Medicamento;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProxyMedicamento {
    public ObservableList<Medicamento> obtenerMedicamentos() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_MEDICAMENTOS, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Medicamento> listaNormal = ConversorJSON.deserializarLista(json, Medicamento.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    // Agregar una receta nueva → devuelve la receta creada
    public Medicamento agregarMedicamento(Medicamento medicamento) {
        try {
            String jsonReceta = ConversorJSON.serializar(medicamento);
            Mensaje solicitud = new Mensaje(Comandos.AGREGAR_MEDICAMENTO, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Medicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public Medicamento consultarMedicamento(String nombreMedicamento) {
        try {
            // Convertimos el ID a JSON para mantener coherencia
            String jsonId = ConversorJSON.serializar(nombreMedicamento);
            Mensaje solicitud = new Mensaje(Comandos.CONSULTAR_MEDICAMENTO, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Medicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Actualizar una receta → devuelve la receta actualizada
    public Medicamento actualizarMedicamento(Medicamento medicamento) {
        try {
            String jsonReceta = ConversorJSON.serializar(medicamento);
            Mensaje solicitud = new Mensaje(Comandos.ACTUALIZAR_MEDICAMENTO, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Medicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar una receta → devuelve la receta eliminada (si el backend la devuelve)
    public Medicamento eliminarMedicamento(int idReceta) {
        try {
            // Igual que consultar, enviamos el ID como JSON
            String jsonId = ConversorJSON.serializar(idReceta);
            Mensaje solicitud = new Mensaje(Comandos.ELIMINAR_MEDICAMENTO, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Medicamento.class);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
