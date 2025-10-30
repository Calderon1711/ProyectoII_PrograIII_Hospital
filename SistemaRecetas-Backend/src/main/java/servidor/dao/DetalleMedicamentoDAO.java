package servidor.dao;

import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Medicamento;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleMedicamentoDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    public List<DetalleMedicamento> obtenerTodos() throws SQLException {
        String sql = """
            SELECT 
                id_Receta_detalleReceta,
                codigo_Medicamento_DetalleReceta,
                cantidad_DetalleReceta,
                duracion_DetalleReceta,
                indicaciones_DetalleReceta
            FROM detalle_receta
            ORDER BY id_Receta_detalleReceta
        """;

        List<DetalleMedicamento> detalles = new ArrayList<>();

        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medicamento med = new Medicamento();
                med.setCodigo(rs.getString("codigo_Medicamento_DetalleReceta"));

                DetalleMedicamento d = new DetalleMedicamento();
                d.setIdDetalle(rs.getString("id_Receta_detalleReceta")); // tu modelo usa este setter
                d.setMedicamento(med);
                d.setCantidad(rs.getInt("cantidad_DetalleReceta"));
                d.setDuracion(rs.getInt("duracion_DetalleReceta"));
                d.setIndicacion(rs.getString("indicaciones_DetalleReceta"));

                detalles.add(d);
            }
        }
        return detalles;
    }

    public int insertar(DetalleMedicamento detalle) throws SQLException {
        String sql = """
            INSERT INTO detalle_receta
            (id_Receta_detalleReceta, codigo_Medicamento_DetalleReceta, 
             cantidad_DetalleReceta, duracion_DetalleReceta, indicaciones_DetalleReceta)
            VALUES (?,?,?,?,?)
        """;

        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, detalle.getIdDetalle());                    // id receta en tu modelo
            ps.setString(2, detalle.getMedicamento().getCodigo());
            ps.setInt(3, detalle.getCantidad());
            ps.setInt(4, detalle.getDuracion());
            ps.setString(5, detalle.getIndicacion());

            return ps.executeUpdate();
        }
    }

    public List<DetalleMedicamento> obtenerPorReceta(String idReceta) throws SQLException {
        String sql = """
            SELECT 
                id_Receta_detalleReceta,
                codigo_Medicamento_DetalleReceta,
                cantidad_DetalleReceta,
                duracion_DetalleReceta,
                indicaciones_DetalleReceta
            FROM detalle_receta
            WHERE id_Receta_detalleReceta = ?
        """;

        List<DetalleMedicamento> detalles = new ArrayList<>();

        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicamento med = new Medicamento();
                    med.setCodigo(rs.getString("codigo_Medicamento_DetalleReceta"));

                    DetalleMedicamento d = new DetalleMedicamento();
                    d.setIdDetalle(rs.getString("id_Receta_detalleReceta"));
                    d.setMedicamento(med);
                    d.setCantidad(rs.getInt("cantidad_DetalleReceta"));
                    d.setDuracion(rs.getInt("duracion_DetalleReceta"));
                    d.setIndicacion(rs.getString("indicaciones_DetalleReceta"));

                    detalles.add(d);
                }
            }
        }
        return detalles;
    }

    public int eliminar(String idReceta, String codigoMedicamento) throws SQLException {
        String sql = """
            DELETE FROM detalle_receta 
            WHERE id_Receta_detalleReceta=? 
              AND codigo_Medicamento_DetalleReceta=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            ps.setString(2, codigoMedicamento);
            return ps.executeUpdate();
        }
    }

  //Comandos DetalleMedicamento
    public List<DetalleMedicamento> listarDetalleMedicamentos() throws SQLException {
        return obtenerTodos();
    }

    public List<DetalleMedicamento> listarDetalleMedicamentos(String idReceta) throws SQLException {
        return obtenerPorReceta(idReceta);
    }

    public int agregarDetalleMedicamento(DetalleMedicamento d) throws SQLException {
        return insertar(d);
    }

    public int eliminarDetalleMedicamento(String idReceta, String codigoMedicamento) throws SQLException {
        return eliminar(idReceta, codigoMedicamento);
    }

    public DetalleMedicamento consultarDetalleMedicamento(String idReceta, String codigoMedicamento) throws SQLException {
        String sql = """
            SELECT id_Receta_detalleReceta, codigo_Medicamento_DetalleReceta,
                   cantidad_DetalleReceta, duracion_DetalleReceta, indicaciones_DetalleReceta
            FROM detalle_receta
            WHERE id_Receta_detalleReceta=? AND codigo_Medicamento_DetalleReceta=?
            LIMIT 1
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, idReceta);
            ps.setString(2, codigoMedicamento);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Medicamento med = new Medicamento();
                med.setCodigo(rs.getString("codigo_Medicamento_DetalleReceta"));

                DetalleMedicamento d = new DetalleMedicamento();
                d.setIdDetalle(rs.getString("id_Receta_detalleReceta"));
                d.setMedicamento(med);
                d.setCantidad(rs.getInt("cantidad_DetalleReceta"));
                d.setDuracion(rs.getInt("duracion_DetalleReceta"));
                d.setIndicacion(rs.getString("indicaciones_DetalleReceta"));
                return d;
            }
        }
    }

    public int actualizarDetalleMedicamento(DetalleMedicamento d) throws SQLException {
        String sql = """
            UPDATE detalle_receta
               SET cantidad_DetalleReceta=?,
                   duracion_DetalleReceta=?,
                   indicaciones_DetalleReceta=?
             WHERE id_Receta_detalleReceta=? 
               AND codigo_Medicamento_DetalleReceta=?
        """;
        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getDuracion());
            ps.setString(3, d.getIndicacion());
            ps.setString(4, d.getIdDetalle()); // id de receta en tu modelo
            ps.setString(5, d.getMedicamento().getCodigo());
            return ps.executeUpdate();
        }
    }
}
