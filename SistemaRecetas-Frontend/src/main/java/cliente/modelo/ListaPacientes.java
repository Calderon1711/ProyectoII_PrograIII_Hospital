package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListaPacientes {

    private ObservableList<Paciente> pacientes;

    // Constructor vacío
    public ListaPacientes() {
        this.pacientes = FXCollections.observableArrayList();
    }

    // Constructor con lista
    public ListaPacientes(List<Paciente> pacientes) {
        this.pacientes = FXCollections.observableArrayList(pacientes);
    }

    // Getter y Setter
    public ObservableList<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = FXCollections.observableArrayList(pacientes);
    }

    @Override
    public String toString() {
        return "ListaPacientes{" +
                "pacientes=" + pacientes +
                '}';
    }
}
