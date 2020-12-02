package code.aterstones.mctweaks.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * Created by tschm on 28.04.2016.
 */
public class CommandRegisterer {

    private static Field commandMap;
    private static Constructor<PluginCommand> cons;

    static {
        try {
            commandMap = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);

            cons = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            cons.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerCommand(Plugin plugin, String cmd, CommandExecutor cex) {
        registerCommand(plugin, cmd, cmd, cex);
    }

    public static void registerCommand(Plugin plugin, String cmd, String desc, CommandExecutor cex) {

        CommandMap cmdMap = getCommandMap();

        if(cmdMap != null) {
            PluginCommand command = createCmd(cmd, plugin);

            if(command != null) {
                command.setDescription(desc);
                command.setUsage("/" + cmd);

                command.setExecutor(cex);

                cmdMap.register(cmd, command);
            }
        }

    }

    private static PluginCommand createCmd(String cmd, Plugin plugin) {
        try {
            return cons.newInstance(cmd, plugin);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static CommandMap getCommandMap() {
        try {
            return (CommandMap) commandMap.get(Bukkit.getPluginManager());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
