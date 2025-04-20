package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.utility.MinutesTimerConverter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
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
    private BukkitTask tickTask;

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

    public void sendTitleGlobally(TextComponent title, TextComponent subtitle, Long fadeInMills, Long holdMills, Long fadeOutMills) {
        Title titleMessage = Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
        for(Player player : playerList) {

            Audience audience = (Audience) player;
            audience.showTitle(titleMessage);
        }
    }

    public void sendTitleToATeam(TeamColour teamColour, TextComponent title, TextComponent subtitle, Long fadeInMills, Long holdMills, Long fadeOutMills) {
        Title titleMessage = Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
        for(Player player : playersInTeamsList.get(teamColour)) {

            Audience audience = (Audience) player;
            audience.showTitle(titleMessage);
        }
    }

    public void sendExplanation() {

        ArrayList<String> messages = new ArrayList<>();

        messages.add("Witaj na Destroy the Monument!");
        messages.add("Twoim zadaniem jest zniszczenie monumentów przeciwnika!");
        messages.add("Zniszcząc wszystkie monumenty wygrasz arenę!");
        messages.add("Po końcu czasu, drużyna z większą ilością monumentów wygrywa!");
        messages.add("Powodzenia!");

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> subtitles = new ArrayList<>();

        titles.add("Witaj na");
        subtitles.add("Destroy the Monument");
        titles.add("Twoje zadanie:");
        subtitles.add("Niszcz monumenty!");
        titles.add("Wygrasz");
        subtitles.add("Zniszcząc je wszystkie!");
        titles.add("Lub");
        subtitles.add("Mając ich więcej.");
        titles.add("Powodzenia!");
        subtitles.add("Przyda się :P");

        for(int i = 0; i < messages.size(); i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendTitleGlobally(Component.text(titles.get(finalI)), Component.text(subtitles.get(finalI)), 0L, 5000L, 0L);
                    sendMessageGlobally(Component.text(messages.get(finalI)));
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L * i);
        }

    }

    public void startCountdown() {

        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                timer -= 1;
                timerString = MinutesTimerConverter.convert(timer);
                if(timer <= 0) advanceState();
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 0L, 20L);
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
        } else if (arenaState == ArenaState.COUNTDOWN && timer > ArenaSettings.arenaCutDownCountdownInSeconds && playerRatio < ArenaSettings.cutDownCountdownPlayerPercentageRequirement) {
            timer = -1;
            tickTask.cancel();
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
                tickTask.cancel();
            }
            case STARTING -> {
                this.arenaState = ArenaState.RUNNING;
                timer = (int) (ArenaSettings.arenaLengthInMinutes * 60);
                startCountdown();
            }
            case RUNNING -> {
                this.arenaState = ArenaState.ENDING;
                tickTask.cancel();
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
