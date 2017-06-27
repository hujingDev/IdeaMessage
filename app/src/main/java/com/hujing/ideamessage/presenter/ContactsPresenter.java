package com.hujing.ideamessage.presenter;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by acer on 2017/5/8.
 */

public interface ContactsPresenter {

    void initContacts();
    void updateContacts();
    void delContacts(String contact);
}
