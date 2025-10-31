package servidor.servicio;

import servidor.Modelo.*;
import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Personal;
import java.util.Collections;
import servidor.Modelo.Receta;
import servidor.dao.RecetaDAO;
import java.sql.SQLException;
import java.util.List;

public class RecetaService {
    private final RecetaDAO recetaDAO;


    public RecetaService() {
        this.recetaDAO = new RecetaDAO();
    }


    // Crear receta con detalles
    public boolean agregarReceta(Receta receta, List<DetalleMedicamento> detalles) {
        if (receta == null) {
            System.err.println("Error: la receta no puede ser nula.");
            return false;
        }
        try {
            recetaDAO.crearRecetaConDetalles(receta, detalles);
            Hospital.getInstance().getRecetas().insertarReceta(receta);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear receta: " + e.getMessage());
            return false;
        }
    }

    // Actualizar estado de receta
    public boolean actualizarEstado(String idReceta, int nuevoEstado) {
        if (idReceta == null) {
            System.err.println("Error: el ID de receta no puede ser nulo.");
            return false;
        }
        try {
            return recetaDAO.actualizarEstado(idReceta, nuevoEstado) > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de receta: " + e.getMessage());
            return false;
        }
    }

    // Agregar detalle a una receta existente
    public boolean agregarDetalle(String idReceta, DetalleMedicamento detalle) {
        if (idReceta == null || detalle == null) {
            System.err.println("Error: el ID de receta o el detalle no pueden ser nulos.");
            return false;
        }
        try {
            return recetaDAO.agregarDetalle(idReceta, detalle) > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar detalle a receta: " + e.getMessage());
            return false;
        }
    }

    // Verificar si una receta existe
    public boolean existeReceta(String idReceta) {
        if (idReceta == null) {
            System.err.println("Error: el ID de receta no puede ser nulo.");
            return false;
        }
        try {
            return recetaDAO.existeReceta(idReceta);
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de receta: " + e.getMessage());
            return false;
        }
    }

    // Eliminar receta
    public boolean eliminarReceta(String idReceta) {
        if (idReceta == null) {
            System.err.println("Error: el ID de receta no puede ser nulo.");
            return false;
        }
        try {
            recetaDAO.eliminar(idReceta);
            Hospital.getInstance().getRecetas().eliminar(idReceta);
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar receta: " + e.getMessage());
            return false;
        }
    }

    // Listar todas las recetas
    public List<Receta> listarRecetas() {
        try {
            return recetaDAO.listarRecetas();
        } catch (SQLException e) {
            System.err.println("Error al listar recetas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Consultar receta por ID
    public Receta consultarReceta(String idReceta) {
        if (idReceta == null) {
            System.err.println("Error: el ID de receta no puede ser nulo.");
            return null;
        }
        try {
            return recetaDAO.consultarReceta(idReceta);
        } catch (SQLException e) {
            System.err.println("Error al consultar receta: " + e.getMessage());
            return null;
        }
    }
}
