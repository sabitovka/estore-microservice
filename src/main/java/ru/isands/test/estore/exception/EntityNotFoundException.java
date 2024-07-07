package ru.isands.test.estore.exception;

import java.util.List;

public class EntityNotFoundException extends BaseRestException {
    public EntityNotFoundException(String message, List<String> details) {
        super(message, details);
    }
}
