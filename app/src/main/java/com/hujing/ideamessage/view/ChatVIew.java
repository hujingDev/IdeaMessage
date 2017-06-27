package com.hujing.ideamessage.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by acer on 2017/5/12.
 */

public interface ChatVIew {
    void getHistoryMsg(List<EMMessage> messages);
    void updateList();
}
