package com.hujing.ideamessage.presenter.impl;

import com.hujing.ideamessage.presenter.SplashPresenter;
import com.hujing.ideamessage.view.SplashView;
import com.hyphenate.chat.EMClient;

import org.litepal.tablemanager.Connector;

/**
 * Created by acer on 2017/4/30.
 */

public class SplashPresenterImpl implements SplashPresenter {
    SplashView mSplashView;

    public SplashPresenterImpl(SplashView mSplashView) {
        this.mSplashView = mSplashView;
    }

    @Override
    public void checkLogin() {
        if (EMClient.getInstance().isLoggedInBefore()&&EMClient.getInstance().isLoggedInBefore()){
            //以前登录过，并且连接上了环信服务器
            mSplashView.onGetLoginState(true);
        }else {
            //以前没有登录过
            mSplashView.onGetLoginState(false);
        }
    }

    @Override
    public void setAnim() {
        mSplashView.startAnim();
    }

    @Override
    public void initDataBase() {
        Connector.getDatabase();
    }
}
