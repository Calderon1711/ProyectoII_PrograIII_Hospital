package servidor.servicio;


import servidor.Modelo.DetalleMedicamento;
import servidor.dao.DetalleMedicamentoDAO;
import java.sql.SQLException;
import java.util.List;

public class DetalleMedicamentoService {
    private final DetalleMedicamentoDAO detalleDAO;

    public DetalleMedicamentoService() {
        this.detalleDAO = new DetalleMedicamentoDAO();
    }

    public List<DetalleMedicamento> listarDetalles() {
        try {
            return detalleDAO.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al listar detalles de medicamentos: " + e.getMessage());
            return List.of();
        }
    }

    public boolean agregarDetalle(DetalleMedicamento detalle) {
        try {
            return detalleDAO.insertar(detalle) > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarDetalle(String idReceta, String codigoMedicamento) {
        try {
            return detalleDAO.eliminar(idReceta,codigoMedicamento) > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            return false;
        }
    }
}
