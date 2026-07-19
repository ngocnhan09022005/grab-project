package com.grapapplication3.grab_backendapplication.service.exception;

public class DriverBusyException extends RuntimeException {
    public DriverBusyException(String message) {
        super(message);
    }
}
