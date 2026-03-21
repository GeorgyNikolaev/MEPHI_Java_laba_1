package domain;

public record TechniqueUsage(Technique technique, Sorcerer owner, long damage) {

    @Override
    public String toString() {
        return "TechniqueUsage{" +
                "technique=" + technique +
                ", owner=" + (owner != null ? owner.name() : "null") +
                ", damage=" + damage +
                '}';
    }
}