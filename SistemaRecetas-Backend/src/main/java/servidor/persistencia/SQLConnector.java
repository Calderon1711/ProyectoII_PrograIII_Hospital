package servidor.persistencia;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnector {
    private static SQLConnector instance;
    private final String url;
    private final String user;
    private final String pass;

    private SQLConnector() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) throw new IllegalStateException("No se encontró db.properties en resources/");
            Properties p = new Properties();
            p.load(in);
            Class.forName(p.getProperty("db.driver")); // com.mysql.cj.jdbc.Driver
            this.url  = p.getProperty("db.url")
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
                    + "&useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSize=250&prepStmtCacheSqlLimit=2048";
            this.user = p.getProperty("db.user");
            this.pass = p.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar SQLConnector", e);
        }
    }

    public static synchronized SQLConnector getInstance() {
        if (instance == null) instance = new SQLConnector();
        return instance;
    }

    public Connection newConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}