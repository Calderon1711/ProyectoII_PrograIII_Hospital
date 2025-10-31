package servidor.servicio;

import servidor.Modelo.Hospital;
import servidor.dao.PacienteDAO;
import servidor.Modelo.Paciente;
import java.sql.SQLException;
import java.util.List;

public class PacienteService {
    private final PacienteDAO pacienteDAO;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAO();
    }

    // Listar todos los pacientes
    public List<Paciente> obtenerTodosPacientes() {
        try {
            return pacienteDAO.obtenerTodos();
        } catch (SQLException e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
            return List.of();
        }
    }

    //  Consultar un paciente por ID
    public Paciente consultarPaciente(String id) {
        try {
            return pacienteDAO.obtenerPorId(id);
        } catch (SQLException e) {
            System.err.println("Error al consultar paciente: " + e.getMessage());
            return null;
        }
    }

    //  Agregar nuevo paciente
    public boolean agregarPaciente(Paciente paciente) {
        try {
            boolean credenciales= Hospital.getInstance().getPacientes().existeAlguienConEseID(paciente.getId());
            if (!credenciales) {
                System.out.println("Paciente existente con ese id");
                return false;
            }
                pacienteDAO.insertar(paciente);
               Hospital.getInstance().getPacientes().insertarPaciente(paciente,false);

            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar paciente: " + e.getMessage());
            return false;
        }
    }

    //  Actualizar paciente
    public boolean actualizarPaciente(Paciente paciente) {
        try {
            return pacienteDAO.actualizar(paciente) > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar paciente: " + e.getMessage());
            return false;
        }
    }

    //   Eliminar paciente
    public boolean eliminarPaciente(String id) {
        try {
            Hospital.getInstance().getPacientes().eliminar(id);
            pacienteDAO.eliminar(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar paciente: " + e.getMessage());
            return false;
        }
    }
}
