package parser;

import domain.*;
import enums.*;
import exception.MissionParsingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

/**
 * Simple key-value style text parser.
 * Expected lines:
 * key: value
 * keys may be:
 *  missionId, date, location, outcome, damageCost, note
 *  curse.name, curse.threatLevel
 *  sorcerer[0].name, sorcerer[0].rank
 *  technique[0].name, technique[0].type, technique[0].owner, technique[0].damage
 */
public class TextMissionParser implements MissionParser {

    @Override
    public Mission parse(Path file) throws MissionParsingException {
        List<String> lines;
        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new MissionParsingException("Failed to read text file: " + e.getMessage(), e);
        }

        String missionId = null;
        LocalDate date = null;
        String location = null;
        MissionOutcome outcome = null;
        long damageCost = 0L;
        String comment = null;

        String curseName = null;
        ThreatLevel curseThreat = null;

        // temporary storage for indexed entities
        Map<Integer, Map<String, String>> sorcererMap = new HashMap<>();
        Map<Integer, Map<String, String>> techniqueMap = new HashMap<>();

        for (String rawLine : lines) {
            if (rawLine == null) continue;
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String key = line.substring(0, colon).trim();
            String value = line.substring(colon + 1).trim();

            // simple fields
            switch (key) {
                case "missionId":
                    missionId = value;
                    continue;
                case "date":
                    date = LocalDate.parse(value);
                    continue;
                case "location":
                    location = value;
                    continue;
                case "outcome":
                    outcome = parseOutcome(value);
                    continue;
                case "damageCost":
                    damageCost = parseLongSafe(value, 0L);
                    continue;
                case "note":
                case "comment":
                    comment = value;
                    continue;
            }

            if (key.startsWith("curse.")) {
                String sub = key.substring("curse.".length());
                if ("name".equals(sub)) curseName = value;
                else if ("threatLevel".equals(sub)) curseThreat = parseThreatLevel(value);
                continue;
            }

            if (key.startsWith("sorcerer[")) {
                // sorcerer[0].name
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                if (idxStart <= 0 || idxEnd <= idxStart) continue;
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd));
                String rest = key.substring(idxEnd + 1);
                if (rest.startsWith(".")) rest = rest.substring(1);
                sorcererMap.computeIfAbsent(idx, k -> new HashMap<>()).put(rest, value);
                continue;
            }

            if (key.startsWith("technique[")) {
                int idxStart = key.indexOf('[') + 1;
                int idxEnd = key.indexOf(']');
                if (idxStart <= 0 || idxEnd <= idxStart) continue;
                int idx = Integer.parseInt(key.substring(idxStart, idxEnd));
                String rest = key.substring(idxEnd + 1);
                if (rest.startsWith(".")) rest = rest.substring(1);
                techniqueMap.computeIfAbsent(idx, k -> new HashMap<>()).put(rest, value);
                continue;
            }

            // fallback: unrecognized key ignored
        }

        // build curse
        Curse curse = null;
        if (curseName != null || curseThreat != null) {
            curse = new Curse(curseName, curseThreat);
        }

        // build sorcerers list
        List<Sorcerer> sorcerers = new ArrayList<>();
        List<Integer> sIndices = new ArrayList<>(sorcererMap.keySet());
        Collections.sort(sIndices);
        for (Integer idx : sIndices) {
            Map<String, String> fields = sorcererMap.get(idx);
            if (fields == null) continue;
            String name = fields.get("name");
            SorcererRank rank = parseSorcererRank(fields.get("rank"));
            if (name != null) {
                sorcerers.add(new Sorcerer(name, rank));
            }
        }

        // build techniques
        List<TechniqueUsage> techniques = new ArrayList<>();
        List<Integer> tIndices = new ArrayList<>(techniqueMap.keySet());
        Collections.sort(tIndices);
        for (Integer idx : tIndices) {
            Map<String, String> fields = techniqueMap.get(idx);
            if (fields == null) continue;
            String tname = fields.get("name");
            TechniqueType ttype = parseTechniqueType(fields.get("type"));
            String ownerName = fields.get("owner");
            long tdamage = parseLongSafe(fields.get("damage"), 0L);

            Technique tech = new Technique(tname, ttype);
            Sorcerer owner = findSorcererByName(sorcerers, ownerName);
            if (owner == null && ownerName != null) {
                owner = new Sorcerer(ownerName, null);
                sorcerers.add(owner);
            }
            techniques.add(new TechniqueUsage(tech, owner, tdamage));
        }

        if (missionId == null) {
            throw new MissionParsingException("Missing required field: missionId");
        }
        if (date == null) {
            throw new MissionParsingException("Missing required field: date");
        }
        if (location == null) {
            throw new MissionParsingException("Missing required field: location");
        }

        return new Mission(missionId, date, location, outcome, damageCost, curse, sorcerers, techniques, comment);
    }

    private MissionOutcome parseOutcome(String raw) {
        if (raw == null) return MissionOutcome.UNKNOWN;
        try { return MissionOutcome.valueOf(raw.trim()); }
        catch (Exception e) { return MissionOutcome.UNKNOWN; }
    }

    private ThreatLevel parseThreatLevel(String raw) {
        if (raw == null) return null;
        try { return ThreatLevel.valueOf(raw.trim()); }
        catch (Exception e) { return null; }
    }

    private SorcererRank parseSorcererRank(String raw) {
        if (raw == null) return null;
        try { return SorcererRank.valueOf(raw.trim()); }
        catch (Exception e) { return null; }
    }

    private TechniqueType parseTechniqueType(String raw) {
        if (raw == null) return null;
        try { return TechniqueType.valueOf(raw.trim()); }
        catch (Exception e) { return null; }
    }

    private long parseLongSafe(String raw, long defaultValue) {
        if (raw == null) return defaultValue;
        try { return Long.parseLong(raw.trim()); } catch (Exception e) { return defaultValue; }
    }

    private Sorcerer findSorcererByName(List<Sorcerer> list, String name) {
        if (name == null) return null;
        for (Sorcerer s : list) {
            if (s.getName() != null && s.getName().equals(name)) return s;
        }
        return null;
    }
}