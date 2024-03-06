package com.second.hand.trading.server.enums;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/610:03
 */
public enum Degree {
    All_NEW("全新"),
    NINETY_PERCENT_NEW("九成新"),
    EIGHTY_PERCENT_NEW("八成新"),
    SEVENTY_PERCENT_NEW("七成新");

    private String msg;

    Degree(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
