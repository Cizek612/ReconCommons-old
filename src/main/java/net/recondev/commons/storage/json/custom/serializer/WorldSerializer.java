package net.recondev.commons.storage.json.custom.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.World;

import java.lang.reflect.Type;

public class WorldSerializer implements JsonSerializer<World> {

    @Override
    public JsonElement serialize(final World world, final Type type, final JsonSerializationContext jsonSerializationContext) {

        final JsonObject object = new JsonObject();
        object.addProperty("world", world.getName());

        return object;
    }
}
