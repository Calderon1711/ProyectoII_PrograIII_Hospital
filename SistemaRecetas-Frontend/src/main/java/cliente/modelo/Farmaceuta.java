package cliente.modelo;

public class Farmaceuta extends Personal {
    public Farmaceuta(String nombre, String id, String clave,Rol rol) {
        super(nombre, id, clave,rol);

    }

    public Farmaceuta() {
    }

    @Override
    public String tipo() {
        return "Farmaceuta";
    }

    @Override
    public String toString() {
        return "Farmaceuta{" +
                "nombre='" + nombre + '\'' +
                ", id='" + id + '\'' +
                ", clave='" + clave + '\'' +
                ", rol=" + rol +
                '}';
    }
}
