package domain;

import enums.MissionOutcome;

import java.time.LocalDate;
import java.util.List;

public record Mission(
        String missionId,
        LocalDate date,
        String location,
        MissionOutcome outcome,
        long damageCost,
        Curse curse,
        List<Sorcerer> sorcerers,
        List<TechniqueUsage> techniques,
        String comment) {

    @Override
    public String toString() {
        return "Mission{" +
                "missionId='" + missionId + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", outcome=" + outcome +
                ", damageCost=" + damageCost +
                ", curse=" + curse +
                ", sorcerers=" + sorcerers +
                ", techniques=" + techniques +
                ", comment='" + comment + '\'' +
                '}';
    }
}