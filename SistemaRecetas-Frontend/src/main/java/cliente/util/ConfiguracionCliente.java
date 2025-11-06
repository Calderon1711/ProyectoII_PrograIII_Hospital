package cliente.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionCliente {

    private static String HOST = "localhost";
    private static int PUERTO = 5000;
    private static int TIMEOUT = 5000;
    private static int TIEMPO_CACHE_MS = 5 * 6_000; // 30 segundos

    // Método para cargar configuración desde un archivo
    public static void cargarConfiguracion() {
        Properties propiedades = new Properties();

        try {
            // 1️⃣ Buscar primero en la carpeta del proyecto
            File file = new File("SistemaRecetas-Frontend/cliente.properties");

            // 2️⃣ Si no existe, intentar buscar dentro de resources (si está en src/main/resources)
            InputStream input;
            if (file.exists()) {
                input = new FileInputStream(file);
            } else {
                input = ConfiguracionCliente.class.getClassLoader().getResourceAsStream("cliente.properties");
            }

            if (input == null) {
                System.out.println("⚠️ No se encontró archivo de configuración. Se usan valores por defecto.");
                return;
            }

            // 3️⃣ Cargar propiedades
            propiedades.load(input);

            HOST = propiedades.getProperty("host", "localhost");
            PUERTO = Integer.parseInt(propiedades.getProperty("puerto", "5000"));
            TIMEOUT = Integer.parseInt(propiedades.getProperty("timeout", "5000"));
            TIEMPO_CACHE_MS = Integer.parseInt(propiedades.getProperty("tiempocache", "30000"));

            System.out.println("✅ Configuración cargada: HOST=" + HOST + ", PUERTO=" + PUERTO + ", TIEMPO_CACHE_MS=" + TIEMPO_CACHE_MS);

        } catch (IOException e) {
            System.out.println("⚠️ Error al cargar la configuración: " + e.getMessage());
        }
    }

    // Getters
    public static String getHost() { return HOST; }
    public static int getPuerto() { return PUERTO; }
    public static int getTimeout() { return TIMEOUT; }
    public static int getTiempoCache() { return TIEMPO_CACHE_MS; }
}
