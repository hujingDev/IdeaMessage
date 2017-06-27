package com.hujing.ideamessage.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by acer on 2017/4/30.
 */

public class ThreadUtils {
    private static Handler handler=new Handler(Looper.getMainLooper());
    //单线程的线程池
    private static Executor executor= Executors.newSingleThreadExecutor();

    /**在子线程运行
     * @param runnable
     */
    public static void runOnNoUIThread(final  Runnable runnable){
        executor.execute(runnable);
    }

    /**在主线程运行
     * @param runnable
     */
    public static  void runOnMainThread(Runnable runnable){
        handler.post(runnable);
    }
}
