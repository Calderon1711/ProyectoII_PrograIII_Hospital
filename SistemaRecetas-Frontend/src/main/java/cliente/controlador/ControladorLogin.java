package Controlador;

// ControladorLogin.java
import Modelo.Personal;
import Vista.LoginVista1;
import Modelo.Hospital;

import javax.swing.*;
import java.util.Arrays;

public class ControladorLogin {
    private final LoginVista1 vista;
    private final ControladorGeneral appController;
    private Hospital hospi=Hospital.getInstance();

    public ControladorLogin(LoginVista1 vista, ControladorGeneral appController) {
        this.vista = vista;

        this.appController = appController;
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

        // limpiar array por seguridad
        Arrays.fill(passChars, '\0');

        vista.getMSJ_Error().setText(""); // limpiar mensaje previo

        if (username.isEmpty() || password.isEmpty()) {
            vista.getMSJ_Error().setText("Ingrese usuario y contraseña.");
            return;
        }

        Personal usuario =Hospital.getInstance().loginPersonal(username, password);

        if (usuario != null) {
            // autenticación OK -> delegar al controlador general
            appController.abrirVistaPorRol(usuario);
            vista.dispose(); // cerramos el login
        } else {
            vista.getMSJ_Error().setText("Usuario o contraseña incorrectos.");
            vista.getCampoClave().setText("");
            vista.getCampoUsuario().requestFocusInWindow();
        }
    }

    private void onCancelar() {
        vista.getCampoUsuario().setText("");
        vista.getCampoClave().setText("");
        vista.getMSJ_Error().setText("");
        vista.getCampoUsuario().requestFocusInWindow();
    }

    private void onCambiarContra() {
        JTextField campoID=new JTextField();
        JPasswordField nueva = new JPasswordField();
        JPasswordField confirmar = new JPasswordField();

        Object[] message = {
                "ID:",campoID,
                "Nueva contraseña:", nueva,
                "Confirmar contraseña:", confirmar
        };

        int option = JOptionPane.showConfirmDialog(vista, message, "Cambiar contraseña", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String pass1 = new String(nueva.getPassword());
            String pass2 = new String(confirmar.getPassword());
            String id= campoID.getText();

            if (pass1.equals(pass2) && !pass1.isEmpty()) {
                // Aquí actualizas en Hospital o en el modelo
                Personal usuario= hospi.getPersonal().getPersonalPorID(id);
                usuario.setClave(pass1);
                hospi.guradarPersonal();
                JOptionPane.showMessageDialog(vista, "Contraseña cambiada con éxito.");
            } else {
                JOptionPane.showMessageDialog(vista, "Las contraseñas no coinciden o están vacías.");
            }
        }
    }

}

