package servidor.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ConversorJSON {

    private static final Gson gson = new Gson();

    // Convertir objeto a JSON
    public static String serializar(Object obj) {
        if (obj == null) return null;
        return gson.toJson(obj);
    }

    // Convertir JSON a objeto
    public static <T> T deserializar(String json, Class<T> clase) {
        if (json == null || json.isEmpty()) return null;
        return gson.fromJson(json, clase);
    }

    // Convertir JSON a lista de objetos
    public static <T> List<T> deserializarLista(String json, Class<T> clase) {
        if (json == null || json.isEmpty()) return null;
        Type tipoLista = TypeToken.getParameterized(List.class, clase).getType();
        return gson.fromJson(json, tipoLista);
    }
}
