package ru.isands.test.estore.exception;

import java.util.List;

public class IllegalParamException extends BaseRestException{
    public IllegalParamException(String message, List<String> details) {
        super(message, details);
    }
}
