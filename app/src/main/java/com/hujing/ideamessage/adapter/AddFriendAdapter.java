package com.hujing.ideamessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hujing.ideamessage.R;

import java.util.List;

/**
 * Created by acer on 2017/5/10.
 */

public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ViewHolder> {
    private static final String TAG = "？";
    List<String> mContacts;
    List<String> mExistContacts;
    OnItemClickListener onItemClickListener;
    public AddFriendAdapter(List<String> contacts,List<String> existContacts) {
        mContacts=contacts;
        mExistContacts=existContacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_friend_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String contact = mContacts.get(position);
        if (mExistContacts.contains(contact)){
            holder.button.setEnabled(false);
            holder.button.setText("已添加");
        }else {
            holder.button.setEnabled(true);
        }
        holder.textView.setText(contact);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b = onItemClickListener == null;
                Log.d(TAG, "onClick: "+b);
                if (onItemClickListener!=null) {
                    Log.d(TAG, "onClick: ");
                    onItemClickListener.clickButton(v,contact);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mContacts==null){
            return 0;
        }else {
            return mContacts.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.tv_search_userName);
            button= (Button) itemView.findViewById(R.id.btn_add_friend);
        }
    }
    public interface OnItemClickListener{
        void clickButton(View v,String contact);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener=listener;
    }
    public void setList(List<String> contacts,List<String> existContacts){
        mContacts=contacts;
        mExistContacts=existContacts;
    }
}
