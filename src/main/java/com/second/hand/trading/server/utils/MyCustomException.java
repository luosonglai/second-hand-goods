package com.second.hand.trading.server.utils;

/**
 * @Description
 * @Author:luosonglai
 * @CreateTime:2024/3/710:56
 */
public class MyCustomException  extends  Exception{
    // 构造方法
    public MyCustomException() {
        super();
    }

    public MyCustomException(String message) {
        super(message);
    }

    public MyCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyCustomException(Throwable cause) {
        super(cause);
    }
}
