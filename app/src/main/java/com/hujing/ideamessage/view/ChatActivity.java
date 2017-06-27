package com.hujing.ideamessage.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hujing.ideamessage.adapter.ChatAdapter;
import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.presenter.impl.ChatPresenterImpl;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements ChatVIew,View.OnClickListener{
    private static final String TAG = "ChatActivity";
    @BindView(R.id.tb_chat)
     Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title_chat)
     TextView textViewTitle;
    @BindView(R.id.et_input)
     EditText editText;
    @BindView(R.id.btn_send)
     Button button;
    @BindView(R.id.rv_chat)
    RecyclerView recyclerViewChat;
    private String contact;
    private ChatPresenterImpl chatPresenter;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        contact = intent.getStringExtra(Value.CONTACT);
        textViewTitle.setText("与"+contact+"聊天中");
        initToolbar();
        button.setOnClickListener(this);
        chatPresenter = new ChatPresenterImpl(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    button.setEnabled(false);
                }else {
                    button.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        adapter = new ChatAdapter(null);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerViewChat.setLayoutManager(manager);
        recyclerViewChat.setAdapter(adapter);
        chatPresenter.getChatHistoryMessage(contact);
    }



    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void getHistoryMsg(List<EMMessage> messages) {
        adapter.setMessageList(messages);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount()>0){
            recyclerViewChat.scrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount()>0){
//            recyclerViewChat.smoothScrollToPosition(adapter.getItemCount()-1);
            recyclerViewChat.scrollToPosition(adapter.getItemCount()-1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                String msg = editText.getText().toString().trim();
                chatPresenter.sendMessage(msg,contact);
                editText.setText("");
                break;
            default:
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessageEvent(List<EMMessage> messages){
        chatPresenter.getChatHistoryMessage(contact);
    }
}
