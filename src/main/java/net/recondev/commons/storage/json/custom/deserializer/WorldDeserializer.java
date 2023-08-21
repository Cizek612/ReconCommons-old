package net.recondev.commons.storage.json.custom.deserializer;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.reflect.Type;

public class WorldDeserializer implements JsonDeserializer<World> {


    @Override
    public World deserialize(final JsonElement element, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        final String world = element.getAsJsonObject().getAsString();
        return Bukkit.getWorld(world);
    }


}
