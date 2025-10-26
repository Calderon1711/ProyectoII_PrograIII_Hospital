package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;
import cliente.modelo.Receta;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Proxy para manejar la comunicación con el servidor respecto a las recetas.

public class ProxyReceta extends ProxyService {

    public ObservableList<Receta> obtenerRecetas() {
        ObservableList<Receta> recetas = FXCollections.observableArrayList();

        try {
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_RECETAS, null);
            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();

                // Gson devuelve una List<Receta>
                List<Receta> listaNormal = ConversorJSON.deserializarLista(json, Receta.class);

                // La convertimos a ObservableList
                recetas = FXCollections.observableArrayList(listaNormal);

                System.out.println("Recetas obtenidas: " + recetas.size());
            } else {
                System.out.println("No se pudieron obtener recetas: " +
                        (respuesta != null ? respuesta.getMensaje() : "Sin respuesta del servidor"));
            }

        } catch (Exception e) {
            System.err.println("Error en ProxyReceta.obtenerRecetas(): " + e.getMessage());
            e.printStackTrace();
        }

        return recetas;
    }
}
