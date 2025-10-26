package servidor.dao;

import servidor.Modelo.*;
import servidor.Modelo.Rol;
import servidor.persistencia.SQLExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonalDAO {
    private final SQLExecutor executor;

    public PersonalDAO() throws SQLException {
        executor = new SQLExecutor();
    }

    // Guardar cualquier tipo de personal
    public void guardar(Personal personal) throws SQLException {
        String sql = "INSERT INTO personal (id, nombre, clave, especialidad, rol) VALUES (?, ?, ?, ?, ?)";
        String especialidad = null;
        if (personal instanceof Medico) {
            especialidad = ((Medico) personal).getEspecialidad();
        }
        executor.ejecutaUpdate(sql,
                personal.getId(),
                personal.getNombre(),
                personal.getClave(),
                especialidad,
                personal.getRol().toString());
    }

    // Actualizar personal existente
    public void actualizar(Personal personal) throws SQLException {
        String sql = "UPDATE personal SET nombre = ?, clave = ?, especialidad = ?, rol = ? WHERE id = ?";
        String especialidad = null;
        if (personal instanceof Medico) {
            especialidad = ((Medico) personal).getEspecialidad();
        }
        executor.ejecutaUpdate(sql,
                personal.getNombre(),
                personal.getClave(),
                especialidad,
                personal.getRol().toString(),
                personal.getId());
    }

    // Eliminar personal
    public void eliminar(String id) throws SQLException {
        String sql = "DELETE FROM personal WHERE id = ?";
        executor.ejecutaUpdate(sql, id);
    }

    // Obtener personal por ID
    public Personal obtener(String id) throws SQLException {
        String sql = "SELECT * FROM personal WHERE id = ?";
        ResultSet rs = executor.ejecutaQuery(sql, id);
        Personal personal = null;
        if (rs.next()) {
            Rol rol = Rol.valueOf(rs.getString("rol"));
            if (rol == Rol.MEDICO) {
                personal = new Medico(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("especialidad"),
                        Rol.MEDICO
                );
            } else if (rol == Rol.ADMINISTRADOR) {
                personal = new Administrador(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        Rol.ADMINISTRADOR
                );
            } else if (rol == Rol.FARMACEUTICO) {
                personal = new Farmaceuta(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        Rol.FARMACEUTICO
                );
            }
        }
        rs.close();
        return personal;
    }

    // Obtener todos los personales
    public List<Personal> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM Personal";
        ResultSet rs = executor.ejecutaQuery(sql);
        List<Personal> lista = new ArrayList<>();
        while (rs.next()) {
            Rol rol = Rol.valueOf(rs.getString("rol"));
            Personal personal = null;
            if (rol == Rol.MEDICO) {
                personal = new Medico(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        rs.getString("especialidad"),
                        Rol.MEDICO
                );
            } else if (rol == Rol.ADMINISTRADOR) {
                personal = new Administrador(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        Rol.ADMINISTRADOR
                );
            } else if (rol == Rol.FARMACEUTICO) {
                personal = new Farmaceuta(
                        rs.getString("nombre"),
                        rs.getString("id"),
                        rs.getString("clave"),
                        Rol.FARMACEUTICO
                );
            }
            lista.add(personal);
        }
        rs.close();
        return lista;
    }
}
