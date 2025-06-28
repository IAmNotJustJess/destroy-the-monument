package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.teams.Team;
import IAmNotJustJess.destroyTheMonument.teams.TeamColour;
import IAmNotJustJess.destroyTheMonument.teams.TeamManager;
import IAmNotJustJess.destroyTheMonument.utility.MiniMessageSerializers;
import IAmNotJustJess.destroyTheMonument.utility.MinutesTimerConverter;
import IAmNotJustJess.destroyTheMonument.utility.RandomElementPicker;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ArenaInstance {
    private HashMap<TeamColour, ArrayList<Location>> monumentList;
    private HashMap<TeamColour, Integer> monumentRemainingCount;
    private HashMap<TeamColour, ArrayList<Location>> spawnLocations;
    private ArrayList<TeamColour> teamColours;
    private ArrayList<Player> playerList;
    private HashMap<TeamColour, ArrayList<Player>> playersInTeamsList;
    private ArrayList<Location> playerPlacedBlocksLocations;
    private TeamColour victor;
    private Location lobbyLocation;
    private int timer;
    private String timerString;
    private ArenaState arenaState;
    private BukkitTask tickTask;
    private String bossbarFormat;
    private BossBar bossbar;

    public void sendMessageGlobally(TextComponent textComponent) {
        for(Player player : playerList) {
            Audience audience = (Audience) player;
            audience.sendMessage(textComponent);
        }
    }
    public void sendMessageGlobally(String string) {
        List<String> stringListToSend = MiniMessageSerializers.deserializeMultilineToString(string);
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
        List<String> stringListToSend = MiniMessageSerializers.deserializeMultilineToString(string);
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
        Title titleMessage = Title.title(MiniMessageSerializers.deserializeToComponent(title), MiniMessageSerializers.deserializeToComponent(subtitle), Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
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
        Title titleMessage = Title.title(MiniMessageSerializers.deserializeToComponent(title), MiniMessageSerializers.deserializeToComponent(subtitle), Title.Times.times(Duration.ofMillis(fadeInMills), Duration.ofMillis(holdMills), Duration.ofMillis(fadeOutMills)));
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

        location.getWorld().spawnParticle(Particle.BLOCK, location, 25, 1, 1, 1, 0.25, location.getBlock().getBlockData());
        location.getWorld().spawnParticle(Particle.LAVA, location, 3, 0.1, 0.1, 0.1, 0.25, location.getBlock().getBlockData());
        location.getWorld().spawnParticle(Particle.POOF, location, 15, 0.1, 0.1, 0.1, 0.25, location.getBlock().getBlockData());
        location.getWorld().strikeLightningEffect(location);

        location.getBlock().setType(Material.AIR);

        Team breakerTeam = PlayerCharacterManager.getPlayerTeam(breaker);
        Team brokenTeam = TeamManager.list.get(brokenTeamColour);

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
            PlayerCharacterManager.getList().get(player).addShards(shards, message);
        }

        if(remainingMonuments == 0) {
            endArena(getOppositeTeam(brokenTeamColour));
            return;
        }

        for(Player player : playersInTeamsList.get(brokenTeamColour)) {
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.MASTER, 3, 1);
        }
    }

    public void sendExplanation() {

        List<String> messages = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-messages").split("<newline>"));
        List<String> titles = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-titles").split("<newline>"));
        List<String> subtitles = List.of(MessagesConfiguration.arenaMessagesConfiguration.getString("starting-tips-subtitles").split("<newline>"));

        for(Player player : playerList) {
            player.setWalkSpeed(0.0f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, PotionEffect.INFINITE_DURATION, 1));
        }

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
                            PlayerCharacterManager.getList().get(player).updatePlayerSpeed();
                            player.removePotionEffect(PotionEffectType.BLINDNESS);
                        }
                    }
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 100L * i);
        }
    }

    public void endArena(TeamColour winner) {
        for(Player player : playerList) {
            player.setGameMode(GameMode.SPECTATOR);
        }

        victor = winner;
        Team team1 = TeamManager.list.get(teamColours.getFirst());
        Team team2 = TeamManager.list.get(teamColours.get(1));

        String subtitle = MessagesConfiguration.arenaMessagesConfiguration.getString("end-screen-subtitle")
            .replace("<teamColour1>", team1.textColour)
            .replace("<remainingMonuments1>", Integer.toString(monumentRemainingCount.get(team1.teamColour)))
            .replace("<teamColour2>", team2.textColour)
            .replace("<remainingMonuments2>", Integer.toString(monumentRemainingCount.get(team1.teamColour)));

        subtitle = MiniMessageSerializers.deserializeToString(subtitle);

        if(winner == TeamColour.NONE) {
            String title = MessagesConfiguration.arenaMessagesConfiguration.getString("tie-title");
            title = MiniMessageSerializers.deserializeToString(title);
            sendTitleGlobally(title, subtitle, 500L, 5000L, 500L);
        }
        else {
            String titleVictory = MessagesConfiguration.arenaMessagesConfiguration.getString("victory-title");
            titleVictory = MiniMessageSerializers.deserializeToString(titleVictory);
            String titleDefeat = MessagesConfiguration.arenaMessagesConfiguration.getString("defeat-title");
            titleDefeat = MiniMessageSerializers.deserializeToString(titleDefeat);
            sendTitleToATeam(winner, titleVictory, subtitle, 500L, 5000L, 500L);
            sendTitleToATeam(getOppositeTeam(winner), titleDefeat, subtitle, 500L, 5000L, 500L);
        }

        tickTask.cancel();
    }

    public void startCountdown() {

        tickTask = new BukkitRunnable() {
            @Override
            public void run() {
                timer -= 1;
                timerString = MinutesTimerConverter.convert(timer);
                if(timer <= 0) advanceState();
                if(arenaState == ArenaState.RUNNING){
                    if(timer <= 0) endArena(TeamColour.NONE);
                    for(Player player : playerList) {
                        PlayerCharacterManager.getList().get(player).readThroughEffectList();
                    }
                }
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 0L, 20L);
    };

    public void addPlayerToArena(Player player) {
        playerList.add(player);
        checkPlayerCount();
        updateBossBar();
        ((Audience) player).showBossBar(bossbar);
    }

    public void removePlayerFromArena(Player player) {
        playerList.remove(player);
        if(PlayerCharacterManager.getList().containsKey(player)) {
            playersInTeamsList.remove(PlayerCharacterManager.getList().get(player).getTeam());
        }
        checkPlayerCount();
        updateBossBar();
        ((Audience) player).hideBossBar(bossbar);
    }

    private void updateBossBar() {
        if(Objects.isNull(bossbar)) {
            if (Objects.requireNonNull(getArenaState()) == ArenaState.LOBBY) {
                bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-lobby-format");
                TextComponent text = (TextComponent) MiniMessageSerializers.deserializeToComponent(bossbarFormat.replace("<players>", Integer.toString(playerList.size()))
                    .replace("<maxPlayers>", Integer.toString(ArenaSettings.maxPlayersPerTeam * 2)));
                bossbar = BossBar.bossBar(text, 1.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            }
        }
        else {
            switch(getArenaState()) {
                case LOBBY -> {
                    TextComponent text = (TextComponent) MiniMessageSerializers.deserializeToComponent(bossbarFormat
                        .replace("<players>", Integer.toString(playerList.size()))
                        .replace("<maxPlayers>", Integer.toString(ArenaSettings.maxPlayersPerTeam * 2)));
                    bossbar.name(text);
                    bossbar.progress(1.0f);
                }
                case COUNTDOWN -> {
                    TextComponent text = (TextComponent) MiniMessageSerializers.deserializeToComponent(bossbarFormat
                        .replace("<players>", Integer.toString(playerList.size()))
                        .replace("<maxPlayers>", Integer.toString(ArenaSettings.maxPlayersPerTeam * 2))
                        .replace("<seconds>", Integer.toString(timer)));
                    bossbar.name(text);
                    bossbar.progress((float) timer / ArenaSettings.arenaBeginCountdownInSeconds);
                }
                case RUNNING -> {
                    TextComponent text = (TextComponent) MiniMessageSerializers.deserializeToComponent(bossbarFormat
                        .replace("<teamColour1>", TeamManager.list.get(teamColours.getFirst()).textColour)
                        .replace("<teamColour2>", TeamManager.list.get(teamColours.get(1)).textColour)
                        .replace("<remainingMonuments1>", Integer.toString(monumentRemainingCount.get(teamColours.getFirst())))
                        .replace("<remainingMonuments2>", Integer.toString(monumentRemainingCount.get(teamColours.get(1))))
                        .replace("<timeRemaining>", timerString));
                    bossbar.name(text);
                    bossbar.progress((float) (timer / (ArenaSettings.arenaLengthInMinutes * 60)));
                }
                case ENDING -> {
                    if(victor == TeamColour.NONE) {
                        bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-ending-tie-format");
                    }
                    else {
                        bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-ending-format");
                    }
                    TextComponent text = (TextComponent) MiniMessageSerializers.deserializeToComponent(bossbarFormat
                        .replace("<teamName>", TeamManager.list.get(teamColours.getFirst()).name));
                    bossbar.name(text);
                    bossbar.progress(0.0f);
                }
            }
        }
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

    public void teleportPlayerToArena(Player player) {
        player.teleport(RandomElementPicker.getRandomElement(spawnLocations.get(PlayerCharacterManager.getList().get(player).getTeam())));
    }

    public void advanceState() {
        switch (arenaState) {
            case LOBBY -> {
                this.arenaState = ArenaState.COUNTDOWN;
                bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-countdown-format");
                timer = ArenaSettings.arenaBeginCountdownInSeconds;
                startCountdown();
            }
            case COUNTDOWN ->  {
                this.arenaState = ArenaState.STARTING;
                teleportPlayersToArena();
                World world = playerList.getFirst().getWorld();
                world.setGameRule(GameRule.DO_INSOMNIA, false);
                world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
                sendExplanation();
                tickTask.cancel();
            }
            case STARTING -> {
                this.arenaState = ArenaState.RUNNING;
                bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-running-format");
                timer = (int) (ArenaSettings.arenaLengthInMinutes * 60);
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

    public ArenaState getArenaState() {
        return arenaState;
    }

    public void setLobbyLocation(Location lobbyLocation) {
        this.lobbyLocation = lobbyLocation;
    }
}
