package com.second.hand.trading.server.enums;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/610:03
 */
public enum Degree {
    ALL_NEW("全新", 100),
    NINETY_PERCENT("九成新", 90),
    EIGHTY_PERCENT("八成新", 80),
    SEVEN_PERCENT("七成新", 70);

    private final String display;
    private final int value;

    Degree(String display, int value) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public int getValue() {
        return value;
    }
}
