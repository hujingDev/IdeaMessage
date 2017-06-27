package com.hujing.ideamessage.fragment;

import java.util.List;

/**
 * Created by acer on 2017/5/8.
 */

public interface ContactsFragmentView {
    void showContacts(List<String> contacts);
    void  updateContacts(boolean isUpdateSuccess,List<String> contacts);
    void OnGetDelContact(boolean isDelSuccess,String des);
}
