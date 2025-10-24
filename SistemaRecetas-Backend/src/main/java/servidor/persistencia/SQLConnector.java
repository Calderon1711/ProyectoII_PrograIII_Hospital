package servidor.persistencia;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnector {
    private static SQLConnector instance;
    private Connection connection;

    private SQLConnector() {
        try {
            // Cargar el archivo de propiedades desde resources
            Properties props = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo db.properties en resources");
            }
            props.load(input);

            // Obtener los valores
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            // Cargar el driver y crear la conexión
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(" Conexión exitosa a la base de datos!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(" Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public static SQLConnector getInstance() throws SQLException {
        if (instance == null) {
            instance = new SQLConnector();
        }else if(instance.getConnection().isClosed()){
            instance = new SQLConnector();
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}