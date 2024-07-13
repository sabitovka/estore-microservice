package ru.isands.test.estore.exception;

import java.util.List;

public class ElectroItemOutOfStockException extends BaseRestException {
    public ElectroItemOutOfStockException(String message, List<String> details) {
        super(message, details);
    }
}
