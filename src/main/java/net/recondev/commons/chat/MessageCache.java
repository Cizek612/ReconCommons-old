package net.recondev.commons.chat;

import net.recondev.commons.builders.PlaceholderReplacer;
import net.recondev.commons.patterns.Registry;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class MessageCache implements Registry<String, Message> {
    private final FileConfiguration config;
    @Getter private final Map<String, Message> registry;

    public MessageCache(final FileConfiguration config) {
        this.config = config;
        this.registry = new HashMap<>();
    }

    public MessageCache sendMessage(final Player player, final String path) {
        if (this.hasMessage(path)) {
            this.getMessage(path).send(player);
        }
        return this;
    }

    public MessageCache sendMessage(final Player player, final PlaceholderReplacer placeholders, final String path) {
        if (this.hasMessage(path)) {
            this.getMessage(path).send(player, placeholders);
        }
        return this;
    }

    public MessageCache loadMessage(final String path) {
        this.registry.put(path.toLowerCase(), new Message(this, path));
        return this;
    }

    public boolean hasMessage(final String path) {
        return this.registry.containsKey(path.toLowerCase());
    }

    public Message getMessage(final String path) {
        return this.registry.get(path.toLowerCase());
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
