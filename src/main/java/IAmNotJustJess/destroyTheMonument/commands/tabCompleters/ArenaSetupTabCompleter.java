package IAmNotJustJess.destroyTheMonument.commands.tabCompleters;

import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class ArenaSetupTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(command.getName().equalsIgnoreCase("dtm") && args.length >= 3) {
            if(args[2].equalsIgnoreCase("team1") || args[2].equalsIgnoreCase("team2")) {
                return new ArrayList<>() {{
                    for(TeamColour teamColour : TeamColour.values()) {
                        add(teamColour.toString());
                    }
                }};
            }
        }

        return null;
    }
}
