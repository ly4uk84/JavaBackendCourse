package org.JavaBackendCourse;

public enum Currency {
    USD("USD", "Доллар США"),
    EUR("EUR", "Евро"),
    GBP("GBP", "Фунт стерлингов"),
    JPY("JPY", "Японская иена"),
    CHF("CHF", "Швейцарский франк");

    private final String code;
    private final String description;

    Currency(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.code.equalsIgnoreCase(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("неизвестный код валюты: " + code);
    }

    @Override
    public String toString() {
        return code;
    }
}
