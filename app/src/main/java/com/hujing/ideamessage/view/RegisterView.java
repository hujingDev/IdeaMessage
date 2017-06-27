package com.hujing.ideamessage.view;

/**
 * Created by acer on 2017/5/3.
 */

public interface RegisterView {
    /** 获取是否注册成功的状态
     * @param isSuccess 是否注册成功
     * @param registerMessage    注册返回的信息
     */
    void onGetRegisterState(boolean isSuccess,String registerMessage,String name,String pwd);
}
