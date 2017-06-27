package com.hujing.ideamessage.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/5/14.
 */

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    List<EMConversation> conversations;
    OnMsgItemClickListener onMsgItemClickListener;

    public MsgAdapter(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMConversation emConversation = conversations.get(position);
        EMMessage lastMessage = emConversation.getLastMessage();
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount == 0) {
            holder.relativeLayout.setVisibility(View.GONE);
        }else if (unreadMsgCount>99){
            holder.textViewUnreadCount.setText("99+");
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }else {
            holder.textViewUnreadCount.setText(unreadMsgCount + "");
            holder.relativeLayout.setVisibility(View.VISIBLE);
        }
        String time = DateUtils.getTimestampString(new Date(lastMessage.getMsgTime()));
        holder.textViewTime.setText(time);
        final String from = lastMessage.getFrom();
        holder.textViewFriendName.setText(from);
        EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
        String message = body.getMessage();
        holder.textViewMsgContent.setText(message);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMsgItemClickListener != null) {
                    onMsgItemClickListener.click(from, v);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations == null ? 0 : conversations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFriendName;
        TextView textViewTime;
        TextView textViewMsgContent;
        TextView textViewUnreadCount;
        CardView cardView;
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewUnreadCount = (TextView) itemView.findViewById(R.id.tv_unread_count);
            textViewFriendName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            textViewTime = (TextView) itemView.findViewById(R.id.tv_last_msg_time);
            textViewMsgContent = (TextView) itemView.findViewById(R.id.tv_msg_content);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_unread);
            cardView = (CardView) itemView.findViewById(R.id.cv);
        }
    }

    public void setConversations(List<EMConversation> conversations) {
        this.conversations = conversations;
    }

    public interface OnMsgItemClickListener {
        void click(String from, View v);
    }

    public void setOnMsgItemClickListener(OnMsgItemClickListener onMsgItemClickListener) {
        this.onMsgItemClickListener = onMsgItemClickListener;
    }
}
