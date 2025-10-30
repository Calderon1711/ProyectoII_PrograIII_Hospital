package servidor.dao;

import servidor.Modelo.Administrador;
import servidor.Modelo.Farmaceuta;
import servidor.Modelo.Medico;
import servidor.Modelo.Personal;
import servidor.Modelo.Rol;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonalDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    private Personal map(ResultSet rs) throws SQLException {
        String id   = rs.getString("id_Personal");
        String nom  = rs.getString("nombre_Personal");
        String cla  = rs.getString("clave_Personal");
        String rolS = rs.getString("rol_Personal");
        Rol rol     = Rol.valueOf(rolS.toUpperCase());

        switch (rol) {
            case MEDICO -> {
                String esp = rs.getString("especialidad_Personal_Medico"); // puede ser null
                return new Medico(nom, id, cla, esp, Rol.MEDICO);
            }
            case FARMACEUTICO -> {
                return new Farmaceuta(nom, id, cla, Rol.FARMACEUTICO);
            }
            case ADMINISTRADOR -> {
                return new Administrador(nom, id, cla, Rol.ADMINISTRADOR);
            }
            default -> throw new SQLException("Rol no soportado: " + rolS);
        }
    }

    public List<Personal> obtenerTodos() throws SQLException {
        String sql = """
            SELECT id_Personal, nombre_Personal, clave_Personal, rol_Personal, especialidad_Personal_Medico
            FROM personal ORDER BY nombre_Personal
        """;
        List<Personal> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    public List<Medico> listarMedicos() throws SQLException {
        String sql = """
            SELECT id_Personal, nombre_Personal, clave_Personal, rol_Personal, especialidad_Personal_Medico
            FROM personal WHERE rol_Personal='MEDICO' ORDER BY nombre_Personal
        """;
        List<Medico> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Personal p = map(rs);
                if (p instanceof Medico m) out.add(m);
            }
        }
        return out;
    }

    public List<Farmaceuta> listarFarmaceutas() throws SQLException {
        String sql = """
            SELECT id_Personal, nombre_Personal, clave_Personal, rol_Personal
            FROM personal WHERE rol_Personal='FARMACEUTICO' ORDER BY nombre_Personal
        """;
        List<Farmaceuta> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Personal p = map(rs);
                if (p instanceof Farmaceuta f) out.add(f);
            }
        }
        return out;
    }

    public Personal obtenerPorId(String id) throws SQLException {
        String sql = """
            SELECT id_Personal, nombre_Personal, clave_Personal, rol_Personal, especialidad_Personal_Medico
            FROM personal WHERE id_Personal=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean existe(String id) throws SQLException {
        String sql = "SELECT 1 FROM personal WHERE id_Personal=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int insertar(Personal p) throws SQLException {
        String sql = """
            INSERT INTO personal (id_Personal, nombre_Personal, clave_Personal, rol_Personal, especialidad_Personal_Medico)
            VALUES (?,?,?,?,?)
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getId());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getClave());
            ps.setString(4, p.getRol().name());
            if (p instanceof Medico m) {
                ps.setString(5, m.getEspecialidad());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            return ps.executeUpdate();
        }
    }

    public int actualizar(Personal p) throws SQLException {
        String sql = """
            UPDATE personal SET nombre_Personal=?, clave_Personal=?, rol_Personal=?, especialidad_Personal_Medico=?
            WHERE id_Personal=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getClave());
            ps.setString(3, p.getRol().name());
            if (p instanceof Medico m) {
                ps.setString(4, m.getEspecialidad());
            } else {
                ps.setNull(4, Types.VARCHAR);
            }
            ps.setString(5, p.getId());
            return ps.executeUpdate();
        }
    }

    public boolean actualizarClave(String id, String nuevaClave) throws SQLException {
        String sql = "UPDATE personal SET clave_Personal=? WHERE id_Personal=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevaClave);
            ps.setString(2, id);
            return ps.executeUpdate() == 1;
        }
    }

    public int eliminar(String id) throws SQLException {
        String sql = "DELETE FROM personal WHERE id_Personal=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        }
    }

    /* Comandos personal*/


    public List<Personal> listarPersonal() throws SQLException {
        return obtenerTodos();
    }

    /*Administrador*/
    public List<Administrador> listarAdministradores() throws SQLException {
        return obtenerTodos().stream()
                .filter(p -> p.getRol() == Rol.ADMINISTRADOR)
                .map(p -> (Administrador) p)
                .toList();
    }

    public int agregarAdministrador(Administrador a) throws SQLException {
        return insertar(a);
    }

    public int eliminarAdministrador(String id) throws SQLException {
        return eliminar(id);
    }

    public Administrador consultarAdministrador(String id) throws SQLException {
        Personal p = obtenerPorId(id);
        return (p instanceof Administrador) ? (Administrador) p : null;
    }

    public int actualizarAdministrador(Administrador a) throws SQLException {
        return actualizar(a);
    }

    /* Médico*/
    public List<Medico> listarMedicosComando() throws SQLException { // evita chocar con listarMedicos()
        return listarMedicos();
    }

    public int agregarMedico(Medico m) throws SQLException {
        return insertar(m);
    }

    public int eliminarMedico(String id) throws SQLException {
        return eliminar(id);
    }

    public Medico consultarMedico(String id) throws SQLException {
        Personal p = obtenerPorId(id);
        return (p instanceof Medico) ? (Medico) p : null;
    }

    public int actualizarMedico(Medico m) throws SQLException {
        return actualizar(m);
    }

    /* Farmacéutica*/
    public List<Farmaceuta> listarFarmaceuticas() throws SQLException {
        return listarFarmaceutas();
    }

    public int agregarFarmaceutica(Farmaceuta f) throws SQLException {
        return insertar(f);
    }

    public int eliminarFarmaceutica(String id) throws SQLException {
        return eliminar(id);
    }

    public Farmaceuta consultarFarmaceutica(String id) throws SQLException {
        Personal p = obtenerPorId(id);
        return (p instanceof Farmaceuta) ? (Farmaceuta) p : null;
    }

    public int actualizarFarmaceutica(Farmaceuta f) throws SQLException {
        return actualizar(f);
    }
}
