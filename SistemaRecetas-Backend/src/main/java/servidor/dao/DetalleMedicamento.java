package servidor.dao;

import servidor.Modelo.Medicamento;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleMedicamento {
    private final SQLConnector connector = SQLConnector.getInstance();

    public List<Medicamento> obtenerTodos() throws SQLException {
        String sql = """
          SELECT codigo_Medicamento, nombre_Medicamento, presentacion_Medicamento
          FROM medicamento ORDER BY nombre_Medicamento
        """;
        List<Medicamento> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Medicamento(
                        rs.getString("nombre_Medicamento"),
                        rs.getString("presentacion_Medicamento"),
                        rs.getString("codigo_Medicamento")
                ));
            }
        }
        return out;
    }

    public Medicamento obtenerPorCodigo(String codigo) throws SQLException {
        String sql = """
          SELECT codigo_Medicamento, nombre_Medicamento, presentacion_Medicamento
          FROM medicamento WHERE codigo_Medicamento=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicamento(
                            rs.getString("nombre_Medicamento"),
                            rs.getString("presentacion_Medicamento"),
                            rs.getString("codigo_Medicamento")
                    );
                }
                return null;
            }
        }
    }

    public int insertar(Medicamento m) throws SQLException {
        String sql = "INSERT INTO medicamento (codigo_Medicamento, nombre_Medicamento, presentacion_Medicamento) VALUES (?,?,?)";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getCodigo());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getPresentacion());
            return ps.executeUpdate();
        }
    }

    public int actualizar(Medicamento m) throws SQLException {
        String sql = "UPDATE medicamento SET nombre_Medicamento=?, presentacion_Medicamento=? WHERE codigo_Medicamento=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getPresentacion());
            ps.setString(3, m.getCodigo());
            return ps.executeUpdate();
        }
    }

    public int eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE codigo_Medicamento=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate();
        }
    }
}
