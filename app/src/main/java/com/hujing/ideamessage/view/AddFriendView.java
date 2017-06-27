package com.hujing.ideamessage.view;

import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by acer on 2017/5/10.
 */

public interface AddFriendView {
    void OnGetSearchFriendList(List<AVUser> list,List<String> contacts,boolean success,String msg);
    void OnGetAddFriendSuccess(boolean isAdded,String msg);
}
