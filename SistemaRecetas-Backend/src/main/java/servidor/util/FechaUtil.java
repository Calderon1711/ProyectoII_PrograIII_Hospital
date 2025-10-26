package servidor.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FechaUtil {

    private static final String FORMATO_DEFAULT = "yyyy-MM-dd";

    // Convertir String a LocalDate
    public static LocalDate stringToLocalDate(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) return null;
        return LocalDate.parse(fecha, DateTimeFormatter.ofPattern(FORMATO_DEFAULT));
    }

    // Convertir LocalDate a String
    public static String localDateToString(LocalDate fecha) {
        if (fecha == null) return null;
        return fecha.format(DateTimeFormatter.ofPattern(FORMATO_DEFAULT));
    }
}
