package cliente.proxy;

import cliente.constantes.Comandos;
import cliente.modelo.Hospital;
import cliente.red.ClienteSocket;
import cliente.red.Mensaje;
import cliente.util.ConfiguracionCliente;
import cliente.util.ConversorJSON;


import java.util.List;

public class ProxyHospital {

    // Obtener el hospital

    public Hospital obtenerHospital() {
        try {
            // No hay datos que enviar, pero mantenemos coherencia: enviamos null
            String jsonDatos = ConversorJSON.serializar(null);
            Mensaje solicitud = new Mensaje(Comandos.LISTAR_HOSPITAL, jsonDatos);

            ClienteSocket cliente = ClienteSocket.getInstance();
            Mensaje respuesta = cliente.enviarYEsperar(solicitud, ConfiguracionCliente.getTimeout());

            if (respuesta != null && respuesta.isExito()) {
                String json = (String) respuesta.getResultado();
                Hospital hospital;
                hospital = ConversorJSON.deserializar(json, Hospital.class);
                return hospital;
            }
        } catch (Exception e) {
            System.err.println("Error al obtener hospital: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
