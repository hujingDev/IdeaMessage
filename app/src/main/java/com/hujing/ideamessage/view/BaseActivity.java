package com.hujing.ideamessage.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hujing.ideamessage.event.ExitEvent;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by acer on 2017/5/16.
 */

public class BaseActivity extends AppCompatActivity {
    private LocalReceiver localReceiver;
    private LocalBroadcastManager manager;
    private IntentFilter intentFilter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.hujing.ideamessage.FORCE_LOGOUT");
        localReceiver=new LocalReceiver();
        manager.registerReceiver(localReceiver,intentFilter);
        builder = new AlertDialog.Builder(this);
    }
    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.hujing.ideamessage.FORCE_LOGOUT")){
                finish();
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccountDisconnected(ExitEvent exitEvent){
        if (exitEvent.getErrorCode()== EMError.USER_LOGIN_ANOTHER_DEVICE) {
            EMClient.getInstance().logout(true);
            builder.setTitle("下线通知");
            builder.setMessage("您的账号已在其他设备登录，该设备将会强制下线");
            builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent loginIntent=new Intent(BaseActivity.this, LoginActivity.class);
                    Intent intent = new Intent("com.hujing.ideamessage.FORCE_LOGOUT");
                    manager.sendBroadcast(intent);
                    startActivity(loginIntent);
                }
            });
            builder.show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
