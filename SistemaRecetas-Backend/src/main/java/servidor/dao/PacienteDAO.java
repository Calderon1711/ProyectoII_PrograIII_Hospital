package servidor.dao;

import servidor.Modelo.*;
import servidor.Modelo.Rol;
import servidor.persistencia.SQLConnector;
import servidor.persistencia.SQLExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class PacienteDAO {
    private final SQLExecutor executor;

    public PacienteDAO()throws SQLException {
        executor = new SQLExecutor();
    }

    //Guardar paciente
    public void guardarPaciente(Paciente paciente) throws SQLException {
        String sql= "INSERT INTO paciente(id, nombre, fechaNacimiento,telefono) VALUES (?,?,?,?)";
        executor.ejecutaUpdate(sql,paciente.getId(), paciente.getNombre(),paciente.getFechaNacimiento(),paciente.getTelefono());
    }
    //Actualizar paciente
    public void actualizarPaciente(Paciente paciente) throws SQLException {
        String sql= "UPDATE paciente SET nombre=?, fechaNacimiento=?, telefono=? WHERE id=?";
        executor.ejecutaUpdate(sql,
                paciente.getNombre(),
                paciente.getFechaNacimiento(),
                paciente.getTelefono(),
                paciente.getId());

    }
    //actualizar Eliminar paciente
    public void eliminarPaciente(String id) throws SQLException {
        String sql= "DELETE FROM paciente WHERE id=?";
        executor.ejecutaUpdate(sql,id);
    }

    // Obtener paciente por ID
    public Paciente obtenerPaciente(String id) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id=?";
        ResultSet rs = executor.ejecutaQuery(sql, id);
        Paciente paciente = null;
        if (rs.next()) {
            int idInt = Integer.parseInt(rs.getString("id"));
            java.sql.Date sqlDate = rs.getDate("fechaNacimiento"); // obtener fecha de SQL
            java.time.LocalDate fechaNacimiento = null;
            if (sqlDate != null) {
                fechaNacimiento = sqlDate.toLocalDate(); // convertir a LocalDate
            }

            paciente = new Paciente(idInt, fechaNacimiento,  rs.getString("nombre"),rs.getString("id"));

        }
        rs.close();
        return paciente;
    }

    // Obtener todos los pacientes
    public List<Paciente> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM paciente";
        ResultSet rs = executor.ejecutaQuery(sql);
        List<Paciente> lista = new ArrayList<>();
        while (rs.next()) {
            int idInt = Integer.parseInt(rs.getString("id"));
            java.sql.Date sqlDate = rs.getDate("fechaNacimiento");
            java.time.LocalDate fechaNacimiento = null;
            if (sqlDate != null) {
                fechaNacimiento = sqlDate.toLocalDate();
            }

            Paciente paciente = new Paciente(idInt, fechaNacimiento,  rs.getString("nombre"),rs.getString("id"));
            lista.add(paciente);
        }
        rs.close();
        return lista;
    }



}
