package com.hujing.ideamessage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hujing.ideamessage.adapter.MsgAdapter;
import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.presenter.impl.MsgPresenterImpl;
import com.hujing.ideamessage.view.ChatActivity;
import com.hujing.ideamessage.view.MainActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by acer on 2017/5/7.
 */

public class MsgFragment extends BaseFragment implements MsgFragmentView {
    private static final String TAG = "MsgFragment";
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private MsgPresenterImpl msgPresenter;
    private MsgAdapter msgAdapter;
    private MainActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.msg_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_msg_contact);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        msgPresenter = new MsgPresenterImpl(this);
        setRecyclerView();
        setOnClickListener();
        return view;
    }

    private void setOnClickListener() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"是否清空未读消息？",Snackbar.LENGTH_SHORT)
                        .setAction("是", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                msgPresenter.clearAllUnreadMsg();
                            }
                        }).show();
            }
        });
    }

    private void setRecyclerView() {
        LinearLayoutManager manger=new LinearLayoutManager(getContext());
        msgAdapter = new MsgAdapter(null);
        recyclerView.setLayoutManager(manger);
        recyclerView.setAdapter(msgAdapter);
        msgPresenter.getConversations();
        msgAdapter.setOnMsgItemClickListener(new MsgAdapter.OnMsgItemClickListener() {
            @Override
            public void click(String from, View v) {
                Intent intent=new Intent(getContext(), ChatActivity.class);
                intent.putExtra(Value.CONTACT,from);
                RelativeLayout relativeLayout= (RelativeLayout) v.findViewById(R.id.rl_unread);
                relativeLayout.setVisibility(View.GONE);
                activity.badgeItem.hide();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onGetAllConversations(List<EMConversation> conversations) {
        msgAdapter.setConversations(conversations);
        msgAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllUnreadMessage() {
        msgPresenter.getConversations();
        activity.updateBadgeItem();
    }

    @Override
    public void onStart() {
        super.onStart();
        activity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnGetNewMessage(List<EMMessage> messages){
        msgPresenter.getConversations();
    }
}
