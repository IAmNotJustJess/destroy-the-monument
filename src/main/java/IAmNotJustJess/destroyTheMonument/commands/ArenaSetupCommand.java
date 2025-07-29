package IAmNotJustJess.destroyTheMonument.commands;

import IAmNotJustJess.destroyTheMonument.arenas.ArenaInstance;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Location;
import org.bukkit.Material;
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
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm create <arena> <#c0c0c0>- <#ffffff>Creates a new arena instance.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> team1 <teamColour> <#c0c0c0>- <#ffffff>Set first team's colour.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> team2 <teamColour> <#c0c0c0>- <#ffffff>Set second team's colour.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> spawn <teamColour> add <#c0c0c0>- <#ffffff>Add a new spawn to the team at your location.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> spawn <teamColour> clear <#c0c0c0>- <#ffffff>Remove all spawns of the team.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> monument <teamColour> add <#c0c0c0>- <#ffffff>Add a new monument to the team at your location.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> monument <teamColour> clear <#c0c0c0>- <#ffffff>Remove all monument locations of the team.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> shop add <#c0c0c0>- <#ffffff>Add a new shop at your location.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> shop clear <#c0c0c0>- <#ffffff>Clear all shops.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm join <arena> <#c0c0c0>- <#ffffff>Join an arena.");
                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm leave <#c0c0c0>- <#ffffff>Leave an arena.");
                return true;
            }

            switch(args[0]) {
                case "create" -> {
                    if(Objects.isNull(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm create <arena>");
                        return true;
                    }
                    if(ArenaManager.arenaList.containsKey(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>This arena instance already exists!");
                        return true;
                    }
                    ArenaManager.arenaList.put(args[1], new ArenaInstance(args[1]));
                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Created the arena instance <#ffffff>" + args[1] + "<#14db4c>!");
                    return true;
                }
                case "set" -> {
                    if(Objects.isNull(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> <options>");
                        return true;
                    }
                    if(!ArenaManager.arenaList.containsKey(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>This arena doesn't exist!");
                        return true;
                    }
                    switch (args[2]) {
                        case "lobby" -> {
                            ArenaManager.arenaList.get(args[1]).setLobbyLocation(player.getLocation().getBlock().getLocation());
                            QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the lobby location!");
                        }
                        case "team1" -> {
                            if(Objects.isNull(args[3]) || args[3].equalsIgnoreCase("none")) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            if(ArenaManager.arenaList.get(args[1]).setFirstTeam(TeamColour.valueOf(args[3].toUpperCase()))) {
                                QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the first team's colour to " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>The second team's colour is " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#cc2b2b>!");
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>You can't set two teams to the same one!");
                            }

                        }
                        case "team2" -> {
                            if(Objects.isNull(args[3])) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            if(ArenaManager.arenaList.get(args[1]).setSecondTeam(TeamColour.valueOf(args[3].toUpperCase()))){
                                QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the second team's colour to " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>The first team's colour is " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#cc2b2b>!");
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>You can't set two teams to the same one!");
                            }

                        }
                        case "spawn" -> {
                            if(Objects.isNull(args[3]) || Objects.isNull(args[4])) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> spawn <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .add(player.getLocation().getBlock().getLocation());
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new spawn location of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared spawn locations of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                                }
                                default -> {
                                    QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> spawn <teamColour> <add/clear>");
                                }
                            }
                            return true;
                        }
                        case "monument" -> {
                            if(Objects.isNull(args[3]) || Objects.isNull(args[4])) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> monument <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .add(player.getLocation().getBlock().getLocation());
                                    player.getLocation().getBlock().setType(Material.OBSIDIAN);
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new monument location of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                                }
                                case "clear" -> {
                                    for(Location location : ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))) {
                                        location.getBlock().setType(Material.AIR);
                                    }
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared monument locations of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).textColour + args[3] + "<#14db4c>!");
                                }
                                default -> {
                                    QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> monument <teamColour> <add/clear>");
                                }
                            }
                            return true;
                        }
                        case "shop" -> {
                            if(Objects.isNull(args[3])) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> shop <add/clear>");
                                return true;
                            }
                            switch(args[3]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .add(player.getLocation().getBlock().getLocation());
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new shop location!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared shop locations!");
                                }
                                default -> {
                                    QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> shop <add/clear>");
                                }
                            }
                            return true;
                        }
                        case null, default -> {
                            QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> <options>");
                        }
                    }
                    return true;
                }
                case "join" -> {
                    if(Objects.isNull(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm join <arena>");
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
