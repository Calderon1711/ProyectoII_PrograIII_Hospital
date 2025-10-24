package servidor.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLExecutor {
    private final SQLConnector dbConnector;

    public SQLExecutor() throws SQLException {
        dbConnector = SQLConnector.getInstance();
    }

    // Ejecutar consultas SELECT

    public ResultSet ejecutaQuery(String sql, Object... params) throws SQLException {
        Connection conn = dbConnector.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);

        // Asignar parámetros
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        return stmt.executeQuery(); // El ResultSet debe cerrarse luego en quien lo use
    }

    // Ejecutar INSERT, UPDATE o DELETE
    public int ejecutaUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        }
    }

    public void cerrarRecursos(ResultSet rs, PreparedStatement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {
            dbConnector.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}