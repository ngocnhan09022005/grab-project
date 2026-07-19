package com.grapapplication3.grab_backendapplication.service;

public enum BookingScenario {
    STANDARD,
    CUSTOMER_NOT_FOUND,
    DRIVER_NOT_FOUND,
    DEADLOCK,
    PAYMENT_FAILURE;

    public static BookingScenario fromString(String value) {
        if (value == null) {
            return STANDARD;
        }
        return switch (value.toUpperCase()) {
            case "CUSTOMER_NOT_FOUND" -> CUSTOMER_NOT_FOUND;
            case "DRIVER_NOT_FOUND" -> DRIVER_NOT_FOUND;
            case "DEADLOCK" -> DEADLOCK;
            case "PAYMENT_FAILURE" -> PAYMENT_FAILURE;
            default -> STANDARD;
        };
    }
}
