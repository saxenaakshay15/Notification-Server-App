package notification_server.notification_server.exception;

public class InvalidSMSMessageException extends RuntimeException {
    public InvalidSMSMessageException(String message) {
        super(message);
    }
}
