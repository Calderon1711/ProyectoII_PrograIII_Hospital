package servidor.servicio;

import servidor.Modelo.*;
import servidor.Modelo.Administrador;
import servidor.Modelo.Farmaceuta;
import servidor.Modelo.Medico;
import servidor.Modelo.Personal;
import servidor.dao.PersonalDAO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PersonalService {

    private final PersonalDAO personalDAO;


    public PersonalService(){
        this.personalDAO = new PersonalDAO();
    }

    //listar todos los del personal
    public List<Personal> obtenerTodoPersonal(){
        try{
            return personalDAO.obtenerTodos();
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return List.of();
        }
    }

    //Consultar un personal por ID
    public Personal consultarPersonal(String id){
        try{
            return personalDAO.obtenerPorId(id);
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return null;
        }
    }

    //Agregar nuevo personal
    public boolean agregarPersonal(Personal personal){
        try {
            // Verifica si ya existe en la lista
            Personal existente = Hospital.getInstance()
                    .getPersonal()
                    .verificarCredenciales(personal.getId(), personal.getClave());
            if (existente != null) {
                System.out.println("El personal ya existe en la lista del Hospital.");
                return false;
            }

            // Inserta en la lista del Hospital
            boolean insertadoEnLista = Hospital.getInstance()
                    .getPersonal()
                    .insertarPersonal(personal, false);

            if (!insertadoEnLista) {
                System.out.println("No se pudo insertar el personal en la lista del Hospital.");
                return false;
            }

            // Inserta en la base de datos
            return personalDAO.insertar(personal) > 0;

        } catch (SQLException e) {
            System.out.println("Error al agregar personal: " + e.getMessage());
            return false;
        }
    }

    //Actualizar persoanl
    public boolean actualizarPersonal(Personal personal){
        try{
            return personalDAO.actualizar(personal)>0;
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return false;
        }
    }

    //eliminar personal
    public boolean eliminarPersonal(String id){
        try{
            Hospital.getInstance().getPersonal().eliminar(id);
            personalDAO.eliminar(id);
           return true;
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return false;
        }
    }

    // Listar solo médicos
    public List<Medico> listarMedicos() {
        try {
            return personalDAO.listarMedicos();
        } catch (SQLException e) {
            System.err.println("Error al listar médicos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Listar solo farmacéuticos
    public List<Farmaceuta> listarFarmaceutas() {
        try {
            return personalDAO.listarFarmaceutas();
        } catch (SQLException e) {
            System.err.println("Error al listar farmacéuticos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Listar solo administradores
    public List<Administrador> listarAdministradores() {
        try {
            return personalDAO.listarAdministradores();
        } catch (SQLException e) {
            System.err.println("Error al listar administradores: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Consultar un administrador específico
    public Administrador consultarAdministrador(String id) {
        if (id == null) {
            System.err.println("Error: el ID no puede ser nulo.");
            return null;
        }
        try {
            return personalDAO.consultarAdministrador(id);
        } catch (SQLException e) {
            System.err.println("Error al consultar administrador: " + e.getMessage());
            return null;
        }
    }

    // Verificar si un personal existe
    public boolean existePersonal(String id) {
        if (id == null) {
            System.err.println("Error: el ID no puede ser nulo.");
            return false;
        }
        try {
            return personalDAO.existe(id);
        } catch (SQLException e) {
            System.err.println("Error al verificar existencia de personal: " + e.getMessage());
            return false;
        }
    }

    // Actualizar solo la clave del personal
    public boolean actualizarClave(String id, String nuevaClave) {
        if (id == null || nuevaClave == null) {
            System.err.println("Error: ni el ID ni la nueva clave pueden ser nulos.");
            return false;
        }
        try {
            return personalDAO.actualizarClave(id, nuevaClave);
        } catch (SQLException e) {
            System.err.println("Error al actualizar clave del personal: " + e.getMessage());
            return false;
        }
    }

}


