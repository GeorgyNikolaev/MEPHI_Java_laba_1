package domain;

import enums.SorcererRank;

public record Sorcerer(String name, SorcererRank rank) {

    @Override
    public String toString() {
        return "Sorcerer{" +
                "name='" + name + '\'' +
                ", rank=" + rank +
                '}';
    }
}