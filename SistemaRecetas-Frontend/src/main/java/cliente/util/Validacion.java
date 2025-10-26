package cliente.util;

// Clase para verificar los datos que entran por la controladora

public class Validacion {

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esNumero(String texto) {
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esCorreo(String correo) {
        return correo != null && correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-z]{2,}$");
    }

    public static boolean esPositivo(int valor) {
        return valor > 0;
    }
}
