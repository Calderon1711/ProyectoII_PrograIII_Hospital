package cliente.util;

import com.google.gson.*;
import cliente.modelo.*;

import java.lang.reflect.Type;

public class PersonalAdapter implements JsonDeserializer<Personal> {
    @Override
    public Personal deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String tipo = obj.get("tipo").getAsString();

        switch (tipo) {
            case "Medico":
                return context.deserialize(json, Medico.class);
            case "Farmaceutico":
                return context.deserialize(json, Farmaceuta.class);
            case "Administrador":
                return context.deserialize(json, Administrador.class);
            default:
                throw new JsonParseException("Tipo desconocido de Personal: " + tipo);
        }
    }
}
