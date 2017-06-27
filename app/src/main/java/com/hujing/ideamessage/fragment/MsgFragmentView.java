package com.hujing.ideamessage.fragment;

import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by acer on 2017/5/14.
 */

public interface MsgFragmentView {
    void onGetAllConversations(List<EMConversation> conversations);
    void clearAllUnreadMessage();
}
