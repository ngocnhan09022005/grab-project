package com.grapapplication3.grab_backendapplication.service.exception;

public class DeadlockException extends RuntimeException {
    public DeadlockException(String message) {
        super(message);
    }
}
