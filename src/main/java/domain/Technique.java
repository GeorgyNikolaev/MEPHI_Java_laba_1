package domain;

import enums.TechniqueType;

public record Technique(String name, TechniqueType type) {

    @Override
    public String toString() {
        return "Technique{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}