package cliente.proxy;

import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import cliente.modelo.Paciente;
import cliente.constantes.*;

import java.util.List;

public class ProxyPaciente extends ProxyService {

    // Obtener todas las recetas
    public ObservableList<Paciente> obtenerPacientes() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_PACIENTES, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Paciente> listaNormal = ConversorJSON.deserializarLista(json,Paciente.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    // Agregar una receta nueva → devuelve la receta creada
    public Paciente agregarPaciente(Paciente paciente) {
        try {
            String jsonReceta = ConversorJSON.serializar(paciente);
            Mensaje solicitud = new Mensaje(Comandos.AGREGAR_PACIENTE, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Paciente.class);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar Paciente " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Consultar una receta por ID → devuelve la receta encontrada
    public Paciente consultarPaciente(int idPaciente) {
        try {
            // Convertimos el ID a JSON para mantener coherencia
            String jsonId = ConversorJSON.serializar(idPaciente);
            Mensaje solicitud = new Mensaje(Comandos.CONSULTAR_PACIENTE, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Paciente.class);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Actualizar una receta → devuelve la receta actualizada
    public Paciente actualizarPaciente(Paciente paciente) {
        try {
            String jsonReceta = ConversorJSON.serializar(paciente);
            Mensaje solicitud = new Mensaje(Comandos.ACTUALIZAR_PACIENTE, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Paciente.class);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar una receta → devuelve la receta eliminada (si el backend la devuelve)
    public Paciente eliminarPaciente(int idPaciente) {
        try {
            // Igual que consultar, enviamos el ID como JSON
            String jsonId = ConversorJSON.serializar(idPaciente);
            Mensaje solicitud = new Mensaje(Comandos.ELIMINAR_PACIENTE, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Paciente.class);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar Paciente: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
