package net.recondev.commons.command;


import net.recondev.commons.command.error.ArgumentError;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unused")
public interface ArgumentParser {
    List<String> currentArguments = new ArrayList<>();
    AtomicBoolean verboseErrors = new AtomicBoolean(false);
    AtomicReference<CommandSender> currentSender = new AtomicReference();

    default long asLong(final int index) {
        try {
            return Long.parseLong(currentArguments.get(index));
        }
        catch (final NumberFormatException exc) {
            throw new ArgumentError(exc.getMessage(), "&c" + currentArguments.get(index) + " isn't a valid number!", exc);
        }
    }

    default int asInt(final int index) {
        try {
            return Integer.parseInt(currentArguments.get(index));
        }
        catch (final NumberFormatException exc) {
            throw new ArgumentError(exc.getMessage(), "&c" + currentArguments.get(index) + " isn't a valid number!", exc);
        }
    }

    default double asDouble(final int index) {
        try {
            return Double.parseDouble(currentArguments.get(index));
        }
        catch (final NumberFormatException exc) {
            throw new ArgumentError(exc.getMessage(), "&c" + currentArguments.get(index) + " isn't a valid number!", exc);
        }
    }

    default Player asPlayer(final int index) {
        try {
            final Player player = Bukkit.getPlayer(currentArguments.get(index));
            if (player == null) {
                throw new NullPointerException("Player wasn't found!");
            }
            return player;
        }
        catch (final Throwable thr) {
            throw new ArgumentError(thr.getMessage(), "&c" + currentArguments.get(index) + " isn't online!", thr);
        }
    }

    default void assertPlayer() {
        if (currentSender.get() instanceof Player) {
            return;
        }
        try {
            final Player player = (Player)currentSender.get();
        }
        catch (final Throwable thr) {
            throw new ArgumentError(thr.getMessage(), "&cOnly players may use this command!", thr);
        }
    }

    default void assertConsole() {
        if (!(currentSender instanceof ConsoleCommandSender)) {
            return;
        }
        try {
            final ConsoleCommandSender consoleCommandSender = (ConsoleCommandSender) currentSender.get();
        }
        catch (Throwable thr) {
            throw new ArgumentError(thr.getMessage(), "&cOnly players may use this command!", thr);
        }
    }

    default void assertArgumentLength(int length) {
        this.assertArgumentLength(length, "Argument length wasn't correct!");
    }

    default void assertArgumentLength(int length, String string) {
        if (currentArguments.size() >= length) {
            return;
        }
        try {
            throw new IllegalArgumentException(string);
        }
        catch (Throwable thr) {
            throw new ArgumentError(thr.getMessage(), "&cYou only specified " + currentArguments.size() + " arguments. " + length + " required!", thr);
        }
    }
}