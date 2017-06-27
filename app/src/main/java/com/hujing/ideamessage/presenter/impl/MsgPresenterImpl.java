package com.hujing.ideamessage.presenter.impl;

import com.hujing.ideamessage.fragment.MsgFragmentView;
import com.hujing.ideamessage.presenter.MsgPresenter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by acer on 2017/5/14.
 */

public class MsgPresenterImpl implements MsgPresenter {
    MsgFragmentView msgFragmentView;
    private List<EMConversation> conversations;

    public MsgPresenterImpl(MsgFragmentView msgFragmentView) {
        this.msgFragmentView = msgFragmentView;
    }

    @Override
    public void getConversations() {
        Map<String, EMConversation> allConversations =
                EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> values = allConversations.values();
        conversations = new ArrayList<>(values);
        Collections.sort(conversations, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1, EMConversation o2) {
                return (int) (o1.getLastMessage().getMsgTime()-o2.getLastMessage().getMsgTime());
            }
        });
        msgFragmentView.onGetAllConversations(conversations);
    }

    @Override
    public void clearAllUnreadMsg() {
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        msgFragmentView.clearAllUnreadMessage();
    }
}
