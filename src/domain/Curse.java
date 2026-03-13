package domain;

import enums.ThreatLevel;
import java.util.Objects;

public class Curse {

    private final String name;
    private final ThreatLevel threatLevel;

    public Curse(String name, ThreatLevel threatLevel) {
        this.name = name;
        this.threatLevel = threatLevel;
    }

    public String getName() {
        return name;
    }

    public ThreatLevel getThreatLevel() {
        return threatLevel;
    }

    @Override
    public String toString() {
        return "Curse{" +
                "name='" + name + '\'' +
                ", threatLevel=" + threatLevel +
                '}';
    }
}