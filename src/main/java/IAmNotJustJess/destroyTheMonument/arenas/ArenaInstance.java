package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.DestroyTheMonument;
import IAmNotJustJess.destroyTheMonument.configuration.MessagesConfiguration;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacter;
import IAmNotJustJess.destroyTheMonument.player.PlayerCharacterManager;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClass;
import IAmNotJustJess.destroyTheMonument.player.classes.PlayerClassManager;
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
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;

public class ArenaInstance {
    private String arenaName;
    private ArrayList<TeamColour> teamColours;
    private HashMap<TeamColour, ArrayList<Location>> monumentList;
    private HashMap<TeamColour, Integer> monumentRemainingCount;
    private HashMap<TeamColour, ArrayList<Location>> spawnLocations;
    private ArrayList<Location> shopLocations;
    private HashSet<Player> playerList;
    private HashMap<TeamColour, HashSet<Player>> playersInTeamsList;
    private ArrayList<Location> playerPlacedBlocksLocations;
    private HashMap<Location, Material> playerDestroyedBlocksLocations;
    private HashMap<Location, BlockData> playerDestroyedBlocksData;
    private TeamColour victor;
    private Location lobbyLocation;
    private int timer;
    private int shardsPerMinute;
    private String timerString;
    private ArenaState arenaState;
    private BukkitTask tickTask;
    private String bossbarFormat;
    private BossBar bossbar;

    public boolean checkArenaAvailabilityState() {
        boolean arenaAvailability = true;

        if(teamColours.get(0) == TeamColour.NONE) {
            arenaAvailability = false;
        }
        if(teamColours.get(1) == TeamColour.NONE) {
            arenaAvailability = false;
        }
        if(monumentList.get(teamColours.get(0)).isEmpty()) {
            arenaAvailability = false;
        }
        if(monumentList.get(teamColours.get(1)).isEmpty()) {
            arenaAvailability = false;
        }
        if(spawnLocations.get(teamColours.get(0)).isEmpty()) {
            arenaAvailability = false;
        }
        if(spawnLocations.get(teamColours.get(1)).isEmpty()) {
            arenaAvailability = false;
        }
        if(shopLocations.isEmpty()) {
            arenaAvailability = false;
        }
        if(lobbyLocation == null) {
            arenaAvailability = false;
        }

        return arenaAvailability;
    }

    public boolean setFirstTeam(TeamColour teamColour) {
        if(teamColour == teamColours.get(1)) {
            return false;
        }
        if(teamColours.getFirst() != TeamColour.NONE) {
            monumentList.remove(teamColours.getFirst());
            spawnLocations.remove(teamColours.getFirst());
        }
        teamColours.set(0, teamColour);
        monumentList.put(teamColour, new ArrayList<>());
        spawnLocations.put(teamColour, new ArrayList<>());
        return true;
    }

    public boolean setSecondTeam(TeamColour teamColour) {
        if(teamColour == teamColours.getFirst()) {
            return false;
        }
        if(teamColours.get(1) != TeamColour.NONE) {
            monumentList.remove(teamColours.getFirst());
            spawnLocations.remove(teamColours.getFirst());
        }
        teamColours.set(1, teamColour);
        monumentList.put(teamColour, new ArrayList<>());
        spawnLocations.put(teamColour, new ArrayList<>());
        return true;
    }

    public TeamColour getFirstTeam() {
        return teamColours.getFirst();
    }
    public TeamColour getSecondTeam() {
        return teamColours.get(1);
    }

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
        location.getWorld().spawnParticle(Particle.LAVA, location, 3, 0.1, 0.1, 0.1, 0.25);
        location.getWorld().spawnParticle(Particle.POOF, location, 15, 0.1, 0.1, 0.1, 0.25);
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

    public void checkForArenaEnd() {
        for(TeamColour teamColour : playersInTeamsList.keySet()) {
            if(playersInTeamsList.get(teamColour).isEmpty()) {
                endArena(getOppositeTeam(teamColour));
                return;
            }
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
                if(arenaState == ArenaState.RUNNING){
                    if(timer <= 0) {
                        endArena(TeamColour.NONE);
                        for(Player player : playerList) {
                            PlayerCharacterManager.getList().get(player).readThroughEffectList();
                        }
                        advanceState();
                        return;
                    }
                    if(timer % 60 * ArenaSettings.boostShardsEveryTimeInMinutes == 0) {
                        shardsPerMinute += ArenaSettings.boostShardsBy;
                    }
                    if(timer % 60 == 0) {
                        for(Player player : playerList) {
                            PlayerCharacterManager.getList().get(player).addShards(shardsPerMinute, MessagesConfiguration.playerMessagesConfiguration.getString("passive-shards"));
                        }
                    }
                }
                if(timer <= 0) advanceState();
            }
        }.runTaskTimerAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 0L, 20L);
    };

    public void addPlayerToArena(Player player) {
        if(arenaState != ArenaState.LOBBY) return;
        playerList.add(player);
        PlayerCharacterManager.getList().put(player, new PlayerCharacter(player, (PlayerClass) RandomElementPicker.getRandomElement(PlayerClassManager.getList()).clone(), TeamColour.NONE, 1.0f));
        checkPlayerCount();
        updateBossBar();
        ((Audience) player).showBossBar(bossbar);
    }

    public void removePlayerFromArena(Player player) {
        if(!playerList.contains(player)) return;
        playerList.remove(player);
        playersInTeamsList.get(PlayerCharacterManager.getList().get(player).getTeam()).remove(player);
        checkPlayerCount();
        updateBossBar();
        ((Audience) player).hideBossBar(bossbar);
        switch(arenaState) {
            case STARTING, RUNNING -> {
                checkForArenaEnd();
            }
        }
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

    public void resetArena() {
        for (Player player : playerList) {
            removePlayerFromArena(player);
        }

        for(TeamColour teamColour : monumentList.keySet()) {
            for(Location location : monumentList.get(teamColour)) {
                location.getBlock().setType(Material.CRYING_OBSIDIAN);
            }
        }

        int amountOfBlocksPlaced = playerPlacedBlocksLocations.size();
        int amountOfBlocksBroken = playerDestroyedBlocksLocations.size();

        int maxBlockRemovalPerTick = 50;
        int loopTimes = amountOfBlocksPlaced / maxBlockRemovalPerTick;

        int i;
        for(i = 0; i < loopTimes; i++) {
            int finalAmountOfBlocksPlaced = amountOfBlocksPlaced;
            new BukkitRunnable() {
                @Override
                public void run() {
                    int value = finalAmountOfBlocksPlaced - maxBlockRemovalPerTick;
                    for(int j = finalAmountOfBlocksPlaced - 1; j > value; j--) {
                        playerPlacedBlocksLocations.get(j).getBlock().setType(Material.AIR);
                        playerPlacedBlocksLocations.removeLast();
                    }
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L * i);
            amountOfBlocksPlaced -= maxBlockRemovalPerTick;
        }
        int finalAmountOfBlocksPlaced = amountOfBlocksPlaced;
        new BukkitRunnable() {
            @Override
            public void run() {
                for(int j = finalAmountOfBlocksPlaced - 1; j >= 0; j--) {
                    playerPlacedBlocksLocations.get(j).getBlock().setType(Material.AIR);
                    playerPlacedBlocksLocations.removeLast();
                }
            }
        }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L * i);

        loopTimes = amountOfBlocksBroken / maxBlockRemovalPerTick;
        for(i = 0; i < loopTimes; i++) {
            int finalAmountOfBlocksBroken = amountOfBlocksBroken;
            new BukkitRunnable() {
                @Override
                public void run() {
                    int value = finalAmountOfBlocksBroken - maxBlockRemovalPerTick;
                    int j = finalAmountOfBlocksBroken - 1;
                    for(Location location : playerDestroyedBlocksLocations.keySet()) {
                        if (j <= value) return;
                        location.getBlock().setType(playerDestroyedBlocksLocations.get(location));
                        location.getBlock().setBlockData(playerDestroyedBlocksData.get(location));
                        playerDestroyedBlocksLocations.remove(location);
                        playerDestroyedBlocksData.remove(location);
                        j--;
                    }
                }
            }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L * i + 10L);
            amountOfBlocksBroken -= maxBlockRemovalPerTick;
        }
        int finalAmountOfBlocksBroken = amountOfBlocksBroken;
        new BukkitRunnable() {
            @Override
            public void run() {
                int j = finalAmountOfBlocksBroken - 1;
                for(Location location : playerDestroyedBlocksLocations.keySet()) {
                    if (j <= -1) return;
                    location.getBlock().setType(playerDestroyedBlocksLocations.get(location));
                    location.getBlock().setBlockData(playerDestroyedBlocksData.get(location));
                    playerDestroyedBlocksLocations.remove(location);
                    playerDestroyedBlocksData.remove(location);
                    j--;
                }
                advanceState();
            }
        }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(DestroyTheMonument.class), 20L * i + 10L);
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
                World world = playerList.toArray(new Player[0])[0].getWorld();
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
                this.shardsPerMinute = ArenaSettings.shardsPerMinute;
                bossbarFormat = MessagesConfiguration.arenaMessagesConfiguration.getString("bossbar-running-format");
                timer = (int) (ArenaSettings.arenaLengthInMinutes * 60);
            }
            case RUNNING -> {
                this.arenaState = ArenaState.ENDING;
                tickTask.cancel();
            }
            case ENDING -> {
                this.arenaState = ArenaState.CLEARING;
                resetArena();
            }
            case CLEARING -> {
                this.arenaState = ArenaState.LOBBY;
            }
        }
    }

    public ArenaInstance(String arenaName) {
        this.arenaName = arenaName;
        this.playerList = new HashSet<>();
        this.teamColours = new ArrayList<>() {{
            add(TeamColour.NONE);
            add(TeamColour.NONE);
        }};
        this.shopLocations = new ArrayList<>();
        this.monumentRemainingCount = new HashMap<>();
        this.monumentList = new HashMap<>();
        this.playerPlacedBlocksLocations = new ArrayList<>();
        this.playersInTeamsList = new HashMap<>();
        this.spawnLocations = new HashMap<>();
        this.playerPlacedBlocksLocations = new ArrayList<>();
        this.playerDestroyedBlocksData = new HashMap<>();
        this.playerDestroyedBlocksLocations = new HashMap<>();
        this.arenaState = ArenaState.LOBBY;
    }
    public HashMap<TeamColour, Integer> getMonumentRemainingCount() {
        return monumentRemainingCount;
    }

    public HashMap<TeamColour, ArrayList<Location>> getMonumentList() {
        return monumentList;
    }

    public ArrayList<TeamColour> getTeamColours() {
        return teamColours;
    }

    public ArrayList<Location> getShopLocations() {
        return shopLocations;
    }

    public HashMap<TeamColour, ArrayList<Location>> getSpawnLocations() {
        return spawnLocations;
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

    public HashMap<Location, Material> getPlayerDestroyedBlocksLocations() {
        return playerDestroyedBlocksLocations;
    }

    public HashMap<Location, BlockData> getPlayerDestroyedBlocksData() {
        return playerDestroyedBlocksData;
    }

    public String getArenaName() {
        return arenaName;
    }
}
