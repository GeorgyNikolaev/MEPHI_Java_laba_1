package parser;

import java.nio.file.Path;

public class MissionParserFactory {

    public static MissionParser getParser(Path file) {
        String name = file.toString().toLowerCase();

        return switch (getExtension(name)) {
            case "json" -> new JsonMissionParser();
            case "xml" -> new XmlMissionParser();
            case "txt", "text" -> new TextMissionParser();
            default -> throw new IllegalArgumentException("Unsupported file extension for file: " + file);
        };
    }

    private static String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) return "";
        return filename.substring(lastDot + 1);
    }
}