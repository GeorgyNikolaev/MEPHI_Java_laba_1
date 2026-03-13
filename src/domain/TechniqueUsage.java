package domain;

import java.util.Objects;

public class TechniqueUsage {

    private final Technique technique;
    private final Sorcerer owner;
    private final long damage;

    public TechniqueUsage(Technique technique, Sorcerer owner, long damage) {
        this.technique = technique;
        this.owner = owner;
        this.damage = damage;
    }

    public Technique getTechnique() {
        return technique;
    }

    public Sorcerer getOwner() {
        return owner;
    }

    public long getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return "TechniqueUsage{" +
                "technique=" + technique +
                ", owner=" + (owner != null ? owner.getName() : "null") +
                ", damage=" + damage +
                '}';
    }
}