package net.recondev.commons;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.recondev.commons.menus.MenuManager;
import net.recondev.commons.storage.json.custom.deserializer.WorldDeserializer;
import net.recondev.commons.storage.json.custom.serializer.WorldSerializer;
import net.recondev.commons.utils.ColorUtil;
import net.recondev.commons.utils.MathUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public class ReconCommons extends JavaPlugin {

    @Getter public static ReconCommons commons;
    @Getter private static Gson gson;
    @Getter private MathUtils mathUtils;
    @Getter private ColorUtil colorUtil;
    @Getter private MenuManager menuManager;

    @Override
    public void onEnable() {
        commons = this;

        final Metrics metrics = new Metrics(this, 17793);


        gson = new GsonBuilder()

               // .registerTypeAdapter(World.class, new WorldSerializer())
              //  .registerTypeAdapter(World.class, new WorldDeserializer())
                .setPrettyPrinting().create();



        loadMenus();

    }

    private void loadMenus() {
        this.menuManager = new MenuManager();
    }


}
