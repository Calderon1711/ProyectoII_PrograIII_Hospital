package servidor.Modelo;
import servidor.Modelo.*;

public class Medico extends Personal {
    private String especialidad;

    public Medico(String nombre, String id, String clave, String especialidad,Rol rol) {
        super(nombre, id, clave,rol);
        this.especialidad = especialidad;
        this.rol = Rol.MEDICO;
    }

    public Medico(String especialidad) {
        this.especialidad = especialidad;
    }

    public Medico() {
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String tipo() {
        return "Medico";
    }

    @Override
    public String toString() {
        return nombre;
    }
}
