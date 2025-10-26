package servidor.servicio;


import servidor.dao.PacienteDAO;
import servidor.Modelo.Paciente;

import java.sql.SQLException;
import java.util.List;

public class PacienteService {

    private PacienteDAO pacienteDAO;

    public PacienteService() throws SQLException {
        this.pacienteDAO = new PacienteDAO();
    }

    // Listar todos los pacientes
    public List<Paciente> listarPacientes() throws SQLException {
        return pacienteDAO.();
    }

    // Consultar paciente por ID
    public Paciente consultarPaciente(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío.");
        }
        return pacienteDAO.obtenerPaciente(id);
    }

    // Agregar un nuevo paciente
    public void agregarPaciente(Paciente paciente) throws SQLException {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo.");
        }
        if (paciente.getId() == null || paciente.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío.");
        }
        if (paciente.getNombre() == null || paciente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del paciente no puede ser nulo o vacío.");
        }

        pacienteDAO.guardarPaciente(paciente);
    }

    // Actualizar un paciente existente
    public void actualizarPaciente(Paciente paciente) throws SQLException {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo.");
        }
        if (paciente.getId() == null || paciente.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío.");
        }
        pacienteDAO.actualizarPaciente(paciente);
    }

    // Eliminar un paciente por ID
    public void eliminarPaciente(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo o vacío.");
        }
        pacienteDAO.eliminarPaciente(id);
    }
}



