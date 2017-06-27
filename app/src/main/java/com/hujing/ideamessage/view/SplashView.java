package com.hujing.ideamessage.view;

/**
 * Created by acer on 2017/4/30.
 */

public interface SplashView {
    /**
     * 获取了登录状态后的，界面跳转逻辑
     * @param isLogin 是否登录了
     */
    void onGetLoginState(boolean isLogin);
    void startAnim();
}
