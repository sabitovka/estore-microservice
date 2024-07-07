package ru.isands.test.estore.exception;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class BaseRestException extends RuntimeException {
    private final List<String> details = new ArrayList<>();
    public BaseRestException(String message, List<String> details) {
        super(message);
        this.details.addAll(details);
    }
}
