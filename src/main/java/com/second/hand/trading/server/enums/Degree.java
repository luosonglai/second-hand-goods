package com.second.hand.trading.server.enums;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/610:03
 */
public enum Degree  {
    ALL_NEW("全新", 0),
    NINETY_PERCENT("九成新", 1),
    EIGHTY_PERCENT("八成新", 2),
    SEVEN_PERCENT("七成新", 3);

    private final String display;
    private final int value;

    Degree(String display, int value) {
        this.display = display;
        this.value = value;
    }

    public String getDisplay(){
        return this.display;
    }
    public int getValue() {
        return value;
    }
    public static String stateOf(Integer value){
        if (value == null) {
            return null;
        }
        for (Degree degree : Degree.values()) {
            if (degree.ordinal() == value) {
                return degree.display;
            }
        }
        return null;

    }
}
