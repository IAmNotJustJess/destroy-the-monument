package IAmNotJustJess.destroyTheMonument.arena;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterList;
import IAmNotJustJess.destroyTheMonument.team.Team;
import IAmNotJustJess.destroyTheMonument.team.TeamColour;
import IAmNotJustJess.destroyTheMonument.team.TeamList;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageParser;
import IAmNotJustJess.destroyTheMonument.utility.MinutesTimerConverter;
import IAmNotJustJess.destroyTheMonument.utility.RandomElementPicker;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaInstance {
    private HashMap<TeamColour, ArrayList<Location>> monumentList;
    private HashMap<TeamColour, Integer> monumentRemainingCount;
    private HashMap<TeamColour, ArrayList<Location>> spawnLocations;
    private ArrayList<TeamColour> teamColours;
    private ArrayList<Player> playerList;
    private HashMap<TeamColour, ArrayList<Player>> playersInTeamsList;
    private ArrayList<Location> playerPlacedBlocksLocations;
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
    public void sendMessageGlobally(String string) {
        List<String> stringListToSend = MiniMessageParser.deserializeMultilineToString(string);
        for(Player player : playerList) {
            for(String stringInList : stringListToSend) {
                player.sendMessage(stringInList);
            }
        }
    }

    public void sendMessageToATeam(TeamColour teamColour, TextComponent textComponent) {
        for(Player player : playersInTeamsList.get(teamColour)) {
            Audience audience = (Audience) player;
            audience.sendMessage(textComponent);
        }
    }

    public void sendMessageToATeam(TeamColour teamColour, String string) {
        List<String> stringListToSend = MiniMessageParser.deserializeMultilineToString(string);
        for(Player player : playersInTeamsList.get(teamColour)) {
            for(String stringInList : stringListToSend) {
                player.sendMessage(stringInList);
            }
        }
    }

    public void sendTitleGlobally(TextComponent title, TextComponent subtitle, Long fadeInMills, Long holdMills, Long fadeOutMills) {
        Title titleMessage = Title.title(title, subtitle, Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
        for(Player player : playerList) {

            Audience audience = (Audience) player;
            audience.showTitle(titleMessage);
        }
    }

    public void sendTitleGlobally(String title, String subtitle, Long fadeInMills, Long holdMills, Long fadeOutMills) {
        Title titleMessage = Title.title(MiniMessageParser.deserializeToComponent(title), MiniMessageParser.deserializeToComponent(subtitle), Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
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

    public void sendTitleToATeam(TeamColour teamColour, String title, String subtitle, Long fadeInMills, Long holdMills, Long fadeOutMills) {
        Title titleMessage = Title.title(MiniMessageParser.deserializeToComponent(title), MiniMessageParser.deserializeToComponent(subtitle), Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
        for(Player player : playersInTeamsList.get(teamColour)) {

            Audience audience = (Audience) player;
            audience.showTitle(titleMessage);
        }
    }

    public TeamColour getOppositeTeam(TeamColour team) {
        for(TeamColour teamColour : teamColours) {
            if(teamColour != team) {
                return teamColour;
            }
        }
        return null;
    }

    public void breakMonument(Player breaker, TeamColour brokenTeamColour, Location location) {

        int remainingMonuments = monumentRemainingCount.get(brokenTeamColour) - 1;
        monumentRemainingCount.put(brokenTeamColour, remainingMonuments);
        if(remainingMonuments == 0) {
            endArena(getOppositeTeam(brokenTeamColour));
            return;
        }

        location.getWorld().spawnParticle(Particle.BLOCK, location, 25, 1, 1, 1, 0.25, location.getBlock().getBlockData());
        location.getWorld().spawnParticle(Particle.LAVA, location, 3, 0.1, 0.1, 0.1, 0.25, location.getBlock().getBlockData());
        location.getWorld().spawnParticle(Particle.POOF, location, 15, 0.1, 0.1, 0.1, 0.25, location.getBlock().getBlockData());
        location.getWorld().strikeLightningEffect(location);

        location.getBlock().setType(Material.AIR);

        Team breakerTeam = PlayerCharacterList.getPlayerTeam(breaker);
        Team brokenTeam = TeamList.list.get(brokenTeamColour);

        String titleBreakerTeam = MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-enemy-monument-title-to-friendlies");
        String subtitleBreakerTeam = MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-enemy-monument-subtitle-to-friendlies");
        String titleBrokenTeam = MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-enemy-monument-title-to-enemy");
        String subtitleBrokenTeam = MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-enemy-monument-subtitle-to-enemy");

        String breakingMessage = MessagesConfiguration.arenaMessagesConfiguration.getString("breaking-enemy-monument-message")
                .replace("<teamColour>", breakerTeam.textColour)
                .replace("<player>", breaker.getName())
                .replace("<enemyTeamColour>", brokenTeam.textColour)
                .replace("<enemyTeam>", breakerTeam.name)
                .replace("<remainingMonuments>", String.valueOf(remainingMonuments))
                .replace("<x>", String.valueOf(location.getBlockX()))
                .replace("<y>", String.valueOf(location.getBlockY()))
                .replace("<z>", String.valueOf(location.getBlockZ()));

        sendTitleToATeam(breakerTeam.teamColour, titleBreakerTeam, subtitleBreakerTeam, 500L, 4000L, 500L);
        sendTitleToATeam(brokenTeamColour, titleBrokenTeam, subtitleBrokenTeam, 500L, 4000L, 500L);
        sendMessageGlobally(breakingMessage);

        for(Player player : playersInTeamsList.get(breakerTeam.teamColour)) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 3, 1);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1, 1);
            int shards = ArenaSettings.totalMonumentDestructionShardPool / monumentList.get(breakerTeam.teamColour).size();
            String message = MessagesConfiguration.playerMessagesConfiguration.getString("monument-broken-shards");
            PlayerCharacterList.getList().get(player).addShards(shards, message);
        }

        for(Player player : playersInTeamsList.get(brokenTeamColour)) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.MASTER, 3, 1);
        }
    }

    public void sendExplanation() {

        List<String> messages = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-messages").split("<newline>"));
        List<String> titles = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-titles").split("<newline>"));
        List<String> subtitles = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-subtitles").split("<newline>"));

        for(int i = 0; i < messages.size(); i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendTitleGlobally(titles.get(finalI), subtitles.get(finalI), 0L, 5050L, 0L);
                    sendMessageGlobally(messages.get(finalI));
                    if(finalI == messages.size() - 1) {
                        startCountdown();
                        for(Player player : playerList) {
                            player.setGameMode(GameMode.SURVIVAL);
                        }
                    }
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 100L * i);
        }
    }

    public void endArena(TeamColour teamColour) {
        for(Player player : playerList) {
            player.setGameMode(GameMode.SPECTATOR);
        }

        TeamColour winner = TeamColour.NONE;
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
        }
        else if (arenaState == ArenaState.COUNTDOWN && timer > ArenaSettings.arenaCutDownCountdownInSeconds && playerRatio >= ArenaSettings.cutDownCountdownPlayerPercentageRequirement) {
            timer = ArenaSettings.arenaCutDownCountdownInSeconds;
        }
        else if (arenaState == ArenaState.COUNTDOWN && timer > ArenaSettings.arenaCutDownCountdownInSeconds && playerRatio < ArenaSettings.cutDownCountdownPlayerPercentageRequirement) {
            timer = -1;
            tickTask.cancel();
        }
    }

    private void teleportPlayersToArena() {
        for (TeamColour teamColour : teamColours) {
            for (Player player : playersInTeamsList.get(teamColour)) {
                player.teleport(RandomElementPicker.getRandomElement(spawnLocations.get(teamColour)));
            }
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
                teleportPlayersToArena();
                World world = playerList.getFirst().getWorld();
                world.setGameRule(GameRule.DO_INSOMNIA, false);
                world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                sendExplanation();
                // startCountdown();
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
        this.monumentRemainingCount = new HashMap<>();
        this.monumentList = new HashMap<>();
        this.playerPlacedBlocksLocations = new ArrayList<>();
        this.playersInTeamsList = new HashMap<>();
        this.spawnLocations = new HashMap<>();
        this.arenaState = ArenaState.LOBBY;
    }
    public HashMap<TeamColour, Integer> getMonumentRemainingCount() {
        return monumentRemainingCount;
    }

    public HashMap<TeamColour, ArrayList<Location>> getMonumentList() {
        return monumentList;
    }

    public ArrayList<Location> getPlayerPlacedBlocksLocations() {
        return playerPlacedBlocksLocations;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }
}
