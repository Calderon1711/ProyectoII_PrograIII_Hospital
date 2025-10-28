package servidor.dao;

import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Receta;
import servidor.persistencia.SQLConnector;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class RecetaDAO {
    private final SQLConnector connector = SQLConnector.getInstance();


    private static String estadoToDb(int e) {
        return switch (e) {
            case 0 -> "CONFECCIONADA";
            case 1 -> "PROCESO";
            case 2 -> "LISTA";
            case 3 -> "ENTREGADA";
            default -> null; // o lanza una SQLException si prefieres
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
                // 1) Insert receta
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
                // 2) Insert detalles (batch)
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


}
