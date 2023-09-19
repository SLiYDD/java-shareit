package ru.practicum.shareit.exceptions;

public class ResourceAlreadyExistException extends RuntimeException {

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
