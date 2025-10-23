package cliente.modelo;


public class Administrador extends Personal {
    public Administrador(String nombre, String id, String clave, Rol rol) {
        super(nombre, id, clave,rol);

    }

    public Administrador() {
    }


    @Override
    public String toString() {
        return "Administrador{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", clave='" + clave + '\'' +
                ", rol=" + rol +
                '}';
    }

    @Override
    public String tipo() {
        return "Administrador";
    }
}
