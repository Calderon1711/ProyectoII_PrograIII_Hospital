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
                throw new IllegalStateException("No se encontró db.properties en resources/");
            }

            Properties p = new Properties();
            p.load(in);

            url = p.getProperty("db.url");
            user = p.getProperty("db.user");
            pass = p.getProperty("db.password");
            String driver = p.getProperty("db.driver");

            Class.forName(driver);

        } catch (Exception e) {
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
        return DriverManager.getConnection(url, user, pass);
    }
}
