package main.java.parser;

import main.java.domain.Mission;
import main.java.exception.MissionParsingException;

import java.nio.file.Path;

public interface MissionParser {
    /**
     * Parse file into main.java.domain.Mission.
     *
     * @param file path to input file
     * @return Mission instance
     * @throws MissionParsingException on parsing/validation errors
     */
    Mission parse(Path file) throws MissionParsingException;
}