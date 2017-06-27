package com.hujing.ideamessage.presenter.impl;

import com.hujing.ideamessage.callback.MyEMCallBack;
import com.hujing.ideamessage.presenter.LoginPresenter;
import com.hujing.ideamessage.view.LoginView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by acer on 2017/5/3.
 */

public class LoginPresenterImpl implements LoginPresenter{
    private LoginView mLoginView;

    public LoginPresenterImpl(LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    public void myStartActivity() {
        mLoginView.startRegisterActivity();
    }

    @Override
    public void login(String name, String pwd) {
        EMClient.getInstance().login(name, pwd, new MyEMCallBack() {
            @Override
            public void success() {
                //登录成功
                mLoginView.onGetLoginState(true,0,null);
            }

            @Override
            public void failed(int code, String error) {
                //登录失败
                mLoginView.onGetLoginState(false,code,error);
            }
        });
    }

}
