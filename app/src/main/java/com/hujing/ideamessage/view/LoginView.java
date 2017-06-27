package com.hujing.ideamessage.view;

/**
 * Created by acer on 2017/4/30.
 */

public interface LoginView {
   //跳转到注册页面
   void startRegisterActivity();

   /**获取是否登录成功
    * @param isLoginSuccess 判断是否成功
    * @param code 返回的错误代码
    * @param errorMsg 返回的错误信息
    */
   void onGetLoginState(boolean isLoginSuccess,int code,String errorMsg);
}
