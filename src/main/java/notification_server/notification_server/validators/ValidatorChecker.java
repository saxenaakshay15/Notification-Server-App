package notification_server.notification_server.validators;

import notification_server.notification_server.exception.InvalidPhoneNumberException;
import notification_server.notification_server.exception.InvalidSMSMessageException;
import notification_server.notification_server.exception.InvalidTextException;
import notification_server.notification_server.exception.InvalidTimeStringException;

import java.util.List;
import java.util.regex.Pattern;

public class ValidatorChecker {

    public static void validatePhoneNumber(String phoneNumber) {
        String regex = "^[+]?[0-9]{10,15}$";
        Pattern pattern = Pattern.compile(regex);
        if (phoneNumber == null || !pattern.matcher(phoneNumber).matches()) {
            throw new InvalidPhoneNumberException("Invalid phone number format: " + phoneNumber);
        }
    }

    public static void validatePhoneNumberList(List<String> phoneNumbers) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            throw new InvalidPhoneNumberException("Phone number list cannot be empty.");
        }
    }

    public static void validateSMSMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new InvalidSMSMessageException("SMS message cannot be empty.");
        }
        if (message.length() > 160) {
            throw new InvalidSMSMessageException("SMS message is too long. Maximum length is 160 characters.");
        }
    }

    public static void validateTimeString(String timeString, String timeType) {
        if (timeString == null || timeString.trim().isEmpty()) {
            throw new InvalidTimeStringException(timeType + " cannot be empty.");
        }
    }

    public static void validateText(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new InvalidTextException("Search text cannot be empty.");
        }
    }
}
