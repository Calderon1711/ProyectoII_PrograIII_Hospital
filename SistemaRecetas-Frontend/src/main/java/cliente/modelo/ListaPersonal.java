package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListaPersonal {

    private ObservableList<Personal> personal;

    // Constructor vacío
    public ListaPersonal() {
        this.personal = FXCollections.observableArrayList();
    }

    // Constructor con lista
    public ListaPersonal(List<Personal> personal) {
        this.personal = FXCollections.observableArrayList(personal);
    }

    // Getter y Setter
    public ObservableList<Personal> getPersonal() {
        return personal;
    }

    public void setPersonal(List<Personal> personal) {
        this.personal = FXCollections.observableArrayList(personal);
    }

    @Override
    public String toString() {
        return "ListaPersonal{" +
                "personal=" + personal +
                '}';
    }
}
