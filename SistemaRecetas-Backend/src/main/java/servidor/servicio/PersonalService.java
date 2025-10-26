package servidor.servicio;


import servidor.dao.PersonalDAO;
import servidor.Modelo.Personal;
import servidor.Modelo.Rol;

import java.sql.SQLException;
import java.util.List;

public class PersonalService {

    private PersonalDAO personalDAO;

    public PersonalService() throws SQLException {
        this.personalDAO = new PersonalDAO();
    }

    // Listar todo el personal
    public List<Personal> listarPersonal() throws SQLException {
        return personalDAO.obtenerTodos();
    }

    // Consultar personal por ID
    public Personal consultarPersonal(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del personal no puede ser nulo o vacío.");
        }
        return personalDAO.(id);
    }

    // Agregar nuevo personal
    public void agregarPersonal(Personal personal) throws SQLException {
        if (personal == null) {
            throw new IllegalArgumentException("El personal no puede ser nulo.");
        }
        if (personal.getId() == null || personal.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del personal no puede ser nulo o vacío.");
        }
        if (personal.getNombre() == null || personal.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del personal no puede ser nulo o vacío.");
        }
        if (personal.getRol() == null) {
            throw new IllegalArgumentException("El rol del personal no puede ser nulo.");
        }
        personalDAO.guardarPersonal(personal);
    }

    // Actualizar personal existente
    public void actualizarPersonal(Personal personal) throws SQLException {
        if (personal == null) {
            throw new IllegalArgumentException("El personal no puede ser nulo.");
        }
        if (personal.getId() == null || personal.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del personal no puede ser nulo o vacío.");
        }
        personalDAO.actualizarPersonal(personal);
    }

    // Eliminar personal por ID
    public void eliminarPersonal(String id) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del personal no puede ser nulo o vacío.");
        }
        personalDAO.eliminarPersonal(id);
    }
}

