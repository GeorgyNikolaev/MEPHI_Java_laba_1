package io;

import domain.*;
import java.io.PrintStream;
import java.util.List;

public class MissionPrinter {

    public static void print(Mission mission, PrintStream out) {
        out.println("=== Отчет о Миссии ===");
        out.println();
        out.println("ID миссии: " + safe(mission.getMissionId()));
        out.println("Дата: " + safe(mission.getDate()));
        out.println("Локация: " + safe(mission.getLocation()));
        out.println("Исход: " + safe(mission.getOutcome()));
        out.println("Суммарный урон: " + mission.getDamageCost());
        out.println();

        out.println("Проклятия:");
        Curse curse = mission.getCurse();
        if (curse != null) {
            out.println("  Имя: " + safe(curse.getName()));
            out.println("  Уровень угрозы: " + safe(curse.getThreatLevel()));
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Маги:");
        List<Sorcerer> sorcerers = mission.getSorcerers();
        if (sorcerers != null && !sorcerers.isEmpty()) {
            for (Sorcerer s : sorcerers) {
                out.println("  - " + safe(s.getName()) + (s.getRank() != null ? " (" + s.getRank() + ")" : ""));
            }
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Техники:");
        List<TechniqueUsage> techs = mission.getTechniques();
        if (techs != null && !techs.isEmpty()) {
            for (TechniqueUsage t : techs) {
                out.println("  - " + (t.getTechnique() != null ? t.getTechnique().getName() : "<unknown technique>"));
                if (t.getTechnique() != null) {
                    out.println("    Type: " + safe(t.getTechnique().getType()));
                }
                out.println("    Владелец: " + (t.getOwner() != null ? safe(t.getOwner().getName()) : "<unknown>"));
                out.println("    Урон: " + t.getDamage());
                out.println();
            }
        } else {
            out.println("  <none>");
        }

        if (mission.getComment() != null && !mission.getComment().isBlank()) {
            out.println("Заметки:");
            out.println(mission.getComment());
            out.println();
        }
    }

    private static String safe(Object o) {
        return o == null ? "<null>" : o.toString();
    }
}