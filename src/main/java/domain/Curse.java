package domain;

import enums.ThreatLevel;

public record Curse(String name, ThreatLevel threatLevel) {

    @Override
    public String toString() {
        return "Curse{" +
                "name='" + name + '\'' +
                ", threatLevel=" + threatLevel +
                '}';
    }
}