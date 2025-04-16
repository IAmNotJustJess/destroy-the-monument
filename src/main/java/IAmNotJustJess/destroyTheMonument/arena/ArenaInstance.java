package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class ArenaInstance {
    private HashMap<TeamColour, ArrayList<Location>> monumentList;
    private HashMap<TeamColour, Integer> monumentCount;
    private HashMap<TeamColour, Location> spawnLocations;
    private ArrayList<TeamColour> teamColours;
    private ArrayList<Player> playerList;
    private HashMap<TeamColour, ArrayList<Player>> playersInTeamsList;
    private Location lobbyLocation;
    private int timer;
    private String timerString;
    private ArenaState arenaState;
    private int repeatingTaskID;

    public void sendMessageGlobally(TextComponent textComponent) {
        for(Player player : playerList) {
            Audience audience = (Audience) player;
            audience.sendMessage(textComponent);
        }
    }

    public void sendMessageToATeam(TeamColour teamColour, TextComponent textComponent) {
        for(Player player : playersInTeamsList.get(teamColour)) {
            Audience audience = (Audience) player;
            audience.sendMessage(textComponent);
        }
    }

    public void arenaTick() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L);
    }

    public void startCountdown() {

        repeatingTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(DestroyTheMonument.class), new Runnable(){
            @Override
            public void run() {
                timer -= 1;
                if(timer <= 0) advanceState();
            }
        }, 0L, 20L);
    };

    public void addPlayerToArena(Player player) {
        playerList.add(player);
        checkPlayerCount();
    }

    public void removePlayerFromArena(Player player) {
        playerList.remove(player);
        if(PlayerCharacterList.getList().containsKey(player)) {
            playersInTeamsList.remove(PlayerCharacterList.getList().get(player).getTeam());
        }
        checkPlayerCount();
    }

    private void checkPlayerCount() {
        double playerRatio = (double) playerList.size() / (ArenaSettings.maxPlayersPerTeam * teamColours.size());
        if(arenaState == ArenaState.LOBBY && playerRatio >= ArenaSettings.startCountdownPlayerPercentageRequirement) {
            advanceState();
        } else if (arenaState == ArenaState.COUNTDOWN && timer > ArenaSettings.arenaCutDownCountdownInSeconds && playerRatio >= ArenaSettings.cutDownCountdownPlayerPercentageRequirement) {
            timer = ArenaSettings.arenaCutDownCountdownInSeconds;
        }
    }

    public void advanceState() {
        switch (arenaState) {
            case LOBBY -> {
                this.arenaState = ArenaState.COUNTDOWN;
                timer = ArenaSettings.arenaBeginCountdownInSeconds;
                startCountdown();
            }
            case COUNTDOWN ->  {
                this.arenaState = ArenaState.STARTING;
                Bukkit.getScheduler().cancelTask(repeatingTaskID);
            }
            case STARTING -> {
                this.arenaState = ArenaState.RUNNING;
                timer = (int) (ArenaSettings.arenaLengthInMinutes * 60);
                startCountdown();
            }
            case RUNNING -> {
                this.arenaState = ArenaState.ENDING;
                Bukkit.getScheduler().cancelTask(repeatingTaskID);
            }
            case ENDING -> {
                this.arenaState = ArenaState.CLEARING;
            }
            case CLEARING -> {
                this.arenaState = ArenaState.LOBBY;
            }
        }
    }

    public ArenaInstance() {
        this.playerList = new ArrayList<>();
        this.teamColours = new ArrayList<>();
        this.monumentCount = new HashMap<>();
        this.monumentList = new HashMap<>();
        this.playersInTeamsList = new HashMap<>();
        this.spawnLocations = new HashMap<>();
        this.arenaState = ArenaState.LOBBY;
    }
}
