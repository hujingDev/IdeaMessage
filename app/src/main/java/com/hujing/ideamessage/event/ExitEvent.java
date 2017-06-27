package com.hujing.ideamessage.event;

/**
 * Created by acer on 2017/5/16.
 */

public class ExitEvent {
    int errorCode;

    public ExitEvent(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
