package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.modelo.Medico;
import cliente.modelo.Personal;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProxyPersonal {

    public ObservableList<Personal> obtenerPersonal() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_PERSONAL, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Personal> listaNormal = ConversorJSON.deserializarLista(json,Personal.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener personal: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    public ObservableList<Medico> obtenerMedicos() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_MEDICOS, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                List<Medico> listaNormal = ConversorJSON.deserializarLista(json,Medico.class);
                return FXCollections.observableArrayList(listaNormal);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener personal: " + e.getMessage());
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }

    // Agregar una receta nueva → devuelve la receta creada
    public Personal agregarPersonal(Personal personal) {
        try {
            String jsonReceta = ConversorJSON.serializar(personal);
            Mensaje solicitud = new Mensaje(Comandos.AGREGAR_PERSONAL, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Personal.class);
            }
        } catch (Exception e) {
            System.err.println("Error al agregar personal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Consultar una receta por ID → devuelve la receta encontrada
    public Personal consultarPersonal(int idPersonal) {
        try {
            // Convertimos el ID a JSON para mantener coherencia
            String jsonId = ConversorJSON.serializar(idPersonal);
            Mensaje solicitud = new Mensaje(Comandos.CONSULTAR_PERSONAL, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Personal.class);
            }
        } catch (Exception e) {
            System.err.println("Error al consultar personal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //  Actualizar una receta → devuelve la receta actualizada
    public Personal actualizarPersonal(Personal personal) {
        try {
            String jsonReceta = ConversorJSON.serializar(personal);
            Mensaje solicitud = new Mensaje(Comandos.ACTUALIZAR_PERSONAL, jsonReceta);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json, Personal.class);
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar personal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Eliminar una receta → devuelve la receta eliminada (si el backend la devuelve)
    public Personal eliminarPersonal(int idPersonal) {
        try {
            // Igual que consultar, enviamos el ID como JSON
            String jsonId = ConversorJSON.serializar(idPersonal);
            Mensaje solicitud = new Mensaje(Comandos.ELIMINAR_PERSONAL, jsonId);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(json,Personal.class);
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar personal: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Personal login(String id, String clave) {
        try {
            String[] datos = {id, clave};
            String json = ConversorJSON.serializar(datos);

            Mensaje solicitud = new Mensaje(Comandos.LOGIN_PERSONAL, json);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito() && respuesta.getResultado() != null) {
                String jsonUsuario = (String) respuesta.getResultado();
                return ConversorJSON.deserializar(jsonUsuario, Personal.class);
            }

        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean cambiarClave(String id, String nuevaClave) {
        try {
            String[] datos = {id, nuevaClave};
            String json = ConversorJSON.serializar(datos);
            Mensaje solicitud = new Mensaje(Comandos.CAMBIAR_CLAVE, json);
            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());
            return respuesta != null && respuesta.isExito();
        } catch (Exception e) {
            System.err.println("Error al cambiar clave: " + e.getMessage());
        }
        return false;
    }
}
