package IAmNotJustJess.destroyTheMonument.classes.skills;

public class Effect {

    public EffectType effectType;
    public EffectApplicationType effectApplicationType;
    public double longevity;
    public double strength;


    Effect(EffectType effectType, EffectApplicationType effectApplicationType, double longevity, double strength) {
        this.effectType = effectType;
        this.effectApplicationType = effectApplicationType;
        this.longevity = longevity;
        this.strength = strength;
    }
}
