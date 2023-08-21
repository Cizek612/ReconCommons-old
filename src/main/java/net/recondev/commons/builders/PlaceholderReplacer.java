package net.recondev.commons.builders;


import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("unused")
public class PlaceholderReplacer {

    private final Map<String, String> placeholders = new HashMap<>();
    private boolean usePlaceholderAPI;

    public PlaceholderReplacer addPlaceholder(final String key, final String value) {
        this.placeholders.put(key, value);
        return this;
    }

    public String parse(String args) {
        for (final Map.Entry<String, String> entry : this.placeholders.entrySet()) {
            args = args.replace(entry.getKey(), entry.getValue());
        }
        return args;
    }

    public String parse(final OfflinePlayer player, String args) {
        args = this.parse(args);
        if (!this.usePlaceholderAPI || !Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return args;
        }
        return PlaceholderAPI.setPlaceholders(player, args);
    }

    public List<String> parse(final List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, this.parse(list.get(i)));
        }
        return list;
    }

    public List<String> parse(final OfflinePlayer player, final List<String> list) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, this.parse(player, list.get(i)));
        }
        return list;
    }

    public PlaceholderReplacer setUsePlaceholderAPI(final boolean usePlaceholderAPI) {
        this.usePlaceholderAPI = usePlaceholderAPI;
        return this;
    }
}

