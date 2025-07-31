package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;
import IAmNotJustJess.destroyTheMonument.utility.QuickSendingMethods;

public class ArenaSettings {
    public static int maxPlayersPerTeam;
    public static int arenaBeginCountdownInSeconds;
    public static int arenaStartCountdownPlayerRequirement;
    public static int arenaCutCountdownToInSeconds;
    public static int arenaCutCountdownPlayerRequirement;
    public static double arenaLengthInMinutes;
    public static int shardsAwardedPerKill;
    public static int shardsAwardedPerAssist;
    public static int totalMonumentDestructionShardPool;
    public static int shardsAwardedPerMinutePlayed;
    public static int shardsAwardPerMinuteBoost;
    public static int shardsBoostEveryMinutes;
    public static int playerRespawnInterval;

    public static void loadSettings() {
        maxPlayersPerTeam = MainConfiguration.globalGameRulesConfiguration.getInt("rules.max-players-per-team");
        arenaBeginCountdownInSeconds = MainConfiguration.globalGameRulesConfiguration.getInt("rules.arena-begin-countdown-in-seconds");
        arenaStartCountdownPlayerRequirement = (int) Math.floor(MainConfiguration.globalGameRulesConfiguration.getDouble("rules.arena-start-countdown-percentage-requirement"));
        arenaCutCountdownToInSeconds = MainConfiguration.globalGameRulesConfiguration.getInt("rules.arena-cut-countdown-to-in-seconds");
        arenaCutCountdownPlayerRequirement = (int) Math.floor(MainConfiguration.globalGameRulesConfiguration.getDouble("rules.arena-cut-countdown-percentage-requirement"));
        arenaLengthInMinutes = MainConfiguration.globalGameRulesConfiguration.getDouble("rules.arena-length-in-minutes");
        shardsAwardedPerKill = MainConfiguration.globalGameRulesConfiguration.getInt("rules.shards-awarded-per-kill");
        shardsAwardedPerAssist = MainConfiguration.globalGameRulesConfiguration.getInt("rules.shards-awarded-per-assist");
        totalMonumentDestructionShardPool = MainConfiguration.globalGameRulesConfiguration.getInt("rules.total-monument-destruction-shard-pool");
        shardsAwardedPerMinutePlayed = MainConfiguration.globalGameRulesConfiguration.getInt("rules.shards-awarded-per-minute-played");
        shardsAwardPerMinuteBoost = MainConfiguration.globalGameRulesConfiguration.getInt("rules.shards-award-per-minute-boost");
        shardsBoostEveryMinutes = MainConfiguration.globalGameRulesConfiguration.getInt("rules.shards-boost-every-minutes");
        playerRespawnInterval = MainConfiguration.globalGameRulesConfiguration.getInt("rules.player-respawn-interval");
        QuickSendingMethods.sendToConsole(
            "send-load-messages",
            "<#14db4c>Successfully loaded the <#ffffff>arena settings<#14db4c>!"
        );
    }
}