package cliente.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class ConversorJSON {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(cliente.modelo.Personal.class, new PersonalAdapter())
            .create();

    // Convierte cualquier objeto a JSON
    public static String serializar(Object obj) {
        return gson.toJson(obj);
    }

    // Convierte JSON a objeto
    public static <T> T deserializar(String json, Class<T> tipo) {
        return gson.fromJson(json, tipo);
    }

    // Convierte JSON a lista
    public static <T> List<T> deserializarLista(String json, Class<T> tipo) {
        return gson.fromJson(json, TypeToken.getParameterized(List.class, tipo).getType());
    }


}


