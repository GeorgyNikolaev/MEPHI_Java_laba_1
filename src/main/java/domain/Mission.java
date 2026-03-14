package domain;

import enums.MissionOutcome;

import java.time.LocalDate;
import java.util.List;

public class Mission {

    private final String missionId;
    private final LocalDate date;
    private final String location;
    private final MissionOutcome outcome;
    private final long damageCost;
    private final Curse curse;
    private final List<Sorcerer> sorcerers;
    private final List<TechniqueUsage> techniques;
    private final String comment;

    public Mission(String missionId,
                   LocalDate date,
                   String location,
                   MissionOutcome outcome,
                   long damageCost,
                   Curse curse,
                   List<Sorcerer> sorcerers,
                   List<TechniqueUsage> techniques,
                   String comment) {
        this.missionId = missionId;
        this.date = date;
        this.location = location;
        this.outcome = outcome;
        this.damageCost = damageCost;
        this.curse = curse;
        this.sorcerers = sorcerers;
        this.techniques = techniques;
        this.comment = comment;
    }

    public String getMissionId() {
        return missionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public MissionOutcome getOutcome() {
        return outcome;
    }

    public long getDamageCost() {
        return damageCost;
    }

    public Curse getCurse() {
        return curse;
    }

    public List<Sorcerer> getSorcerers() {
        return sorcerers;
    }

    public List<TechniqueUsage> getTechniques() {
        return techniques;
    }

    public String getComment() {
        return comment;
    }

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