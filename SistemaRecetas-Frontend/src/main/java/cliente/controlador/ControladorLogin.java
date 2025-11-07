package cliente.controlador;

import cliente.Vista.*;
import cliente.modelo.*;
import cliente.proxy.ProxyPersonal;

import javax.swing.*;
import java.util.Arrays;

public class ControladorLogin {
    private final LoginVista1 vista;
    private final ControladorGeneral appController;
    private final ProxyPersonal proxyPersonal;

    public ControladorLogin(LoginVista1 vista, ControladorGeneral appController) {
        this.vista = vista;
        this.appController = appController;
        this.proxyPersonal = new ProxyPersonal();
        initController();
    }

    private void initController() {
        vista.getBotonIngresar().addActionListener(e -> onIngresar());
        vista.getBotonCancelar().addActionListener(e -> onCancelar());
        vista.getBotonCambiarContra().addActionListener(e -> onCambiarContra());
        vista.getCampoClave().addActionListener(e -> onIngresar()); // Enter en contraseña
    }

    private void onIngresar() {
        String username = vista.getCampoUsuario().getText().trim();
        char[] passChars = vista.getCampoClave().getPassword();
        String password = new String(passChars);
        Arrays.fill(passChars, '\0');

        vista.getMSJ_Error().setText("");

        if (username.isEmpty() || password.isEmpty()) {
            vista.getMSJ_Error().setText("Ingrese usuario y contraseña.");
            return;
        }

        // ✅ Ejecutar login en un hilo separado
        new SwingWorker<Personal, Void>() {
            @Override
            protected Personal doInBackground() {
                return proxyPersonal.login(username, password);
            }

            @Override
            protected void done() {
                try {
                    Personal usuario = get();

                    if (usuario != null) {
                        appController.abrirVistaPorRol(usuario);
                        vista.dispose();
                    } else {
                        vista.getMSJ_Error().setText("Usuario o contraseña incorrectos.");
                        vista.getCampoClave().setText("");
                        vista.getCampoUsuario().requestFocusInWindow();
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista,
                            "Error al intentar iniciar sesión: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void onCancelar() {
        vista.getCampoUsuario().setText("");
        vista.getCampoClave().setText("");
        vista.getMSJ_Error().setText("");
        vista.getCampoUsuario().requestFocusInWindow();
    }

    private void onCambiarContra() {
        JTextField campoID = new JTextField();
        JPasswordField nueva = new JPasswordField();
        JPasswordField confirmar = new JPasswordField();

        Object[] message = {
                "ID:", campoID,
                "Nueva contraseña:", nueva,
                "Confirmar contraseña:", confirmar
        };

        int option = JOptionPane.showConfirmDialog(vista, message, "Cambiar contraseña", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String pass1 = new String(nueva.getPassword());
            String pass2 = new String(confirmar.getPassword());
            String id = campoID.getText();

            if (pass1.equals(pass2) && !pass1.isEmpty()) {
                // ✅ Cambiar contraseña también en segundo plano
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        return proxyPersonal.cambiarClave(id, pass1);
                    }

                    @Override
                    protected void done() {
                        try {
                            boolean ok = get();
                            if (ok)
                                JOptionPane.showMessageDialog(vista, "Contraseña cambiada con éxito.");
                            else
                                JOptionPane.showMessageDialog(vista, "Error al cambiar la contraseña.");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage());
                        }
                    }
                }.execute();

            } else {
                JOptionPane.showMessageDialog(vista, "Las contraseñas no coinciden o están vacías.");
            }
        }
    }
}
