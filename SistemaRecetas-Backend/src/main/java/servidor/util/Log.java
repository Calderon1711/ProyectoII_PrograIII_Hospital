package servidor.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void info(String mensaje) {
        System.out.println("[INFO] " + LocalDateTime.now().format(FORMATTER) + " - " + mensaje);
    }

    public static void error(String mensaje) {
        System.err.println("[ERROR] " + LocalDateTime.now().format(FORMATTER) + " - " + mensaje);
    }
}
