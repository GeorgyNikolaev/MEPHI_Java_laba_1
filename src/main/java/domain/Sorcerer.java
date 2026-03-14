package domain;

import enums.SorcererRank;

public class Sorcerer {

    private final String name;
    private final SorcererRank rank;

    public Sorcerer(String name, SorcererRank rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public SorcererRank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "Sorcerer{" +
                "name='" + name + '\'' +
                ", rank=" + rank +
                '}';
    }
}