package cliente.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class ListaRecetas {

    private ObservableList<Receta> recetas;

    // Constructor sin parámetros
    public ListaRecetas() {
        this.recetas = FXCollections.observableArrayList();
    }

    // Constructor con parámetros
    public ListaRecetas(List<Receta> recetas) {
        this.recetas = FXCollections.observableArrayList(recetas);
    }

    // Getters y Setters
    public ObservableList<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = FXCollections.observableArrayList(recetas);
    }

    @Override
    public String toString() {
        return "ListaRecetas{" +
                "recetas=" + recetas +
                '}';
    }
}
