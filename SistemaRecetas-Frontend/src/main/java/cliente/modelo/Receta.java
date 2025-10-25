package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.util.List;

public class Receta {

    private Personal personal;
    private Paciente paciente;
    private LocalDate fechaPrescripcion;
    private LocalDate fechaRetiro;
    private int estado; // 1: Procesada - 2: Confeccionada - 3: Lista - 4: Entregada
    private ObservableList<DetalleMedicamento> detalleMedicamentos;
    private String id;

    // Constructor vacío
    public Receta() {
        this.personal = new Medico();
        this.paciente = new Paciente();
        this.detalleMedicamentos = FXCollections.observableArrayList();
    }

    // Constructor completo
    public Receta(String id, Personal personal, Paciente paciente, LocalDate fechaPrescripcion, LocalDate fechaRetiro, int estado) {
        this.id = id;
        this.personal = personal;
        this.paciente = paciente;
        this.fechaPrescripcion = fechaPrescripcion;
        this.fechaRetiro = fechaRetiro;
        this.estado = estado;
        this.detalleMedicamentos = FXCollections.observableArrayList();
    }

    // Getters y Setters
    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public LocalDate getFechaPrescripcion() {
        return fechaPrescripcion;
    }

    public void setFechaPrescripcion(LocalDate fechaPrescripcion) {
        this.fechaPrescripcion = fechaPrescripcion;
    }

    public LocalDate getFechaRetiro() {
        return fechaRetiro;
    }

    public void setFechaRetiro(LocalDate fechaRetiro) {
        this.fechaRetiro = fechaRetiro;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public ObservableList<DetalleMedicamento> getDetalleMedicamentos() {
        return detalleMedicamentos;
    }

    public void setDetalleMedicamentos(List<DetalleMedicamento> detalleMedicamentos) {
        this.detalleMedicamentos = FXCollections.observableArrayList(detalleMedicamentos);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // toString solo para depuración
    @Override
    public String toString() {
        return "Receta{" +
                "id='" + id + '\'' +
                ", personal=" + personal +
                ", paciente=" + paciente +
                ", fechaPrescripcion=" + fechaPrescripcion +
                ", fechaRetiro=" + fechaRetiro +
                ", estado=" + estado +
                ", detalleMedicamentos=" + detalleMedicamentos +
                '}';
    }
}
