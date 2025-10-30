package cliente.util;

public class ConfiguracionCliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    private static final int TIMEOUT = 5000;
    private static final int TIEMPODECACHE =  5 * 6_000;

    public static String getHost() { return HOST; }
    public static int getPuerto() { return PUERTO; }
    public static int getTimeout() { return TIMEOUT; }
    public static int getTimedeCache() { return TIEMPODECACHE; }
}
