package IAmNotJustJess.destroyTheMonument.commands.tabCompleters;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArenaSetupTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        ArrayList<String> nullList = new ArrayList<>();
        
        if(command.getName().equalsIgnoreCase("dtm")) {
            switch(args.length) {
                case 1 -> {
                    if(sender.hasPermission("dtm.admin")) {
                        return new ArrayList<>() {{
                            add("join");
                            add("leave");
                            add("create");
                            add("set");
                        }};
                    }
                    else {
                        return new ArrayList<>() {{
                            add("join");
                            add("leave");
                        }};
                    }
                }
                case 2 -> {
                    if(sender.hasPermission("dtm.admin")) {
                        switch(args[0]) {
                            case "join", "set" -> {
                                return new ArrayList<>() {{
                                    addAll(ArenaManager.arenaList.keySet());
                                }};
                            }
                        }
                    }
                    else {
                        if (args[0].equals("join")) {
                            return new ArrayList<>() {{
                                addAll(ArenaManager.arenaList.keySet());
                            }};
                        }
                    }
                }
                case 3 -> {
                    if(!Objects.equals(args[0], "set")) return nullList;
                    if(!sender.hasPermission("dtm.admin")) return nullList;
                    return new ArrayList<>() {{
                        add("team1");
                        add("team2");
                        add("spawn");
                        add("monument");
                        add("lobby");
                        add("shop");
                    }};
                }
                case 4 -> {
                    if(!Objects.equals(args[0], "set")) return nullList;
                    if(!sender.hasPermission("dtm.admin")) return nullList;
                    switch(args[2]) {
                        case "team1", "team2" -> {
                            return new ArrayList<>() {{
                                for(TeamColour teamColour : TeamColour.values()) {
                                    if(teamColour == TeamColour.NONE) continue;
                                    add(teamColour.toString().toLowerCase());
                                }
                            }};
                        }
                        case "shop" -> {
                            return new ArrayList<>() {{
                                add("add");
                                add("clear");
                            }};
                        }
                        case "spawn", "monument" -> {
                            return new ArrayList<>() {{
                                for(TeamColour teamColour : ArenaManager.arenaList.get(args[1]).getTeamColours()) {
                                    add(teamColour.toString().toLowerCase());
                                }
                            }};
                        }
                    }
                }
                case 5 -> {
                    if(!Objects.equals(args[0], "set")) return nullList;
                    if(!sender.hasPermission("dtm.admin")) return nullList;
                    switch(args[2]) {
                        case "spawn", "monument" -> {}
                        default -> {
                            return nullList;
                        }
                    }
                    return new ArrayList<>() {{
                        add("add");
                        add("clear");
                    }};
                }
                default -> {
                    return nullList;
                }
            }
        }

        return nullList;
    }
}
