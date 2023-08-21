package net.recondev.commons.chat;


import java.lang.reflect.Constructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class Title {
    public static void sendPacket(final Player player, final Object packet) {
        try {
            final  Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", Title.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (final ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void sendTitle(final Player player, final Integer fadeIn, final Integer stay, final Integer fadeOut, String title, String subtitle) {
        try {
            Constructor<?> subtitleConstructor;
            Object e;
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                e = Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatTitle = Title.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Title.getNMSClass("PacketPlayOutTitle").getConstructor(Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], Title.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
                Title.sendPacket(player, titlePacket);
                e = Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = Title.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Title.getNMSClass("PacketPlayOutTitle").getConstructor(Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], Title.getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                Title.sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                e = Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = Title.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = Title.getNMSClass("PacketPlayOutTitle").getConstructor(Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], Title.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                Title.sendPacket(player, subtitlePacket);
                e = Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = Title.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = Title.getNMSClass("PacketPlayOutTitle").getConstructor(Title.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], Title.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                Title.sendPacket(player, subtitlePacket);
            }
        }
        catch (final Exception var11) {
            var11.printStackTrace();
        }
    }
}
