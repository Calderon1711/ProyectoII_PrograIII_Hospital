package cliente.modelo;

public class DetalleMedicamento {

    private Medicamento medicamento;
    private String idDetalle;
    private int cantidad;
    private int duracion;
    private String indicacion;

    // Constructor vacío
    public DetalleMedicamento() {
    }

    // Constructor completo
    public DetalleMedicamento(Medicamento medicamento, String idDetalle, int cantidad, int duracion, String indicacion) {
        this.medicamento = medicamento;
        this.idDetalle = idDetalle;
        this.cantidad = cantidad;
        this.duracion = duracion;
        this.indicacion = indicacion;
    }

    // Getters y Setters
    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getIndicacion() {
        return indicacion;
    }

    public void setIndicacion(String indicacion) {
        this.indicacion = indicacion;
    }

    @Override
    public String toString() {
        return "DetalleMedicamento{" +
                "medicamento=" + medicamento +
                ", idDetalle='" + idDetalle + '\'' +
                ", cantidad=" + cantidad +
                ", duracion=" + duracion +
                ", indicacion='" + indicacion + '\'' +
                '}';
    }
}
