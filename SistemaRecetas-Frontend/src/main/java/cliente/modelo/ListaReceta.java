package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListaReceta {

    private ObservableList<Receta> recetas;

    // Constructor sin parámetros
    public ListaReceta() {
        this.recetas = FXCollections.observableArrayList();
    }

    // Constructor con parámetros
    public ListaReceta(List<Receta> recetas) {
        this.recetas = FXCollections.observableArrayList(recetas);
    }

    // Getter y Setter
    public ObservableList<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = FXCollections.observableArrayList(recetas);
    }

    @Override
    public String toString() {
        return "ListaReceta{" +
                "recetas=" + recetas +
                '}';
    }
}
