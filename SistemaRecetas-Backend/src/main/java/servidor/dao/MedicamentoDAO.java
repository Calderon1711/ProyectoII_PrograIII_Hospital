package servidor.dao;

import servidor.Modelo.Medicamento;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    public List<Medicamento> listarCatalogo() throws SQLException {
        String sql = "SELECT codigo, nombre, presentacion FROM medicamento ORDER BY nombre";
        List<Medicamento> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String codigo = rs.getString("codigo");
                String nombre = rs.getString("nombre");
                String presentacion = rs.getString("presentacion");
                out.add(new Medicamento(nombre, presentacion, codigo));
            }
        }
        return out;
    }

    public Medicamento obtenerPorCodigo(String codigoBuscado) throws SQLException {
        String sql = "SELECT codigo, nombre, presentacion FROM medicamento WHERE codigo = ?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigoBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medicamento(rs.getString("nombre"),
                            rs.getString("presentacion"),
                            rs.getString("codigo"));
                }
            }
        }
        return null;
    }

    public int insertar(Medicamento m) throws SQLException {
        String sql = "INSERT INTO medicamento (codigo, nombre, presentacion) VALUES (?,?,?)";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getCodigo());
            ps.setString(2, m.getNombre());
            ps.setString(3, m.getPresentacion());
            return ps.executeUpdate();
        }
    }

    public int actualizar(Medicamento m) throws SQLException {
        String sql = "UPDATE medicamento SET nombre=?, presentacion=? WHERE codigo=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getPresentacion());
            ps.setString(3, m.getCodigo());
            return ps.executeUpdate();
        }
    }

    public int eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM medicamento WHERE codigo=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, codigo);
            return ps.executeUpdate();
        }
    }
}
