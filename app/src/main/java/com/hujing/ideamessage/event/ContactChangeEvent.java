package com.hujing.ideamessage.event;

/**
 * Created by acer on 2017/5/10.
 */

public  class ContactChangeEvent {
    public String userName;
    public boolean isAddOrDel;//true为增加，false为删除

    public ContactChangeEvent(String userName, boolean isAddOrDel) {
        this.userName = userName;
        this.isAddOrDel = isAddOrDel;
    }
}
