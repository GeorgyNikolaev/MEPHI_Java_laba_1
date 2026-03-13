package parser;

import java.nio.file.Path;

public class MissionParserFactory {

    public static MissionParser getParser(Path file) {
        String name = file.toString().toLowerCase();
        if (name.endsWith(".json")) return new JsonMissionParser();
        if (name.endsWith(".xml")) return new XmlMissionParser();
        if (name.endsWith(".txt") || name.endsWith(".text")) return new TextMissionParser();
        throw new IllegalArgumentException("Unsupported file extension for file: " + file);
    }
}