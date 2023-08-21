package net.recondev.commons.command;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandSender;

@SuppressWarnings("unused")
public abstract class SubCommand {
    private final String name;
    private final String permission;
    private final List<String> aliases;

    public SubCommand(final String name, final String permission) {
        this.name = name;
        this.permission = permission;
        this.aliases = new ArrayList<>();
    }

    public SubCommand(final String name, final String permission, final List<String> aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public abstract void onCommand(final CommandSender var1, final String[] var2);

    public List<String> onTabComplete(final CommandSender commandSender, final String[] args) {
        return Arrays.asList(" ");
    }

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public List<String> getAliases() {
        return this.aliases;
    }
}
