package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListaMedicamentos {

    private ObservableList<Medicamento> medicamentos;

    // Constructor vacío
    public ListaMedicamentos() {
        this.medicamentos = FXCollections.observableArrayList();
    }

    // Constructor con lista
    public ListaMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = FXCollections.observableArrayList(medicamentos);
    }

    // Getter y Setter
    public ObservableList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = FXCollections.observableArrayList(medicamentos);
    }

    @Override
    public String toString() {
        return "ListaMedicamentos{" +
                "medicamentos=" + medicamentos +
                '}';
    }
}
