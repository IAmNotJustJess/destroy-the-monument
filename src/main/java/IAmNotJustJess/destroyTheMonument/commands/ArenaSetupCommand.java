package IAmNotJustJess.destroyTheMonument.commands;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaInstance;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ArenaSetupCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player player) {

            if(args.length < 1) {
                player.sendMessage("/dtm create <arena> - Creates a new arena instance.");
                player.sendMessage("/dtm set <arena> team1 <teamColour> - Set first team's colour.");
                player.sendMessage("/dtm set <arena> team2 <teamColour> - Set second team's colour.");
                player.sendMessage("/dtm set <arena> spawn <teamColour> add - Add a new spawn to the team at your location.");
                player.sendMessage("/dtm set <arena> spawn <teamColour> clear - Remove all spawns of the team.");
                player.sendMessage("/dtm set <arena> monument <teamColour> add - Add a new monument to the team at your location.");
                player.sendMessage("/dtm set <arena> monument <teamColour> clear - Remove all monument locations of the team.");
                player.sendMessage("/dtm set <arena> shop add - Add a new shop at your location.");
                player.sendMessage("/dtm set <arena> shop clear - Clear all shops.");
                player.sendMessage("/dtm join <arena> - Join an arena.");
                player.sendMessage("/dtm leave - Leave an arena.");
                return true;
            }

            switch(args[0]) {
                case "create" -> {
                    if(Objects.isNull(args[1])) {
                        player.sendMessage("Correct usage: /dtm create <arena>");
                        return true;
                    }
                    if(ArenaManager.arenaList.containsKey(args[1])) {
                        player.sendMessage("This arena instance already exists!");
                        return true;
                    }
                    ArenaManager.arenaList.put(args[1], new ArenaInstance(args[1]));
                    player.sendMessage("Created the arena instance " + args[1] + "!");
                }
                case "set" -> {
                    if(Objects.isNull(args[1])) {
                        player.sendMessage("Correct usage: /dtm set <arena> <options>");
                        return true;
                    }
                    if(!ArenaManager.arenaList.containsKey(args[1])) {
                        player.sendMessage("This arena doesn't exist!");
                        return true;
                    }
                    switch (args[2]) {
                        case "team1" -> {
                            if(Objects.isNull(args[3])) {
                                player.sendMessage("Correct usage: /dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            ArenaManager.arenaList.get(args[1]).setFirstTeam(TeamColour.valueOf(args[3]));
                            player.sendMessage("Set the first team's colour to " + TeamColour.valueOf(args[3]) + "!");
                        }
                        case "team2" -> {
                            if(Objects.isNull(args[3])) {
                                player.sendMessage("Correct usage: /dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            ArenaManager.arenaList.get(args[1]).setSecondTeam(TeamColour.valueOf(args[3]));
                            player.sendMessage("Set the second team's colour to " + TeamColour.valueOf(args[3]) + "!");
                        }
                        case "spawn" -> {
                            if(Objects.isNull(args[3]) || Objects.isNull(args[4])) {
                                player.sendMessage("Correct usage: /dtm set <arena> spawn <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3]))
                                        .add(player.getLocation().getBlock().getLocation());
                                    player.sendMessage("Added a new location!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3]))
                                        .clear();
                                    player.sendMessage("Cleared locations!");
                                }
                                default -> {
                                    player.sendMessage("Correct usage: /dtm set <arena> spawn <teamColour> <add/clear>");
                                }
                            }
                            return true;
                        }
                        case "monument" -> {
                            if(Objects.isNull(args[3]) || Objects.isNull(args[4])) {
                                player.sendMessage("Correct usage: /dtm set <arena> monument <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3]))
                                        .add(player.getLocation().getBlock().getLocation());
                                    player.sendMessage("Added a new location!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3]))
                                        .clear();
                                    player.sendMessage("Cleared locations!");
                                }
                                default -> {
                                    player.sendMessage("Correct usage: /dtm set <arena> monument <teamColour> <add/clear>");
                                }
                            }
                            return true;
                        }
                        case "shop" -> {
                            if(Objects.isNull(args[3])) {
                                player.sendMessage("Correct usage: /dtm set <arena> shop <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[3]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .add(player.getLocation().getBlock().getLocation());
                                    player.sendMessage("Added a new location!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .clear();
                                    player.sendMessage("Cleared locations!");
                                }
                                default -> {
                                    player.sendMessage("Correct usage: /dtm set <arena> shop <add/clear>");
                                }
                            }
                            return true;
                        }
                        case null, default -> {
                            player.sendMessage("Correct usage: /dtm set <arena> <options>");
                        }
                    }
                    return true;
                }
                case "join" -> {
                    if(Objects.isNull(args[1])) {
                        player.sendMessage("Correct usage: /dtm join <arena>");
                        return true;
                    }
                    if(ArenaManager.arenaList.containsKey(args[1])) {
                        ArenaManager.playerJoin(player, args[1]);
                    }
                    return true;
                }
                case "leave" -> {
                    ArenaManager.playerLeave(player);
                    return true;
                }
                default -> {
                    return false;
                }
            }
        }

        return false;
    }

}
