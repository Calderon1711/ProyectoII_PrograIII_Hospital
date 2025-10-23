package servidor.Modelo;
import servidor.Modelo.*;

import java.util.Objects;

public class Usuario {
    private String id;
    private String clave;
    private String nombre;
    private Rol rol;


    public Usuario() {}


    public Usuario(String id, String clave, String nombre, Rol rol) {
        this.id = id;
        this.clave = clave;
        this.nombre = nombre;
        this.rol = rol;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }


    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }


    @Override
    public int hashCode() { return Objects.hash(id); }
}
