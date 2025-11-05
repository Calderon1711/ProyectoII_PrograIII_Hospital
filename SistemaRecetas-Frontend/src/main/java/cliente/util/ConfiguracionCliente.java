package cliente.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfiguracionCliente {

    private static String HOST = "localhost";
    private static int PUERTO = 5000;
    private static int TIMEOUT = 5000;
    private static int TIEMPO_CACHE_MS = 5 * 6_000; // ya no es final para poder configurarlo

    // Método para cargar configuración desde un archivo (opcional)
    public static void cargarConfiguracion() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("cliente.properties")) {
            props.load(fis);

            HOST = props.getProperty("host", HOST);
            PUERTO = Integer.parseInt(props.getProperty("puerto", String.valueOf(PUERTO)));
            TIMEOUT = Integer.parseInt(props.getProperty("timeout", String.valueOf(TIMEOUT)));
            TIEMPO_CACHE_MS = Integer.parseInt(props.getProperty("tiempocache", String.valueOf(TIEMPO_CACHE_MS)));

            System.out.println("Configuración cargada: HOST=" + HOST + ", PUERTO=" + PUERTO + ", TIEMPO_CACHE_MS=" + TIEMPO_CACHE_MS);
        } catch (IOException e) {
            System.out.println("No se encontró archivo de configuración. Se usan valores por defecto.");
        }
    }

    // Getters
    public static String getHost() { return HOST; }
    public static int getPuerto() { return PUERTO; }
    public static int getTimeout() { return TIMEOUT; }
    public static int getTiempoCache() { return TIEMPO_CACHE_MS; }
}
