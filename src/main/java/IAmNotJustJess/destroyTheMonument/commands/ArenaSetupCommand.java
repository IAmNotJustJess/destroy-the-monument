package IAmNotJustJess.destroyTheMonument.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaSetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {

            if(args.length < 2) {
                player.sendMessage("/dtm create <arenaName> - Creates a new arena instance.");
                player.sendMessage("/dtm set <arena> team1 <teamColour> - Set first team's colour.");
                player.sendMessage("/dtm set <arena> team2 <teamColour> - Set second team's colour.");
                player.sendMessage("/dtm set <arena> spawn add <teamColour> - Add a new spawn to the team at your location.");
                player.sendMessage("/dtm set <arena> spawn clear <teamColour> - Remove all spawns of the team.");
                player.sendMessage("/dtm set <arena> monument add <teamColour> - Add a new monument to the team at your location.");
                player.sendMessage("/dtm set <arena> monument clear <teamColour> - Remove all monument locations of the team.");
                player.sendMessage("/dtm set <arena> shop add - Add a new shop at your location.");
                player.sendMessage("/dtm set <arena> shop add - Clear all shops.");
            }
        }

        return false;
    }

}
