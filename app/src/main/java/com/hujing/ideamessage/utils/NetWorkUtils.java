package com.hujing.ideamessage.utils;

import android.util.Log;

import java.io.IOException;

/**
 * Created by acer on 2017/5/3.
 */

public class NetWorkUtils {

    public static void checkNetWorkState(final OnGetNewWorkState onGetNewWorkState){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
                    java.lang.Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);// ping网址3次
                    // ping的状态
                    int status = p.waitFor();
                    if (status == 0) {
                        result = "success";
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                onGetNewWorkState.onSuccess();
                            }
                        });
                        return;
                    } else {
                        result = "failed";
                    }
                } catch (IOException e) {
                    result = "IOException";
                } catch (InterruptedException e) {
                    result = "InterruptedException";
                } finally {
                    Log.d("----result---", "result = " + result);
                }
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        onGetNewWorkState.onFailed();
                    }
                });
            }
        }).start();
    }
    public static void isNetworkOnline(final OnGetNewWorkState onGetNewWorkState){
        ThreadUtils.runOnNoUIThread(new Runnable() {
            @Override
            public void run() {
                //为了用户体验，睡个1秒
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Runtime runtime=Runtime.getRuntime();
                try {
                    java.lang.Process process = runtime.exec("ping -c 1 114.114.114.114");
                    int exitValue=process.waitFor();
                    if (exitValue==0){
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                onGetNewWorkState.onSuccess();

                            }
                        });
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        onGetNewWorkState.onFailed();
                    }
                });
            }
        });

    }
   public interface OnGetNewWorkState {
        void onSuccess();
        void onFailed();
    }
}
