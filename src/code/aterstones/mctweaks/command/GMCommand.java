package code.aterstones.mctweaks.command;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GMCommand implements CommandExecutor {

    private HashMap<String, GameMode> modeMapping = new HashMap<>();

    public GMCommand() {
        modeMapping.put("0", GameMode.SURVIVAL);
        modeMapping.put("s", GameMode.SURVIVAL);
        modeMapping.put("survival", GameMode.SURVIVAL);
        
        modeMapping.put("1", GameMode.CREATIVE);
        modeMapping.put("c", GameMode.CREATIVE);
        modeMapping.put("creative", GameMode.CREATIVE);
        
        modeMapping.put("2", GameMode.ADVENTURE);
        modeMapping.put("a", GameMode.ADVENTURE);
        modeMapping.put("adventure", GameMode.ADVENTURE);

        modeMapping.put("3", GameMode.SPECTATOR);
        modeMapping.put("sp", GameMode.SPECTATOR);
        modeMapping.put("spectator", GameMode.SPECTATOR);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdStr, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.isOp()) {
                if(args.length == 1) {
                    GameMode mapped = modeMapping.get(args[0]);

                    if(mapped != null) {
                        p.setGameMode(mapped);
                        p.sendMessage("Set Gamemode to " + mapped.name());
                    } else {
                        p.sendMessage("Couldn't find any matching GameMode for " + args[0]);
                    }
                } else {
                    p.sendMessage("/gm <mode> - modes include 0, 1, 2, s, c, a, sp");
                }
            } else {
                p.sendMessage("Only ops allowed");
            }
        } else {
            sender.sendMessage("Only for players");
        }

        return true;
    }
}
