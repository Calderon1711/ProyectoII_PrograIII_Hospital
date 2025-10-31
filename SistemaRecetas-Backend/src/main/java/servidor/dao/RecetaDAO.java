package servidor.dao;

import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Medico;
import servidor.Modelo.Paciente;
import servidor.Modelo.Receta;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecetaDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    private static String estadoToDb(int e) {
        return switch (e) {
            case 0 -> "CONFECCIONADA";
            case 1 -> "PROCESO";
            case 2 -> "LISTA";
            case 3 -> "ENTREGADA";
            default -> null;
        };
    }

    private static int dbToEstado(String e) {
        if (e == null) return 0;
        return switch (e.toUpperCase()) {
            case "PROCESO" -> 1;
            case "LISTA" -> 2;
            case "ENTREGADA" -> 3;
            default -> 0; // CONFECCIONADA
        };
    }

    public void crearRecetaConDetalles(Receta r, List<DetalleMedicamento> detalles) throws SQLException {
        String sqlReceta = """
            INSERT INTO recetas
              (id_Receta, id_PersonalReceta, id_PacienteReceta,
               fecha_Prescripcion_Receta, fecha_Retiro_Receta, estado_Receta)
            VALUES (?,?,?,?,?,?)
            """;
        String sqlDet = """
            INSERT INTO detalle_receta
              (id_Receta_detalleReceta, codigo_Medicamento_DetalleReceta,
               detalle_DetalleReceta, cantidad_DetalleReceta,
               duracion_DetalleReceta, indicaciones_DetalleReceta)
            VALUES (?,?,?,?,?,?)
            """;
        try (Connection c = connector.newConnection()) {
            boolean oldAuto = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                // 1) Receta
                try (PreparedStatement ps = c.prepareStatement(sqlReceta)) {
                    ps.setString(1, r.getId());
                    ps.setString(2, r.getPersonal().getId());
                    ps.setString(3, r.getPaciente().getId());

                    if (r.getFechaPrescripcion() != null) {
                        ps.setDate(4, Date.valueOf(r.getFechaPrescripcion()));
                    } else {
                        ps.setNull(4, Types.DATE);
                    }

                    if (r.getFechaRetiro() != null) {
                        ps.setDate(5, Date.valueOf(r.getFechaRetiro()));
                    } else {
                        ps.setNull(5, Types.DATE);
                    }

                    ps.setString(6, estadoToDb(r.getEstado()));
                    ps.executeUpdate();
                }

                // 2) Detalles (batch)
                if (detalles != null && !detalles.isEmpty()) {
                    try (PreparedStatement ps = c.prepareStatement(sqlDet)) {
                        for (DetalleMedicamento d : detalles) {
                            ps.setString(1, r.getId());
                            ps.setString(2, d.getMedicamento().getCodigo());
                            ps.setString(3, d.getIndicacion());
                            ps.setInt(4, d.getCantidad());
                            ps.setInt(5, d.getDuracion());
                            ps.setString(6, d.getIndicacion());
                            ps.addBatch();
                        }
                        ps.executeBatch();
                    }
                }

                c.commit();
                c.setAutoCommit(oldAuto);
            } catch (SQLException ex) {
                c.rollback();
                c.setAutoCommit(oldAuto);
                throw ex;
            }
        }
    }

    public int actualizarEstado(String idReceta, int nuevoEstado) throws SQLException {
        String sql = "UPDATE recetas SET estado_Receta=? WHERE id_Receta=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, estadoToDb(nuevoEstado));
            ps.setString(2, idReceta);
            return ps.executeUpdate();
        }
    }

    public int agregarDetalle(String idReceta, DetalleMedicamento d) throws SQLException {
        String sql = """
            INSERT INTO detalle_receta
              (id_Receta_detalleReceta, codigo_Medicamento_DetalleReceta,
               detalle_DetalleReceta, cantidad_DetalleReceta,
               duracion_DetalleReceta, indicaciones_DetalleReceta)
            VALUES (?,?,?,?,?,?)
            """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            ps.setString(2, d.getMedicamento().getCodigo());
            ps.setString(3, d.getIndicacion());
            ps.setInt(4, d.getCantidad());
            ps.setInt(5, d.getDuracion());
            ps.setString(6, d.getIndicacion());
            return ps.executeUpdate();
        }
    }

    public boolean existeReceta(String idReceta) throws SQLException {
        String sql = "SELECT 1 FROM recetas WHERE id_Receta=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public int eliminar(String idReceta) throws SQLException {
        String sql = "DELETE FROM recetas WHERE id_Receta=?";
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            return ps.executeUpdate();
        }
    }

    //Comandos Receta


    public List<Receta> listarRecetas() throws SQLException {
        String sql = """
            SELECT id_Receta, id_PersonalReceta, id_PacienteReceta,
                   fecha_Prescripcion_Receta, fecha_Retiro_Receta, estado_Receta
            FROM recetas
            ORDER BY fecha_Prescripcion_Receta DESC, id_Receta
        """;
        List<Receta> out = new ArrayList<>();
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Receta r = new Receta();
                r.setId(rs.getString("id_Receta"));
                Date f1 = rs.getDate("fecha_Prescripcion_Receta");
                Date f2 = rs.getDate("fecha_Retiro_Receta");
                r.setFechaPrescripcion(f1 != null ? f1.toLocalDate() : null);
                r.setFechaRetiro(f2 != null ? f2.toLocalDate() : null);
                r.setEstado(dbToEstado(rs.getString("estado_Receta")));
                Medico med = new Medico(); med.setId(rs.getString("id_PersonalReceta")); r.setPersonal(med);
                Paciente pac = new Paciente(); pac.setId(rs.getString("id_PacienteReceta")); r.setPaciente(pac);
                out.add(r);
            }
        }
        return out;
    }

    public Receta consultarReceta(String idReceta) throws SQLException {
        String sql = """
            SELECT id_Receta, id_PersonalReceta, id_PacienteReceta,
                   fecha_Prescripcion_Receta, fecha_Retiro_Receta, estado_Receta
            FROM recetas WHERE id_Receta=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Receta r = new Receta();
                r.setId(rs.getString("id_Receta"));
                Date f1 = rs.getDate("fecha_Prescripcion_Receta");
                Date f2 = rs.getDate("fecha_Retiro_Receta");
                r.setFechaPrescripcion(f1 != null ? f1.toLocalDate() : null);
                r.setFechaRetiro(f2 != null ? f2.toLocalDate() : null);
                r.setEstado(dbToEstado(rs.getString("estado_Receta")));
                Medico med = new Medico(); med.setId(rs.getString("id_PersonalReceta")); r.setPersonal(med);
                Paciente pac = new Paciente(); pac.setId(rs.getString("id_PacienteReceta")); r.setPaciente(pac);
                return r;
            }
        }
    }

}
