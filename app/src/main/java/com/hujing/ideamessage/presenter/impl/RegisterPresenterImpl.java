package com.hujing.ideamessage.presenter.impl;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.hujing.ideamessage.presenter.RegisterPresenter;
import com.hujing.ideamessage.utils.ThreadUtils;
import com.hujing.ideamessage.view.RegisterView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;


/**
 * Created by acer on 2017/5/3.
 */

public class RegisterPresenterImpl implements RegisterPresenter {
    RegisterView registerView;

    public RegisterPresenterImpl(RegisterView registerView) {
        this.registerView = registerView;
    }

    @Override
    public void registerUser(final String name, final String pwd) {
        final AVUser user=new AVUser();
        user.setUsername(name);
        user.setPassword(pwd);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e==null){
                    //在leanCloud注册成功
                    //在子线程中，去环信注册
                   ThreadUtils.runOnNoUIThread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               //环信注册成功
                               EMClient.getInstance().createAccount(name,pwd);
                               ThreadUtils.runOnMainThread(new Runnable() {
                                   @Override
                                   public void run() {
                                       registerView.onGetRegisterState(true,null,name,pwd);
                                   }
                               });
                           } catch (HyphenateException e1) {
                               //环信注册失败
                               e1.printStackTrace();
                               try {
                                   user.delete();
                               } catch (AVException e2) {
                                   e2.printStackTrace();
                               }
                               registerView.onGetRegisterState(false,e1.getDescription(),null,null);
                           }
                       }
                   });
                }else {
                    //注册失败
                    registerView.onGetRegisterState(false,e.getMessage(),null,null);
                }
            }
        });
    }
}
