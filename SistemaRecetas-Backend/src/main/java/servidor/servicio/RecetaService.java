package servidor.servicio;

import servidor.Modelo.DetalleMedicamento;
import servidor.Modelo.Personal;
import servidor.Modelo.Receta;
import servidor.dao.RecetaDAO;
import java.sql.SQLException;
import java.util.List;

public class RecetaService {
    private final RecetaDAO recetaDAO;

    public RecetaService() {
        this.recetaDAO = new RecetaDAO();
    }

    //listar todos los del personal
    public List<Receta> obtenerTodasLasRecetas{
        try{
            return recetaDAO.obtenerTodos();
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return List.of();
        }
    }

    // Crear receta con detalles
    public boolean agregarReceta(Receta receta, List<DetalleMedicamento> detalles) {
        try {
            recetaDAO.crearRecetaConDetalles(receta, detalles);
            return true;
        } catch (SQLException e) {
            System.err.println("Error al crear receta: " + e.getMessage());
            return false;
        }
    }

    //  Actualizar estado de receta
    public boolean actualizarEstado(String idReceta, int nuevoEstado) {
        try {
            return recetaDAO.actualizarEstado(idReceta, nuevoEstado) > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de receta: " + e.getMessage());
            return false;
        }
    }

    //  Agregar detalle a una receta existente
    public boolean agregarDetalle(String idReceta, DetalleMedicamento detalle) {
        try {
            return recetaDAO.agregarDetalle(idReceta, detalle) > 0;
        } catch (SQLException e) {
            System.err.println("Error al agregar detalle a receta: " + e.getMessage());
            return false;
        }
    }

    //  Verificar si una receta existe
    public boolean existeReceta(String idReceta) {
        try {
            return recetaDAO.existeReceta(idReceta);
        } catch (SQLException e) {
            System.err.println("Error al verificar receta: " + e.getMessage());
            return false;
        }
    }

    //  Eliminar receta
    public boolean eliminarReceta(String idReceta) {
        try {
            return recetaDAO.eliminar(idReceta) > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar receta: " + e.getMessage());
            return false;
        }
    }
}
