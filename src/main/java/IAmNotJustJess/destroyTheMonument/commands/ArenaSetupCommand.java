package IAmNotJustJess.destroyTheMonument.commands;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaInstance;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaManager;
import IAmNotJustJess.destroyTheMonument.arenas.ArenaSettings;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ArenaSetupCommand implements CommandExecutor {

    private void sendCommandList(Player player) {
        QuickSendingMethods.sendToPlayer(player, "<#19a0e3><b>Destroy the Monument <reset>v<#ffffff>" + JavaPlugin.getPlugin(DestroyTheMonument.class).getDescription().getVersion());
        QuickSendingMethods.sendToPlayer(player, " ");
        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Available commands:<#ffffff>");
        QuickSendingMethods.sendToPlayer(player, " ");
        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm join <arena> <#c0c0c0>- <#ffffff>Join an arena.");
        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm leave <#c0c0c0>- <#ffffff>Leave an arena.");
        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm help <#c0c0c0>- <#ffffff>Displays this helpful text! :)");
        if(player.hasPermission("dtm.admin")) {
            QuickSendingMethods.sendToPlayer(player, "");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Administrative and arena set up commands:");
            QuickSendingMethods.sendToPlayer(player, "");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm create <arena> <#c0c0c0>- <#ffffff>Creates a new arena instance.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> team1 <teamColour> <#c0c0c0>- <#ffffff>Set first team's colour.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> team2 <teamColour> <#c0c0c0>- <#ffffff>Set second team's colour.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> spawn <teamColour> add <#c0c0c0>- <#ffffff>Add a new spawn to the team at your location.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> spawn <teamColour> clear <#c0c0c0>- <#ffffff>Remove all spawns of the team.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> monument <teamColour> add <#c0c0c0>- <#ffffff>Add a new monument to the team at your location.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> monument <teamColour> clear <#c0c0c0>- <#ffffff>Remove all monument locations of the team.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> shop add <#c0c0c0>- <#ffffff>Add a new shop at your location.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm set <arena> shop clear <#c0c0c0>- <#ffffff>Clear all shops.");
            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>/dtm info <arena> <#c0c0c0>- <#ffffff>Display information about the arena.");
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(sender instanceof Player player) {

            if(args.length < 1) {
                sendCommandList(player);
                return true;
            }

            switch(args[0]) {
                case "create" -> {
                    if(!player.hasPermission("dtm.admin")) {
                        sendCommandList(player);
                        return true;
                    }
                    if(args.length == 1) {
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
                case "info" -> {
                    if(!player.hasPermission("dtm.admin")) {
                        sendCommandList(player);
                        return true;
                    }
                    if(args.length == 1) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm info <arena>");
                        return true;
                    }
                    if(!ArenaManager.arenaList.containsKey(args[1])) {
                        QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>This arena instance doesn't exist!");
                        return true;
                    }
                    ArenaInstance arenaInstance = ArenaManager.arenaList.get(args[1]);
                    QuickSendingMethods.sendToPlayer(player, "<#19a0e3><b>Info about Arena Instance <reset><#ffffff>" + args[1] + "<#19a0e3><b>!");
                    QuickSendingMethods.sendToPlayer(player, "");
                    QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Arena <#ffffff>state<#19a0e3>: <#ffffff>" + arenaInstance.getArenaState().toString());
                    switch(arenaInstance.getArenaState()) {
                        case LOBBY -> {
                            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Players required to start the arena: <#ffffff>" + (ArenaSettings.arenaStartCountdownPlayerRequirement));
                        }
                        case COUNTDOWN -> {
                            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Seconds until arena starts: <#ffffff>" + arenaInstance.getTimer());
                        }
                        case STARTING, ENDING -> {
                            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Seconds till arena ends: <#ffffff>" + arenaInstance.getTimerString());
                            QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Team remaining monuments: "
                                + TeamManager.list.get(arenaInstance.getFirstTeam()).textColour + arenaInstance.getMonumentRemainingCount().get(arenaInstance.getFirstTeam())
                                + "<#ffffff> : "
                                + TeamManager.list.get(arenaInstance.getSecondTeam()).textColour + arenaInstance.getMonumentRemainingCount().get(arenaInstance.getSecondTeam())
                            );
                        }
                    }
                    QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Arena <#ffffff>player count<#19a0e3>: <#ffffff>" + arenaInstance.getPlayerList().size() + "/" + ArenaSettings.maxPlayersPerTeam * 2);
                    if(arenaInstance.getLobbyLocation() != null) {
                        QuickSendingMethods.sendToPlayer(player, "<#ffffff>Lobby <#19a0e3>location:");
                        QuickSendingMethods.sendToPlayer(player, "<#ffffff>- x: <x> y:<y> z:<z> in <world>"
                            .replace("<x>", Double.toString(arenaInstance.getLobbyLocation().getX()))
                            .replace("<y>", Double.toString(arenaInstance.getLobbyLocation().getY()))
                            .replace("<z>", Double.toString(arenaInstance.getLobbyLocation().getZ()))
                            .replace("<world>", arenaInstance.getLobbyLocation().getWorld().getName())
                        );
                    }
                    else {
                        QuickSendingMethods.sendToPlayer(player, "<#ffffff>Lobby <#19a0e3>location: <#cc2b2b>unset");
                    }
                    if(arenaInstance.getFirstTeam() == TeamColour.NONE) {
                        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>First <#ffffff>team's colour<#19a0e3>: <#cc2b2b>unset");
                    }
                    else {
                        QuickSendingMethods.sendToPlayer(player, "<#19a0e3>First <#ffffff>team's colour<#19a0e3>: " + TeamManager.list.get(arenaInstance.getFirstTeam()).name);
                    }

                    TeamColour teamColour = arenaInstance.getFirstTeam();
                    for(int i = 0; i < 2; i++) {
                        if(i == 1) {
                            if(arenaInstance.getFirstTeam() == TeamColour.NONE) {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>First <#ffffff>team's colour<#19a0e3>: <#cc2b2b>unset");
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Second <#ffffff>team's colour<#19a0e3>: " + TeamManager.list.get(teamColour).name);
                            }

                        }
                        if(teamColour != TeamColour.NONE) {
                            if(!arenaInstance.getSpawnLocations().get(teamColour).isEmpty()) {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Their <#ffffff>spawn <#19a0e3>locations:");
                                int j = 0;
                                for(Location location : arenaInstance.getSpawnLocations().get(teamColour)) {
                                    QuickSendingMethods.sendToPlayer(player, "<#19a0e3> <number><#ffffff>- x: <x> y:<y> z:<z> in <world>"
                                        .replace("<number>", Integer.toString(j))
                                        .replace("<x>", Double.toString(location.getX()))
                                        .replace("<y>", Double.toString(location.getY()))
                                        .replace("<z>", Double.toString(location.getZ()))
                                        .replace("<world>", location.getWorld().getName())
                                    );
                                    j++;
                                }
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Their <#ffffff>spawn <#19a0e3>locations: <#cc2b2b>unset");
                            }
                            if(!arenaInstance.getMonumentList().get(teamColour).isEmpty()) {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Their <#ffffff>spawn <#19a0e3>locations:");
                                int j = 0;
                                for(Location location : arenaInstance.getMonumentList().get(teamColour)) {
                                    QuickSendingMethods.sendToPlayer(player, "<#19a0e3><number> <#ffffff>- x: <x> y:<y> z:<z> in <world>"
                                        .replace("<number>", Integer.toString(j))
                                        .replace("<x>", Double.toString(location.getX()))
                                        .replace("<y>", Double.toString(location.getY()))
                                        .replace("<z>", Double.toString(location.getZ()))
                                        .replace("<world>", location.getWorld().getName())
                                    );
                                    j++;
                                }
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#19a0e3>Their <#ffffff>spawn <#19a0e3>locations: <#cc2b2b>unset");
                            }
                        }
                        teamColour = arenaInstance.getSecondTeam();
                    }
                    if(!arenaInstance.getShopLocations().isEmpty()) {
                        QuickSendingMethods.sendToPlayer(player, "<#ffffff>Shop <#19a0e3>locations:");
                        int j = 0;
                        for(Location location : arenaInstance.getShopLocations()) {
                            QuickSendingMethods.sendToPlayer(player, "<#19a0e3><number><#ffffff>- x: <x> y:<y> z:<z> in <world>"
                                .replace("<number>", Integer.toString(j))
                                .replace("<x>", Double.toString(location.getX()))
                                .replace("<y>", Double.toString(location.getY()))
                                .replace("<z>", Double.toString(location.getZ()))
                                .replace("<world>", location.getWorld().getName())
                            );
                            j++;
                        }
                    }
                    else {
                        QuickSendingMethods.sendToPlayer(player, "<#ffffff>Shop <#19a0e3>locations: <#cc2b2b>unset");
                    }
                    return true;
                }
                case "set" -> {
                    if(!player.hasPermission("dtm.admin")) {
                        sendCommandList(player);
                        return true;
                    }
                    if(args.length == 1) {
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
                            QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the <#ffffff>lobby location<#14db4c>!");
                        }
                        case "team1" -> {
                            if(args.length == 3 || args[3].equalsIgnoreCase("none")) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            if(ArenaManager.arenaList.get(args[1]).setFirstTeam(TeamColour.valueOf(args[3].toUpperCase()))) {
                                QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the <#ffffff>first team's colour<#14db4c> to " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>The <#ffffff>second team's colour<#cc2b2b> is " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#cc2b2b>!");
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>You can't set two teams to the same one!");
                            }

                        }
                        case "team2" -> {
                            if(args.length == 3) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> team1 <teamColour>");
                                return true;
                            }
                            if(ArenaManager.arenaList.get(args[1]).setSecondTeam(TeamColour.valueOf(args[3].toUpperCase()))){
                                QuickSendingMethods.sendToPlayer(player, "<#14db4c>Successfully set the <#ffffff>second team's colour<#14db4c> to " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                            }
                            else {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>The <#ffffff>first team's colour<#cc2b2b> is " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#cc2b2b>!");
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>You can't set two teams to the same one!");
                            }

                        }
                        case "spawn" -> {
                            if(args.length == 3 || args.length == 4) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> spawn <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .add(player.getLocation().getBlock().getLocation());
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new <#ffffff>spawn location<#14db4c> of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getSpawnLocations().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared <#ffffff>spawn locations<#14db4c> of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                                }
                                default -> QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> spawn <teamColour> <add/clear>");
                            }
                            return true;
                        }
                        case "monument" -> {
                            if(args.length == 3 || args.length == 4) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> monument <teamColour> <add/clear>");
                                return true;
                            }
                            switch(args[4]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .add(player.getLocation().getBlock().getLocation());
                                    player.getLocation().getBlock().setType(Material.OBSIDIAN);
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new <#ffffff>monument location<#14db4c> of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                                }
                                case "clear" -> {
                                    for(Location location : ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))) {
                                        location.getBlock().setType(Material.AIR);
                                    }
                                    ArenaManager.arenaList.get(args[1]).getMonumentList().get(TeamColour.valueOf(args[3].toUpperCase()))
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared <#ffffff>monument locations<#14db4c> of team " + TeamManager.list.get(TeamColour.valueOf(args[3].toUpperCase())).name + "<#14db4c>!");
                                }
                                default -> QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> monument <teamColour> <add/clear>");
                            }
                            return true;
                        }
                        case "shop" -> {
                            if(args.length == 3) {
                                QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> shop <add/clear>");
                                return true;
                            }
                            switch(args[3]) {
                                case "add" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .add(player.getLocation().getBlock().getLocation());
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Added a new <#ffffff>shop location<#14db4c>!");
                                }
                                case "clear" -> {
                                    ArenaManager.arenaList.get(args[1]).getShopLocations()
                                        .clear();
                                    QuickSendingMethods.sendToPlayer(player, "<#14db4c>Cleared <#ffffff>shop locations<#14db4c>!");
                                }
                                default -> QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> shop <add/clear>");
                            }
                            return true;
                        }
                        case null, default -> QuickSendingMethods.sendToPlayer(player, "<#cc2b2b>Correct usage: <#ffffff>/dtm set <arena> <options>");
                    }
                    return true;
                }
                case "join" -> {
                    if(args.length == 1) {
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
                    sendCommandList(player);
                    return true;
                }
            }
        }

        return false;
    }

}
