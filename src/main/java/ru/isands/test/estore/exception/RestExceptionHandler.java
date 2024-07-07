package ru.isands.test.estore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    private ResponseEntity<ErrorResponse> createErrorResponse(BaseRestException exception, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), exception.getDetails()), httpStatus);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleEntityNotValidException(EntityNotValidException exception) {
        return createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalParamException(IllegalParamException exception) {
        return createErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        return createErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleSystemException(RuntimeException exception) {
        log.error("Произошла внутрення ошибка сервера: {}", exception.getMessage());
        log.error(Arrays.toString(exception.getStackTrace()));
        exception.printStackTrace();
        ErrorResponse response = new ErrorResponse("Произошла внутрення ошибка сервера", List.of(exception.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
