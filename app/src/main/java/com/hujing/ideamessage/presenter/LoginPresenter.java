package com.hujing.ideamessage.presenter;

import com.hyphenate.EMCallBack;

/**
 * Created by acer on 2017/5/3.
 */

public interface LoginPresenter {
    void myStartActivity();

    /**     登录的方法
     * @param name 用户名
     * @param pwd   密码
     */
    void login(String name, String pwd);
}
