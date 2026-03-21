package io;

import domain.*;
import java.io.PrintStream;
import java.util.List;

public class MissionPrinter {

    public static void print(Mission mission, PrintStream out) {
        out.println("=== Отчет о Миссии ===");
        out.println();
        out.println("ID миссии: " + safe(mission.missionId()));
        out.println("Дата: " + safe(mission.date()));
        out.println("Локация: " + safe(mission.location()));
        out.println("Исход: " + safe(mission.outcome()));
        out.println("Суммарный урон: " + mission.damageCost());
        out.println();

        out.println("Проклятия:");
        Curse curse = mission.curse();
        if (curse != null) {
            out.println("  Имя: " + safe(curse.name()));
            out.println("  Уровень угрозы: " + safe(curse.threatLevel()));
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Маги:");
        List<Sorcerer> sorcerers = mission.sorcerers();
        if (sorcerers != null && !sorcerers.isEmpty()) {
            for (Sorcerer s : sorcerers) {
                out.println("  - " + safe(s.name()) + (s.rank() != null ? " (" + s.rank() + ")" : ""));
            }
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Техники:");
        List<TechniqueUsage> techs = mission.techniques();
        if (techs != null && !techs.isEmpty()) {
            for (TechniqueUsage t : techs) {
                out.println("  - " + (t.technique() != null ? t.technique().name() : "<unknown technique>"));
                if (t.technique() != null) {
                    out.println("    Type: " + safe(t.technique().type()));
                }
                out.println("    Владелец: " + (t.owner() != null ? safe(t.owner().name()) : "<unknown>"));
                out.println("    Урон: " + t.damage());
                out.println();
            }
        } else {
            out.println("  <none>");
        }

        if (mission.comment() != null && !mission.comment().isBlank()) {
            out.println("Заметки:");
            out.println(mission.comment());
            out.println();
        }
    }

    private static String safe(Object o) {
        return o == null ? "<null>" : o.toString();
    }
}