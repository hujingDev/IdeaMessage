package com.hujing.ideamessage.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by acer on 2017/5/8.
 */

public class Friend extends DataSupport {
    private String user;
    private String friend;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
