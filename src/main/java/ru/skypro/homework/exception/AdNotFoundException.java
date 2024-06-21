package ru.skypro.homework.exception;

public class AdNotFoundException extends RuntimeException {
    public AdNotFoundException() {
    }

    public AdNotFoundException(String message) {
        super(message);
    }

    public AdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdNotFoundException(Throwable cause) {
        super(cause);
    }
}
