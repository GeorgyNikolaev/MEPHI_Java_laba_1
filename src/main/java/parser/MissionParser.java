package parser;

import domain.Mission;
import exception.MissionParsingException;

import java.nio.file.Path;

public interface MissionParser {
    Mission parse(Path file) throws MissionParsingException;
}