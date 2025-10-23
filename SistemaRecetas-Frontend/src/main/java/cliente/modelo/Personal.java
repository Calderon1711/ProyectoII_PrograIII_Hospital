package cliente.modelo;

public abstract class Personal {
    protected String nombre;
    protected String id;
    protected String clave;
    Rol rol;
    public Personal(String nombre, String id, String clave,Rol rol) {
        this.nombre = nombre;
        this.id = id;
        this.clave = clave;
        this.rol = rol;
    }

    public Personal() {

    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }


    //M.V.P
    public Rol getRol() {return rol;}
    public abstract String tipo();
}
