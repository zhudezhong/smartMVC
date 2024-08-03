package com.scnu.smartmvc.exception;

public class TestException extends RuntimeException {
    private String name;

    public TestException(String message, String name) {
        super(message);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
