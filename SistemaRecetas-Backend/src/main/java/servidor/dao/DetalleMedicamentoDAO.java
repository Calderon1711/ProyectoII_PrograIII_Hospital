package servidor.dao;

import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Medicamento;
import servidor.persistencia.SQLConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleMedicamentoDAO {
    private final SQLConnector connector = SQLConnector.getInstance();

    //  Listar todos los detalles de medicamentos
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
                d.setIdDetalle(rs.getString("id_Receta_detalleReceta"));
                d.setMedicamento(med);
                d.setCantidad(rs.getInt("cantidad_DetalleReceta"));
                d.setDuracion(rs.getInt("duracion_DetalleReceta"));
                d.setIndicacion(rs.getString("indicaciones_DetalleReceta"));

                detalles.add(d);
            }
        }
        return detalles;
    }

    //  Insertar un nuevo detalle
    public int insertar(DetalleMedicamento detalle) throws SQLException {
        String sql = """
            INSERT INTO detalle_receta
            (id_Receta_detalleReceta, codigo_Medicamento_DetalleReceta, 
             cantidad_DetalleReceta, duracion_DetalleReceta, indicaciones_DetalleReceta)
            VALUES (?,?,?,?,?)
        """;

        try (Connection c = connector.newConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, detalle.getIdDetalle());
            ps.setString(2, detalle.getMedicamento().getCodigo());
            ps.setInt(3, detalle.getCantidad());
            ps.setInt(4, detalle.getDuracion());
            ps.setString(5, detalle.getIndicacion());

            return ps.executeUpdate();
        }
    }

    //  Consultar detalles de una receta específica
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

    //  Eliminar detalle por receta y medicamento
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
}
