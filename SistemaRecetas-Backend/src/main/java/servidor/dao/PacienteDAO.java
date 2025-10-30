package servidor.dao;

import servidor.Modelo.*;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    public List<Paciente> obtenerTodos() throws SQLException {
        String sql = "SELECT id, nombre, telefono, fecha_nacimiento FROM paciente ORDER BY nombre";
        List<Paciente> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                int telefono = rs.getInt("telefono");
                Date f = rs.getDate("fecha_nacimiento");
                LocalDate fn = (f != null) ? f.toLocalDate() : null;
                Paciente p = new Paciente(telefono, fn, nombre, id);
                out.add(p);
            }
        }
        return out;
    }

    public Paciente obtenerPorId(String idBuscado) throws SQLException {
        String sql = "SELECT id, nombre, telefono, fecha_nacimiento FROM paciente WHERE id = ?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idBuscado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("id");
                    String nombre = rs.getString("nombre");
                    int telefono = rs.getInt("telefono");
                    Date f = rs.getDate("fecha_nacimiento");
                    LocalDate fn = (f != null) ? f.toLocalDate() : null;
                    return new Paciente(telefono, fn, nombre, id);
                }
            }
        }
        return null;
    }

    public int insertar(Paciente p) throws SQLException {
        String sql = "INSERT INTO paciente (id, nombre, telefono, fecha_nacimiento) VALUES (?,?,?,?)";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setInt(3, p.getTelefono());
            if (p.getFechaNacimiento() != null) {
                ps.setDate(4, Date.valueOf(p.getFechaNacimiento()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            return ps.executeUpdate();
        }
    }

    public int actualizar(Paciente p) throws SQLException {
        String sql = "UPDATE paciente SET nombre=?, telefono=?, fecha_nacimiento=? WHERE id=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getTelefono());
            if (p.getFechaNacimiento() != null) {
                ps.setDate(3, Date.valueOf(p.getFechaNacimiento()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            ps.setString(4, p.getId());
            return ps.executeUpdate();
        }
    }

    public int eliminar(String id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    //Comandos Pacientes
    public List<Paciente> listarPacientes() throws SQLException { // LISTAR_PACIENTES
        return obtenerTodos();
    }

    public int agregarPaciente(Paciente p) throws SQLException {   // AGREGAR_PACIENTE
        return insertar(p);
    }

    public int eliminarPaciente(String id) throws SQLException {   // ELIMINAR_PACIENTE
        return eliminar(id);
    }

    public Paciente consultarPaciente(String id) throws SQLException { // CONSULTAR_PACIENTE
        return obtenerPorId(id);
    }

    public int actualizarPaciente(Paciente p) throws SQLException { // ACTUALIZAR_PACIENTE
        return actualizar(p);
    }
}
