package com.hujing.ideamessage;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;
import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.event.ContactChangeEvent;
import com.hujing.ideamessage.event.ExitEvent;
import com.hujing.ideamessage.view.ChatActivity;
import com.hujing.ideamessage.view.MainActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.Iterator;
import java.util.List;


/**
 * Created by acer on 2017/4/30.
 */

public class MyApplication extends Application {
    private static final String TAG = "执行";
    private SoundPool soundPool;
    private int back;
    private int fore;

    @Override
    public void onCreate() {
        initSoundPool();
        initEaseMob();
        initAVOSCloud();
        LitePal.initialize(this);
    }

    private void initSoundPool() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        back = soundPool.load(getApplicationContext(), R.raw.back, 1);
        fore = soundPool.load(getApplicationContext(), R.raw.fore, 1);

    }

    private void initAVOSCloud() {
        AVOSCloud.initialize(this,"pJn3TLLjWx0VphTUQ7MLN51k-gzGzoHsz","YptbUTPu86WS2tWKbjeRpfI1");
        AVOSCloud.setDebugLogEnabled(true);
    }

    private void initEaseMob() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(true);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        //监听联系人变化
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                EventBus.getDefault().post(new ContactChangeEvent(username,true));
            }

            @Override
            public void onContactDeleted(String username) {
                EventBus.getDefault().post(new ContactChangeEvent(username,false));
            }

            @Override
            public void onContactInvited(String username, String reason) {
                try {
                    EMClient.getInstance().contactManager().acceptInvitation(username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFriendRequestAccepted(String username) {

            }

            @Override
            public void onFriendRequestDeclined(String username) {

            }
        });
        //监听消息
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                EventBus.getDefault().post(messages);
                if (isAppInBackground()){
                    soundPool.play(back,1,1,0,0,1);
                    EMMessage emMessage = messages.get(0);
                    sendNotification(emMessage);
                }else {
                    soundPool.play(fore,1,1,0,0,1);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        });
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {
                Log.d(TAG, "onDisconnected: ");
                if (errorCode== EMError.USER_LOGIN_ANOTHER_DEVICE){
                    EventBus.getDefault().post(new ExitEvent(errorCode));
                }
            }
        });
    }

    private void sendNotification(EMMessage emMessage) {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.arrow);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.arrow));
        builder.setContentTitle(emMessage.getFrom());
        EMTextMessageBody text= (EMTextMessageBody) emMessage.getBody();
        builder.setContentText(text.getMessage());
        Intent mainIntent=new Intent(getApplicationContext(), MainActivity.class);
        Intent chatIntent=new Intent(getApplicationContext(), ChatActivity.class);
        chatIntent.putExtra(Value.CONTACT,emMessage.getFrom());
        Intent[] intents=new Intent[]{mainIntent,chatIntent};
        PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(),
                1, intents, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manger= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manger.notify(1,notification);
    }


    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * @return true处于后台 false处于前台
     */
    private boolean isAppInBackground(){
        ActivityManager activityManager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(50);
        if (!tasks.isEmpty()){
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(getPackageName())){
                return true;
            }
        }
        return false;
    }
}
