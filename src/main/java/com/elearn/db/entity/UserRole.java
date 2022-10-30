package com.elearn.db.entity;

public enum UserRole {
    CASHIER("CASHIER"), SENIOR_CASHIER("SENIOR_CASHIER"), COMMODITY_EXPERT("COMMODITY_EXPERT");

    private final String name;

    private UserRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name();
    }
}
