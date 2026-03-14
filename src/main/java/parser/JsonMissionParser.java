package parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;
import enums.*;

import exception.MissionParsingException;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 Maven dependency (add to pom.xml):
 <dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.14.2</version>
 </dependency>
*/
public class JsonMissionParser implements MissionParser {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mission parse(Path file) throws MissionParsingException {
        try {
            JsonNode root = mapper.readTree(file.toFile());

            String missionId = getText(root, "missionId", true);
            LocalDate date = LocalDate.parse(getText(root, "date", true));
            String location = getText(root, "location", true);

            MissionOutcome outcome = parseOutcome(getText(root, "outcome", false));
            long damageCost = root.has("damageCost") ? root.get("damageCost").asLong(0) : 0L;
            String comment = root.has("note") ? root.get("note").asText(null) : null;

            // curse
            JsonNode curseNode = root.path("curse");
            Curse curse = null;
            if (!curseNode.isMissingNode()) {
                String curseName = getText(curseNode, "name", true);
                ThreatLevel tLevel = parseThreatLevel(getText(curseNode, "threatLevel", false));
                curse = new Curse(curseName, tLevel);
            }

            // sorcerers
            List<Sorcerer> sorcerers = new ArrayList<>();
            JsonNode sorcs = root.path("sorcerers");
            if (sorcs.isArray()) {
                for (JsonNode sn : sorcs) {
                    String name = getText(sn, "name", true);
                    SorcererRank rank = parseSorcererRank(getText(sn, "rank", false));
                    sorcerers.add(new Sorcerer(name, rank));
                }
            }

            // techniques -> TechniqueUsage (technique + owner)
            List<TechniqueUsage> techniques = new ArrayList<>();
            JsonNode techs = root.path("techniques");
            if (techs.isArray()) {
                for (JsonNode tn : techs) {
                    String tname = getText(tn, "name", true);
                    TechniqueType ttype = parseTechniqueType(getText(tn, "type", false));
                    String ownerName = getText(tn, "owner", false);
                    long damage = tn.has("damage") ? tn.get("damage").asLong(0L) : 0L;

                    Technique technique = new Technique(tname, ttype);

                    Sorcerer owner = findSorcererByName(sorcerers, ownerName);
                    if (owner == null && ownerName != null) {
                        // create placeholder sorcerer with null rank
                        owner = new Sorcerer(ownerName, null);
                        sorcerers.add(owner);
                    }

                    techniques.add(new TechniqueUsage(technique, owner, damage));
                }
            }

            return new Mission(missionId, date, location, outcome, damageCost, curse, sorcerers, techniques, comment);

        } catch (IOException e) {
            throw new MissionParsingException("Failed to read JSON file: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new MissionParsingException("Invalid JSON structure: " + e.getMessage(), e);
        }
    }

    private String getText(JsonNode node, String field, boolean required) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            if (required) throw new RuntimeException("Missing required field: " + field);
            return null;
        }
        return v.asText();
    }

    private Sorcerer findSorcererByName(List<Sorcerer> list, String name) {
        if (name == null) return null;
        for (Sorcerer s : list) {
            if (s.getName() != null && s.getName().equals(name)) return s;
        }
        return null;
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
}