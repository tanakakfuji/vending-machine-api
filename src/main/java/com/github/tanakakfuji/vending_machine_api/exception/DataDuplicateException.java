package com.github.tanakakfuji.vending_machine_api.exception;

public class DataDuplicateException extends RuntimeException {
    public DataDuplicateException(String message) {
        super(message);
    }
}
