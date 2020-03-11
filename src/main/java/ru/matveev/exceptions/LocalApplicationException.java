package ru.matveev.exceptions;

public class LocalApplicationException extends RuntimeException {

    public LocalApplicationException(String message) {
        super(message);
    }
}
