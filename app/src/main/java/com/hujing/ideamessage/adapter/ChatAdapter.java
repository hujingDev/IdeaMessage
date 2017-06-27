package com.hujing.ideamessage.adapter;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by acer on 2017/5/13.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<EMMessage> messageList;

    public ChatAdapter(List<EMMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = messageList.get(position);
        EMMessage.Direct direct = emMessage.direct();

        return direct == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_rev_item, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_send_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EMMessage emMessage = messageList.get(position);
        long msgTime = emMessage.getMsgTime();
        if (position==0){
            if (DateUtils.isCloseEnough(msgTime,System.currentTimeMillis())){
                holder.textViewTime.setVisibility(View.GONE);
            }else {
                holder.textViewTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
                holder.textViewTime.setVisibility(View.VISIBLE);
            }
        }else {
            EMMessage lastEmMessage = messageList.get(position - 1);
            long lastMsgTime = lastEmMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime,lastMsgTime)){
                holder.textViewTime.setVisibility(View.GONE);
            }else {
                holder.textViewTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
                holder.textViewTime.setVisibility(View.VISIBLE);
            }
        }
        EMTextMessageBody body = (EMTextMessageBody) emMessage.getBody();
        String message = body.getMessage();
        holder.textViewMsg.setText(message);
        if (emMessage.direct()== EMMessage.Direct.SEND){
            EMMessage.Status status=emMessage.status();
            switch (status){
                case SUCCESS:
                    holder.imageViewMsgState.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.imageViewMsgState.setImageResource(R.drawable.error);
                    break;
                case INPROGRESS:
                    holder.imageViewMsgState.setImageResource(R.drawable.progressbar);
                   /* AnimationDrawable drawable= (AnimationDrawable) holder.imageViewMsgState.getDrawable();
                    drawable.start();*/
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;
        TextView textViewMsg;
        ImageView imageViewMsgState;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            textViewTime= (TextView) itemView.findViewById(R.id.tv_time);
            imageViewMsgState= (ImageView) itemView.findViewById(R.id.iv_send_msg_state);
        }
    }
    public void setMessageList(List<EMMessage> messageList){
        this.messageList=messageList;
    }
}
