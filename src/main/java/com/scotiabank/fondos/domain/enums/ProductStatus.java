package com.scotiabank.fondos.domain.enums;

public enum ProductStatus {
    ACTIVE(true),
    INACTIVE(false),
    DISCONTINUED(false);

    private final boolean isSaleable;

    ProductStatus(boolean isSaleable) {
        this.isSaleable = isSaleable;
    }

    public boolean isSaleable() {
        return this.isSaleable;
    }

    public static ProductStatus fromBoolean(Boolean state) {
        if (state == null) return INACTIVE;
        return state ? ACTIVE : INACTIVE;
    }
}