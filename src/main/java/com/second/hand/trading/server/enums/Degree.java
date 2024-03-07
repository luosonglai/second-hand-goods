package com.second.hand.trading.server.enums;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/610:03
 */
public enum Degree {
    All_NEW("allNew","全新"),
    NINETY_PERCENT_NEW("ninetyPercent","九成新"),
    EIGHTY_PERCENT_NEW("eightPercent","八成新"),
    SEVENTY_PERCENT_NEW("servenPercent","七成新");

    private final String name;
    private final String display;

    Degree(String name, String display) {
        this.name = name;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

}
