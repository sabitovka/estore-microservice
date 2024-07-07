package ru.isands.test.estore.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class EntityNotValidException extends BaseRestException {
    public EntityNotValidException(String message, List<String> details) {
        super(message, details);
    }
}
