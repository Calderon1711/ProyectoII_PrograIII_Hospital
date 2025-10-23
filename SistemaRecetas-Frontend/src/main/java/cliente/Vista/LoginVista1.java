package Vista;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginVista1 extends JFrame {

    private JPanel PanelPrincipal;
    private JPanel PanelTitulo;
    private JPanel PanelBotones;
    private JPanel Panel_LogIn;
    private JLabel TextoBienvenida;
    private JButton BotonIngresar;
    private JButton BotonCancelar;
    private JButton BotonCambiarContra;
    private JLabel TextoUsuario;
    private JTextField CampoUsuario;
    private JPasswordField CampoClave;
    private JPanel PanelMNSJ_Error;
    private JLabel MSJ_Error;
    private JPanel PanelUsuario;
    private JPanel PanelClave;
    private JPanel PanelFoto;
    private JLabel Foto;

    public LoginVista1() {
        setContentPane(PanelPrincipal);// llamo al pane para q sirva todo slos atributos de arriba
        setTitle("Login");// le pongo titulo
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //para q cuando la aplicacion cierre el programa tambnien
        setSize(1000, 800);// tamanno de la ventana
        setLocationRelativeTo(null);

        // ----------------------
        // Placeholder para CampoUsuario
        // ----------------------
        CampoUsuario.setText("Ingrese su usuario");//aca ponemos lo q queremos que aparezca en gris
        CampoUsuario.setForeground(Color.GRAY);//aca lo ponemos color gris

        CampoUsuario.addFocusListener(new FocusAdapter() { //Le agregamos un listener que detecta cuándo el campo gana o pierde foco (clic del mouse o tabulación)
            @Override
            public void focusGained(FocusEvent e) {                 //focusGained se llama cuando el usuario hace clic en el campo o tabula hacia él.
                if (CampoUsuario.getText().equals("Ingrese su usuario")) {             //
                    CampoUsuario.setText("");                    //Si el texto del campo es igual al placeholder, se borra (setText("")) y se cambia el
                    CampoUsuario.setForeground(Color.BLACK);            //color a negro, para que el usuario vea lo que escribe.
                }
            }

            @Override
            public void focusLost(FocusEvent e) {                       //focusLost se llama cuando el usuario sale del campo (por ejemplo, hace clic en otro lugar).
                                                                            // Si el campo quedó vacío, vuelve a poner el placeholder en gris.
                if (CampoUsuario.getText().isEmpty()) {
                    CampoUsuario.setText("Ingrese su usuario");
                    CampoUsuario.setForeground(Color.GRAY);
                }
            }
        });

        // ----------------------
        // Placeholder para CampoClave
        // ----------------------
        CampoClave.setEchoChar((char)0); // para que el placeholder se vea
        CampoClave.setText("Ingrese su contraseña");
        CampoClave.setForeground(Color.GRAY);

        CampoClave.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(CampoClave.getPassword()).equals("Ingrese su contraseña")) {
                    CampoClave.setText("");
                    CampoClave.setForeground(Color.BLACK);
                    CampoClave.setEchoChar('*'); // ahora sí se ven los asteriscos
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (CampoClave.getPassword().length == 0) {
                    CampoClave.setText("Ingrese su contraseña");
                    CampoClave.setForeground(Color.GRAY);
                    CampoClave.setEchoChar((char)0); // placeholder visible otra vez
                }
            }
        });

        setVisible(true);//muestra la ventana
        SwingUtilities.invokeLater(() -> PanelPrincipal.requestFocusInWindow()); // pone el foco en el panel principal(asi cuando iniciA NO ESTA EL CURSOR en usuario)
    }
    // --- Getters para que el controlador acceda a los componentes ---

    public JButton getBotonIngresar() { return BotonIngresar; }
    public JButton getBotonCancelar() { return BotonCancelar; }
    public JButton getBotonCambiarContra() { return BotonCambiarContra; }

    public JTextField getCampoUsuario() { return CampoUsuario; }
    public JPasswordField getCampoClave() { return CampoClave; }

    public JLabel getMSJ_Error() { return MSJ_Error; }
//-----------------------------------------------------------------------------------------

    public static void main(String[] args) {
        new LoginVista1();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

