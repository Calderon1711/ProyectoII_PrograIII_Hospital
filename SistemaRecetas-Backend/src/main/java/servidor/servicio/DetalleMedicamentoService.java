package servidor.servicio;


import servidor.Modelo.DetalleMedicamento;
import servidor.dao.DetalleMedicamentoDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Collections;

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

    // Insertar un nuevo detalle
    public boolean agregarDetalle(DetalleMedicamento detalle) {
        if (detalle == null) {
            System.err.println("Error: el detalle no puede ser nulo");
            return false;
        }
        try {
            return detalleDAO.insertar(detalle) > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar detalle de medicamento: " + e.getMessage());
            return false;
        }
    }

    // Eliminar un detalle por receta y código de medicamento
    public boolean eliminarDetalle(String idReceta, String codigoMedicamento) {
        if (idReceta == null || codigoMedicamento == null) {
            System.err.println("Error: idReceta o codigoMedicamento son nulos");
            return false;
        }
        try {
            return detalleDAO.eliminar(idReceta, codigoMedicamento) > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            return false;
        }
    }

    // Obtener todos los detalles de una receta específica
    public List<DetalleMedicamento> obtenerPorReceta(String idReceta) {
        if (idReceta == null) {
            System.err.println("Error: el id de receta no puede ser nulo");
            return Collections.emptyList();
        }
        try {
            return detalleDAO.obtenerPorReceta(idReceta);
        } catch (SQLException e) {
            System.err.println("Error al obtener detalles por receta: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Consultar un detalle específico de una receta y medicamento
    public DetalleMedicamento consultarDetalleMedicamento(String idReceta, String codigoMedicamento) {
        if (idReceta == null || codigoMedicamento == null) {
            System.err.println("Error: idReceta o codigoMedicamento son nulos");
            return null;
        }
        try {
            return detalleDAO.consultarDetalleMedicamento(idReceta, codigoMedicamento);
        } catch (SQLException e) {
            System.err.println("Error al consultar detalle de medicamento: " + e.getMessage());
            return null;
        }
    }

    // Actualizar un detalle existente
    public boolean actualizarDetalle(DetalleMedicamento detalle) {
        if (detalle == null) {
            System.err.println("Error: el detalle no puede ser nulo");
            return false;
        }
        try {
            return detalleDAO.actualizarDetalleMedicamento(detalle) > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar detalle de medicamento: " + e.getMessage());
            return false;
        }
    }

}
