package app;

import parser.MissionParser;
import parser.MissionParserFactory;
import domain.Mission;
import io.MissionPrinter;
import exception.MissionParsingException;

import java.nio.file.Path;
import java.util.Scanner;

public class MissionAnalyzerApp {

    public static void main(String[] args) {
        String pathInput;
        if (args != null && args.length > 0 && args[0] != null && !args[0].isBlank()) {
            pathInput = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter path to mission file: ");
            pathInput = scanner.nextLine().trim();
        }

        try {
            Path file = Path.of(pathInput);
            MissionParser parser = MissionParserFactory.getParser(file);
            domain.Mission mission = parser.parse(file);
            MissionPrinter.print(mission, System.out);
        } catch (MissionParsingException e) {
            System.err.println("Parsing error: " + e.getMessage());
            e.printStackTrace(System.err);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}