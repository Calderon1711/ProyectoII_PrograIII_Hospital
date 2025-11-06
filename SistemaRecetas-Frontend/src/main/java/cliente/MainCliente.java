package cliente;

import cliente.Vista.*;
import cliente.controlador.*;
import cliente.red.*;
import cliente.util.ConfiguracionCliente;

import javax.swing.*;

public class MainCliente {

    public static void main(String[] args) {

        ConfiguracionCliente.cargarConfiguracion();

        SwingUtilities.invokeLater(() -> {

            // Inicializar vista
            LoginVista1 vistaLogin = new LoginVista1(); // Debe heredar JFrame

            try {
                // Inicializar socket y conectarse
                ClienteSocket clienteSocket = ClienteSocket.getInstance();
                clienteSocket.connect(ConfiguracionCliente.getHost(), ConfiguracionCliente.getPuerto());

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "No se pudo conectar con el servidor.",
                        "Error de conexión", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Inicializar app/controlador general
            ControladorGeneral app = new ControladorGeneral();
            ControladorLogin controladoraLogin = new ControladorLogin(vistaLogin, app);

            // Mostrar ventana
            vistaLogin.setVisible(true);
            System.out.println("Cliente iniciado correctamente.");
        });
    }
}
