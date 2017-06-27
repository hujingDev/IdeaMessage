package com.hujing.ideamessage.presenter.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.hujing.ideamessage.presenter.AddFriendPresenter;
import com.hujing.ideamessage.utils.DBUtils;
import com.hujing.ideamessage.utils.ThreadUtils;
import com.hujing.ideamessage.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/5/10.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {
    private static final String TAG = "AddFriendPresenterImpl";
    AddFriendView addFriendView;
    List<String> contacts;
    public AddFriendPresenterImpl(AddFriendView addFriendView) {
        this.addFriendView=addFriendView;
    }

    @Override
    public void searchFriend(String keyWord) {
        final String currentUser = EMClient.getInstance().getCurrentUser();
        final AVQuery<AVUser> query=new AVQuery<>("_User");
        query.whereStartsWith("username",keyWord)
                .whereNotEqualTo("username",currentUser)
                .findInBackground(new FindCallback<AVUser>() {
                    @Override
                    public void done(final List<AVUser> list, AVException e) {
                       if (list!=null&&e==null&&list.size()>0){

                          ThreadUtils.runOnNoUIThread(new Runnable() {
                              @Override
                              public void run() {
                                  try {
                                    contacts=  EMClient.getInstance().contactManager().getAllContactsFromServer();
                                      ThreadUtils.runOnMainThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              addFriendView.OnGetSearchFriendList(list,contacts,true,null);
                                          }
                                      });
                                  } catch (HyphenateException e1) {
                                      addFriendView.OnGetSearchFriendList(null,null,false,"查询失败:"+e1.getMessage());
                                      e1.printStackTrace();
                                  }
                              }
                          });


                       }else if (e==null){
                           addFriendView.OnGetSearchFriendList(null,null,false,"无匹配用户");
                       }else if (e!=null){
                           addFriendView.OnGetSearchFriendList(null,null,false,"查询失败:"+e.getMessage());
                       }
                    }
                });
    }

    @Override
    public void addFriend(final String userName) {
        ThreadUtils.runOnNoUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().addContact(userName,"加个好友吧！");
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            addFriendView.OnGetAddFriendSuccess(true,null);
                        }
                    });
                } catch (HyphenateException e) {
                    addFriendView.OnGetAddFriendSuccess(false,e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
