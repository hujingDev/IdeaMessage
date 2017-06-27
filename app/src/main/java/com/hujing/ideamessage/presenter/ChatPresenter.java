package com.hujing.ideamessage.presenter;


/**
 * Created by acer on 2017/5/12.
 */

public interface ChatPresenter {
    void getChatHistoryMessage(String contact);
    void sendMessage(String msg,String contact);
}
