package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        log.warn("Error", e);
        return new ErrorResponse("error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Error", e);
        return new ErrorResponse("Not Valid", e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("Error", e);
        return new ErrorResponse("Not Found", e.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleResourceAlreadyExistException(final ResourceAlreadyExistException e) {
        log.warn("Error", e);
        return new ErrorResponse("Already Exist", e.getMessage());
    }
}
