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


public class JsonMissionParser extends MissionParser {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Mission parse(Path file) throws MissionParsingException {
        try {
            JsonNode root = mapper.readTree(file.toFile());

            String missionId = getText(root, "missionId", true);
            LocalDate date = LocalDate.parse(getText(root, "date", true));
            String location = getText(root, "location", true);

            MissionOutcome outcome = parseOutcome(getText(root, "outcome", false));
            long damageCost = parseLongSafe(getText(root, "damageCost", false));
            String comment = getText(root, "node", false);

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
                    String sorcererName = getText(sn, "name", true);
                    SorcererRank sorcererRank = parseSorcererRank(getText(sn, "rank", false));
                    sorcerers.add(new Sorcerer(sorcererName, sorcererRank));
                }
            }

            // techniques
            List<TechniqueUsage> techniques = new ArrayList<>();
            JsonNode techs = root.path("techniques");
            if (techs.isArray()) {
                for (JsonNode tn : techs) {
                    String techName = getText(tn, "name", true);
                    TechniqueType techType = parseTechniqueType(getText(tn, "type", false));
                    String ownerName = getText(tn, "owner", false);
                    long damage = parseLongSafe(getText(tn, "damage", true));

                    Technique technique = new Technique(techName, techType);

                    Sorcerer owner = findSorcererByName(sorcerers, ownerName);
                    if (owner == null && ownerName != null) {
                        // Создаем заглушку
                        owner = new Sorcerer(ownerName, null);
                        sorcerers.add(owner);
                    }

                    techniques.add(new TechniqueUsage(technique, owner, damage));
                }
            }

            return new Mission(missionId, date, location, outcome, damageCost, curse, sorcerers, techniques, comment);

        } catch (IOException e) {
            throw new MissionParsingException("Ошибка чтения JSON файла: " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new MissionParsingException("Недопустимая JSON структура: " + e.getMessage(), e);
        }
    }

    private String getText(JsonNode node, String field, boolean required) {
        JsonNode v = node.get(field);
        if (v == null || v.isNull()) {
            if (required) throw new RuntimeException("Не найдено обязательное поле: " + field);
            return "";
        }
        return v.asText();
    }
}