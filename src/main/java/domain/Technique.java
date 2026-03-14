package domain;

import enums.TechniqueType;

public class Technique {

    private final String name;
    private final TechniqueType type;

    public Technique(String name, TechniqueType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public TechniqueType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Technique{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}