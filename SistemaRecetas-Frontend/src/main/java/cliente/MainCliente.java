package cliente;

import cliente.Vista.*;
import cliente.controlador.*;
import cliente.red.*;
import cliente.util.ConfiguracionCliente;

import javax.swing.*;

public class MainCliente {

    public static void main(String[] args) {

        // Cargar configuración (host, puerto, etc.)
        ConfiguracionCliente.cargarConfiguracion();

        SwingUtilities.invokeLater(() -> {

            // Inicializar vista de login
            LoginVista1 vistaLogin = new LoginVista1(); // Debe heredar JFrame
            vistaLogin.setVisible(true);

            // Inicializar controlador principal
            ControladorGeneral app = new ControladorGeneral();
            new ControladorLogin(vistaLogin, app);

            System.out.println("Interfaz cargada correctamente. Intentando conectar al servidor...");

            // Conectarse al servidor en un hilo separado (no bloqueará la GUI)
            new Thread(() -> {
                try {
                    ClienteSocket clienteSocket = ClienteSocket.getInstance();
                    clienteSocket.connect(
                            ConfiguracionCliente.getHost(),
                            ConfiguracionCliente.getPuerto()
                    );

                    System.out.println("✅ Conectado al servidor correctamente.");

                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                            null,
                            "⚠️ No se pudo conectar con el servidor.\n" +
                                    "Verifique que esté corriendo y vuelva a intentarlo.",
                            "Error de conexión",
                            JOptionPane.ERROR_MESSAGE
                    ));
                }
            }).start();
        });
    }
}
