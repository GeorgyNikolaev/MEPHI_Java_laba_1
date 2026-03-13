package parser;

import domain.Mission;
import exception.MissionParsingException;

import java.nio.file.Path;

public interface MissionParser {
    /**
     * Parse file into domain.Mission.
     *
     * @param file path to input file
     * @return Mission instance
     * @throws MissionParsingException on parsing/validation errors
     */
    Mission parse(Path file) throws MissionParsingException;
}