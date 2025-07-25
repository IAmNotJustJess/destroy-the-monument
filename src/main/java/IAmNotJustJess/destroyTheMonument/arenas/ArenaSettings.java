package IAmNotJustJess.destroyTheMonument.arenas;

import IAmNotJustJess.destroyTheMonument.configuration.MainConfiguration;

public class ArenaSettings {
    public static int maxPlayersPerTeam = 16;
    public static int arenaBeginCountdownInSeconds = 60;
    public static double startCountdownPlayerPercentageRequirement = 0.125;
    public static int arenaCutDownCountdownInSeconds = 10;
    public static double cutDownCountdownPlayerPercentageRequirement = 0.5;
    public static double arenaLengthInMinutes = 60;
    public static int shardsPerKill = 50;
    public static int shardsPerAssist = 10;
    public static int totalMonumentDestructionShardPool = 1500;
    public static int shardsPerMinute = 2;
    public static int boostShardsBy = 2;
    public static int boostShardsEveryTimeInMinutes = 10;

    public static void loadSettings() {
        maxPlayersPerTeam = MainConfiguration.globalGameRulesConfiguration.getInt("max-players-per-team");
        arenaBeginCountdownInSeconds = MainConfiguration.globalGameRulesConfiguration.getInt("arena-begin-countdown");
        startCountdownPlayerPercentageRequirement = MainConfiguration.globalGameRulesConfiguration.getInt("arena-start-countdown-percentage-requirement");
    }
}