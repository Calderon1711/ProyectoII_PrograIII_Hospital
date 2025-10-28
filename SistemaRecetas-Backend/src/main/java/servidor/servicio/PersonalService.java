package servidor.servicio;


import servidor.Modelo.Paciente;
import servidor.dao.PacienteDAO;
import servidor.dao.PersonalDAO;
import servidor.Modelo.Personal;
import servidor.Modelo.Rol;

import java.sql.SQLException;
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
    public Personal consultarPaciente(String id){
        try{
            return personalDAO.obtenerPorId(id);
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return null;
        }
    }

    //Agregar nuevo personal
    public boolean agregarPersonal(Personal personal){
        try{
            return personalDAO.insertar(personal)>0;
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
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
           return personalDAO.eliminar(id)>0;
        }catch (SQLException e){
            System.out.println("Erro ao listar personal: "+e.getMessage());
            return false;
        }
    }


}


