package IAmNotJustJess.destroyTheMonument.player.classes.effects;

import org.bukkit.potion.PotionEffect;

public class Effect {

    public EffectType effectType;
    public EffectApplicationType effectApplicationType;
    public double strength;
    public double range;
    public int tickEveryServerTicks;
    public int longevity;
    public PotionEffect potionEffect;
    public double baseStrength;
    public double baseLongevity;
    public double baseStrengthBeforeStacks;
    public double baseLongevityBeforeStacks;
    public long delay;

    Effect(EffectType effectType, EffectApplicationType effectApplicationType, double strength, double range, int tickEveryServerTicks, int longevity, long delay) {
        this.effectType = effectType;
        this.effectApplicationType = effectApplicationType;
        this.longevity = longevity;
        this.baseLongevity = longevity;
        this.baseLongevityBeforeStacks = longevity;
        this.strength = strength;
        this.baseStrength = strength;
        this.baseStrengthBeforeStacks = strength;
        this.range = range;
        this.tickEveryServerTicks = tickEveryServerTicks;
        this.delay = delay;
        this.potionEffect = null;
    }

    Effect(EffectType effectType, EffectApplicationType effectApplicationType, double strength, double range, int tickEveryServerTicks, int longevity, long delay, PotionEffect potionEffect) {
        this.effectType = effectType;
        this.effectApplicationType = effectApplicationType;
        this.longevity = longevity;
        this.baseLongevity = longevity;
        this.baseLongevityBeforeStacks = longevity;
        this.strength = strength;
        this.baseStrength = strength;
        this.baseStrengthBeforeStacks = strength;
        this.range = range;
        this.tickEveryServerTicks = tickEveryServerTicks;
        this.delay = delay;
        this.potionEffect = potionEffect;
    }
}
