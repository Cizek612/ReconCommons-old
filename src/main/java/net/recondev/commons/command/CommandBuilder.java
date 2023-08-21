package net.recondev.commons.command;



import net.recondev.commons.command.error.ArgumentError;
import net.recondev.commons.utils.ColorUtil;
import net.recondev.commons.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public abstract class CommandBuilder extends Command implements ArgumentParser {

    private final String name;
    private final List<String> aliases;
    private List<SubCommand> subCommands;

    public CommandBuilder(final String name) {
        this(name, new ArrayList<>());
    }

    public CommandBuilder(final String name, final List<String> aliases) {
        super(name);
        this.setAliases(aliases);
        this.name = name;
        this.aliases = aliases;
    }

    public abstract boolean onCommand(final CommandSender var1, final String[] var2);

    public void register() {
        CommandUtils.registerCommand(this);
    }

    public void unregister() {
        CommandUtils.unregisterCommand(this);
    }

    public boolean execute(final CommandSender sender, final String label, String[] args) {
        boolean result;
        block6:
        {
            if (args.length > 0 && sender.hasPermission("") && args[args.length - 1].equals("--ve")) {
                String[] newArgs = new String[args.length - 1];
                System.arraycopy(args, 0, newArgs, 0, args.length - 1);
                args = newArgs;
                verboseErrors.set(true);
            }
            currentSender.set(sender);
            currentArguments.addAll(Arrays.asList(args));
            result = true;
            try {
                result = this.onCommand(sender, args);
            } catch (final ArgumentError error) {
                sender.sendMessage(ColorUtil.colorize(error.getPrettyMessage()));
                if (!verboseErrors.get()) break block6;
                sender.sendMessage(ColorUtil.colorize("&cCaused By: " + error.getBack().getClass().getName() + ": " + error.getBack().getMessage()));
                boolean flip = false;
                for (StackTraceElement element : error.getBack().getStackTrace()) {
                    sender.sendMessage(ColorUtil.colorize(((flip ^= true) ? "&4" : "&c") + element.toString()));
                }
            } catch (final Throwable thr) {
                sender.sendMessage(ColorUtil.colorize("&cThere was an error processing your command!"));
                thr.printStackTrace();
                if (!verboseErrors.get()) break block6;
                sender.sendMessage(ColorUtil.colorize("&6Caused By: " + thr.getClass().getName() + ": " + thr.getMessage()));
                boolean flip = false;
                for (StackTraceElement element : thr.getStackTrace()) {
                    sender.sendMessage(ColorUtil.colorize(((flip ^= true) ? "&4" : "&c") + element.toString()));
                }
            }
        }
        currentSender.set(null);
        currentArguments.clear();
        verboseErrors.set(false);
        return result;
    }


    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        List<String> tab = new ArrayList<>();
        if (this.subCommands == null) {
            return tab;
        }
        if (args.length == 0 || args.length == 1) {
            for (final SubCommand subCommand : this.subCommands) {
                if (!sender.hasPermission(subCommand.getPermission())) continue;
                tab.add(subCommand.getName());
            }
            return tab;
        }
        final String firstArgument = args[0];
        for (final SubCommand subCommand : this.subCommands) {
            if (!subCommand.getName().equalsIgnoreCase(firstArgument) || !sender.hasPermission(subCommand.getPermission()))
                continue;
            tab = subCommand.onTabComplete(sender, args);
        }
        final String arg = args[args.length - 1];
        final ArrayList<String> newTab = new ArrayList<>();
        for (final String argument : tab) {
            if (!argument.toLowerCase().startsWith(arg.toLowerCase())) continue;
            newTab.add(argument);
        }
        if (newTab.isEmpty()) {
            return tab;
        }
        return newTab;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<SubCommand> getSubCommands() {
        return this.subCommands;
    }

    public void setSubCommands(List<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }
}
