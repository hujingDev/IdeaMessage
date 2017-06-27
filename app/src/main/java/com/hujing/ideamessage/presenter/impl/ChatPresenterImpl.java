package com.hujing.ideamessage.presenter.impl;

import android.util.Log;

import com.hujing.ideamessage.callback.MyEMCallBack;
import com.hujing.ideamessage.presenter.ChatPresenter;
import com.hujing.ideamessage.utils.ThreadUtils;
import com.hujing.ideamessage.view.ChatVIew;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/5/12.
 */

public class ChatPresenterImpl implements ChatPresenter {
    private static final String TAG = "ChatPresenterImpl";
    private ChatVIew chatView;
    public ChatPresenterImpl(ChatVIew chatView) {
        this.chatView =chatView;
    }

    private List<EMMessage> messageList=new ArrayList<>();
    @Override
    public void getChatHistoryMessage(String contact) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(contact);
        if (conversation!=null){
            conversation.markAllMessagesAsRead();
            EMMessage lastMessage = conversation.getLastMessage();
            String msgId = lastMessage.getMsgId();
            int allMsgCount = conversation.getAllMsgCount();
            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, allMsgCount);
            messageList.clear();
            messageList.addAll(emMessages);
            messageList.add(lastMessage);
            chatView.getHistoryMsg(messageList);
        }
    }

    @Override
    public void sendMessage(String msg, final String contact) {
        final EMMessage message = EMMessage.createTxtSendMessage(msg, contact);
        messageList.add(message);
        message.setMessageStatusCallback(new MyEMCallBack() {
            @Override
            public void success() {
                chatView.updateList();
                message.setStatus(EMMessage.Status.SUCCESS);
            }

            @Override
            public void failed(int code, String error) {
                chatView.updateList();
                message.setStatus(EMMessage.Status.FAIL);
            }
        });
        chatView.getHistoryMsg(messageList);
        ThreadUtils.runOnNoUIThread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        });
    }
}
