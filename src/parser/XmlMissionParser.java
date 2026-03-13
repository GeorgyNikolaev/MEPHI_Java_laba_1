package parser;

import domain.*;
import enums.*;
import exception.MissionParsingException;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DOM-based XML parser. Expects structure similar to sample:
 * <mission> <missionId/> <date/> <location/> <outcome/> <damageCost/> <curse>...</curse>
 * <sorcerers><sorcerer>...</sorcerer></sorcerers>
 * <techniques><technique>...</technique></techniques>
 */
public class XmlMissionParser implements MissionParser {

    @Override
    public Mission parse(Path file) throws MissionParsingException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file.toFile());
            Element root = doc.getDocumentElement();

            String missionId = getTextContent(root, "missionId", true);
            LocalDate date = LocalDate.parse(getTextContent(root, "date", true));
            String location = getTextContent(root, "location", true);
            MissionOutcome outcome = parseOutcome(getTextContent(root, "outcome", false));
            long damageCost = parseLongSafe(getTextContent(root, "damageCost", false), 0L);
            String comment = getTextContent(root, "note", false);

            // curse
            Curse curse = null;
            NodeList curseNodes = root.getElementsByTagName("curse");
            if (curseNodes.getLength() > 0) {
                Element cEl = (Element) curseNodes.item(0);
                String cname = getTextContent(cEl, "name", true);
                ThreatLevel tLevel = parseThreatLevel(getTextContent(cEl, "threatLevel", false));
                curse = new Curse(cname, tLevel);
            }

            // sorcerers
            List<Sorcerer> sorcerers = new ArrayList<>();
            NodeList sNodes = root.getElementsByTagName("sorcerer");
            for (int i = 0; i < sNodes.getLength(); i++) {
                Element sEl = (Element) sNodes.item(i);
                String name = getTextContent(sEl, "name", true);
                SorcererRank rank = parseSorcererRank(getTextContent(sEl, "rank", false));
                sorcerers.add(new Sorcerer(name, rank));
            }

            // techniques
            List<TechniqueUsage> techniques = new ArrayList<>();
            NodeList tNodes = root.getElementsByTagName("technique");
            for (int i = 0; i < tNodes.getLength(); i++) {
                Element tEl = (Element) tNodes.item(i);
                String tname = getTextContent(tEl, "name", true);
                TechniqueType ttype = parseTechniqueType(getTextContent(tEl, "type", false));
                String ownerName = getTextContent(tEl, "owner", false);
                long damage = parseLongSafe(getTextContent(tEl, "damage", false), 0L);

                Technique technique = new Technique(tname, ttype);
                Sorcerer owner = findSorcererByName(sorcerers, ownerName);
                if (owner == null && ownerName != null) {
                    owner = new Sorcerer(ownerName, null);
                    sorcerers.add(owner);
                }
                techniques.add(new TechniqueUsage(technique, owner, damage));
            }

            return new Mission(missionId, date, location, outcome, damageCost, curse, sorcerers, techniques, comment);

        } catch (MissionParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new MissionParsingException("Failed to parse XML: " + e.getMessage(), e);
        }
    }

    private String getTextContent(Element parent, String tag, boolean required) throws MissionParsingException {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() == 0) {
            if (required) throw new MissionParsingException("Missing required tag: " + tag);
            return null;
        }
        return nodes.item(0).getTextContent();
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

    private Sorcerer findSorcererByName(List<Sorcerer> list, String name) {
        if (name == null) return null;
        for (Sorcerer s : list) {
            if (s.getName() != null && s.getName().equals(name)) return s;
        }
        return null;
    }

    private long parseLongSafe(String raw, long defaultValue) {
        if (raw == null) return defaultValue;
        try { return Long.parseLong(raw.trim()); } catch (Exception e) { return defaultValue; }
    }
}