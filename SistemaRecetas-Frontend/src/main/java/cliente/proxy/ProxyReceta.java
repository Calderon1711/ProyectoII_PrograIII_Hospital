package cliente.proxy;

import cliente.red.Mensaje;
import cliente.modelo.Receta;
import javafx.collections.ObservableList;

import java.util.List;

public class ProxyReceta extends ProxyService {

    public ObservableList<Receta> listarRecetas() {
        Mensaje resp = enviar("listar_recetas", null);
        if (resp != null && resp.isExito()) {
            return (ObservableList<Receta>) resp.getResultado();
        }
        return null;
    }

    public boolean agregarReceta(Receta receta) {
        Mensaje resp = enviar("agregar_receta", receta);
        return resp != null && resp.isExito();
    }
}
