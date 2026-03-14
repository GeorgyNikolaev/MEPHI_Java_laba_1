package app;

import domain.Mission;
import parser.MissionParser;
import parser.MissionParserFactory;
import io.MissionPrinter;
import exception.MissionParsingException;

import java.nio.file.Path;
import java.util.Scanner;

public class MissionAnalyzerApp {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String pathInput;

            System.out.println("Program started.");

            while (true) {
                System.out.print("Enter path to mission file (or 'q' to exit): ");
                pathInput = scanner.nextLine().trim();

                if (pathInput.equals("q")) {
                    System.out.println("Exiting...");
                    break;
                }

                try {
                    Path file = Path.of(pathInput);
                    MissionParser parser = MissionParserFactory.getParser(file);
                    Mission mission = parser.parse(file);
                    MissionPrinter.print(mission, System.out);
                } catch (MissionParsingException e) {
                    System.out.println("Ошибка парсинга: " + e.getMessage() + "\n");
                } catch (IllegalArgumentException e) {
                    System.out.println("Файл с путем: `" + pathInput + "` не найден.\n");
                } catch (Exception e) {
                    System.out.println("Неизвестная ошибка: " + e.getMessage() + "\n");
                    e.printStackTrace(System.err);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}