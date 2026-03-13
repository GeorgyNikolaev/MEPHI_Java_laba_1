package exception;

public class MissionParsingException extends Exception {

    public MissionParsingException(String message) {
        super(message);
    }

    public MissionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}