package com.hujing.ideamessage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hujing.ideamessage.utils.StringUtils;

import java.util.List;

/**
 * Created by acer on 2017/5/7.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    List<String> mContacts;
    private OnItemClickListener onItemClickListener;
    public ContactsAdapter(List<String> contacts) {
        mContacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String s = mContacts.get(position);
        holder.textViewSection.setText(StringUtils.getFirstChar(s));
        holder.textViewContact.setText(s);
        if (position == 0) {
            holder.textViewSection.setVisibility(View.VISIBLE);
        } else {
            String current = StringUtils.getFirstChar(s);
            String last=StringUtils.getFirstChar(mContacts.get(position-1));
            if (current.equals(last)){
                holder.textViewSection.setVisibility(View.GONE);
            }else {
                holder.textViewSection.setVisibility(View.VISIBLE);
            }
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.click(v,s);
                }
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener!=null) {
                    onItemClickListener.longClick(v,s);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSection;
        TextView textViewContact;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewSection = (TextView) itemView.findViewById(R.id.tv_section);
            textViewContact = (TextView) itemView.findViewById(R.id.tv_contact);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.ll_contact);
        }
    }

    public List<String> getContacts() {
        return mContacts;
    }
    public interface OnItemClickListener{
        void click(View view,String contact);
        void longClick(View view,String contact);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
}
