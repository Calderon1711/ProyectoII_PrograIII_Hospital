package servidor.util;

public class Validador {

    // Validar que un String no sea nulo ni vacío
    public static void validarString(String valor, String mensajeError) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    // Validar que un objeto no sea nulo
    public static void validarObjeto(Object obj, String mensajeError) {
        if (obj == null) {
            throw new IllegalArgumentException(mensajeError);
        }
    }
}
