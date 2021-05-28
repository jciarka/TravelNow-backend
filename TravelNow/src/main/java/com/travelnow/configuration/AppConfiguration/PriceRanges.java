package com.travelnow.configuration.AppConfiguration;

public enum PriceRanges {
    LOW(0, 50),
    MEDIUM(50, 120),
    HIGH(120, Integer.MAX_VALUE);

    public int downLimit;
    public int upLimit;

    PriceRanges(int downLimit, int upLimit) {
        this.downLimit = downLimit;
        this.upLimit = upLimit;
    }
}
