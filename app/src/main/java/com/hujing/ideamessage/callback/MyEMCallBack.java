package com.hujing.ideamessage.callback;

import com.hujing.ideamessage.utils.ThreadUtils;
import com.hyphenate.EMCallBack;

/**
 * Created by acer on 2017/5/6.
 */

public abstract class MyEMCallBack implements EMCallBack {
    //EMCallBack的回调没有在主线程中，重写回调使其运行在主线程中
    public abstract void success();

    public abstract void failed(int code, String error);
    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainThread(
                new Runnable() {
                    @Override
                    public void run() {
                        success();
                    }
                }
        );
    }

    @Override
    public void onError(final int code, final String error) {

        ThreadUtils.runOnMainThread(
                new Runnable() {
                    @Override
                    public void run() {
                       failed(code,error);
                    }
                }
        );
    }

    @Override
    public void onProgress(int progress, String status) {

    }
}
