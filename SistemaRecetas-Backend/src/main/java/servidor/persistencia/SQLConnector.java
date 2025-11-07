package servidor.persistencia;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnector {

    private static SQLConnector instance;
    private String url;
    private String user;
    private String pass;

    private SQLConnector() {
        try (InputStream in = SQLConnector.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (in == null) {
                throw new IllegalStateException(" No se encontró db.properties en resources/");
            }

            Properties p = new Properties();
            p.load(in);

            url = p.getProperty("db.url");
            user = p.getProperty("db.user");
            pass = p.getProperty("db.password");
            String driver = p.getProperty("db.driver");

            if (url == null || user == null || driver == null) {
                throw new IllegalStateException(" db.properties está incompleto o mal configurado.");
            }

            // Registrar el driver JDBC
            Class.forName(driver);
            System.out.println(" Conector JDBC inicializado correctamente");

        } catch (Exception e) {
            System.err.println("️ Error inicializando SQLConnector:");
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar SQLConnector", e);
        }
    }

    public static synchronized SQLConnector getInstance() {
        if (instance == null) {
            instance = new SQLConnector();
        }
        return instance;
    }

    public Connection newConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pass);
        if (conn != null) {
            System.out.println(" Conexión establecida con la base de datos");
        } else {
            System.err.println(" Falló la conexión a la base de datos");
        }
        return conn;
    }
}
