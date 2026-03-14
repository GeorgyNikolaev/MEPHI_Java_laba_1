package io;

import domain.*;
import java.io.PrintStream;
import java.util.List;

public class MissionPrinter {

    public static void print(Mission mission, PrintStream out) {
        out.println("=== Mission Report ===");
        out.println();
        out.println("Mission ID: " + safe(mission.getMissionId()));
        out.println("Date: " + safe(mission.getDate()));
        out.println("Location: " + safe(mission.getLocation()));
        out.println("Outcome: " + safe(mission.getOutcome()));
        out.println("Damage cost: " + mission.getDamageCost());
        out.println();

        out.println("Curse:");
        Curse curse = mission.getCurse();
        if (curse != null) {
            out.println("  Name: " + safe(curse.getName()));
            out.println("  Threat level: " + safe(curse.getThreatLevel()));
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Sorcerers:");
        List<Sorcerer> sorcerers = mission.getSorcerers();
        if (sorcerers != null && !sorcerers.isEmpty()) {
            for (Sorcerer s : sorcerers) {
                out.println("  - " + safe(s.getName()) + (s.getRank() != null ? " (" + s.getRank() + ")" : ""));
            }
        } else {
            out.println("  <none>");
        }
        out.println();

        out.println("Techniques:");
        List<TechniqueUsage> techs = mission.getTechniques();
        if (techs != null && !techs.isEmpty()) {
            for (TechniqueUsage t : techs) {
                out.println("  - " + (t.getTechnique() != null ? t.getTechnique().getName() : "<unknown technique>"));
                if (t.getTechnique() != null) {
                    out.println("    Type: " + safe(t.getTechnique().getType()));
                }
                out.println("    Owner: " + (t.getOwner() != null ? safe(t.getOwner().getName()) : "<unknown>"));
                out.println("    Damage: " + t.getDamage());
                out.println();
            }
        } else {
            out.println("  <none>");
        }

        if (mission.getComment() != null && !mission.getComment().isBlank()) {
            out.println("Note:");
            out.println(mission.getComment());
            out.println();
        }
    }

    private static String safe(Object o) {
        return o == null ? "<null>" : o.toString();
    }
}