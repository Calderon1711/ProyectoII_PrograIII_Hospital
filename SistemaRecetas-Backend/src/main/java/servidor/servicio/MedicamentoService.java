package servidor.servicio;


import servidor.Modelo.Medicamento;
import servidor.dao.MedicamentoDAO;
import java.sql.SQLException;
import java.util.List;

public class MedicamentoService {
    private final MedicamentoDAO medicamentoDAO;

    public MedicamentoService() {
        this.medicamentoDAO = new MedicamentoDAO();
    }

    public List<Medicamento> obtenerMedicamentos() {
        try {
            return medicamentoDAO.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al listar medicamentos: " + e.getMessage());
            return List.of();
        }
    }

    public Medicamento consultarMedicamento(String codigo) {
        try {
            return medicamentoDAO.obtenerPorCodigo(codigo);
        } catch (SQLException e) {
            System.err.println("Error al consultar medicamento: " + e.getMessage());
            return null;
        }
    }

    public boolean agregarMedicamento(Medicamento medicamento) {
        try {
            return medicamentoDAO.insertar(medicamento) > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarMedicamento(Medicamento medicamento) {
        try {
            return medicamentoDAO.actualizar(medicamento) > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar medicamento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarMedicamento(String codigo) {
        try {
            return medicamentoDAO.eliminar(codigo) > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar medicamento: " + e.getMessage());
            return false;
        }
    }
}

