package ru.km.kafka.conference.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.km.kafka.conference.exception.AlreadyExistsException;
import ru.km.kafka.conference.exception.NotFoundException;
import ru.km.kafka.conference.dto.ResponseDto;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseDto<String> handleBadRequestException(Exception exception) {
        return new ResponseDto<>(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseDto<String> handleNotFoundException(Exception exception) {
        return new ResponseDto<>(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseDto<String> handleOtherException(Exception exception) {
        return new ResponseDto<>(
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
}
