package net.recondev.commons.utils;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ColorUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F-0-9]{6}");
    private static final String MINECRAFT_VERSION = Bukkit.getServer().getBukkitVersion().split("-")[0];
    private static final List<String> ACCEPTED_VERSIONS = Arrays.asList("1.16", "1.17", "1.18", "1.19", "1.20");

    public static String colorize(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', ColorUtil.translateHex(msg));
    }

    public static List<String> colorizeList(final List<String> messages) {
        for (int i = 0; i < messages.size(); ++i) {
            messages.set(i, ColorUtil.colorize(messages.get(i)));
        }
        return messages;
    }



    public static void tell(final CommandSender sender, final String msg) {
        sender.sendMessage(ColorUtil.colorize(msg));
    }

    public static void tellList(final CommandSender sender, final List<String> messages) {
        for (final String msg : ColorUtil.colorizeList(messages)) {
            sender.sendMessage(msg);
        }
    }

    public static String translateHex(String msg) {
        if (msg == null || msg.isEmpty()) {
            return msg;
        }
        if (ACCEPTED_VERSIONS.stream().noneMatch(MINECRAFT_VERSION::startsWith)) {
            return msg;
        }
        Matcher match = HEX_PATTERN.matcher(msg);
        while (match.find()) {
            String color = msg.substring(match.start(), match.end());
            msg = msg.replace(color, net.md_5.bungee.api.ChatColor.valueOf(color) + "");
            match = HEX_PATTERN.matcher(msg);
        }
        return msg;
    }
}

