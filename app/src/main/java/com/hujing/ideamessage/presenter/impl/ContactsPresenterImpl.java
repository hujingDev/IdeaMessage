package com.hujing.ideamessage.presenter.impl;


import com.hujing.ideamessage.fragment.ContactsFragmentView;
import com.hujing.ideamessage.presenter.ContactsPresenter;
import com.hujing.ideamessage.utils.DBUtils;
import com.hujing.ideamessage.utils.ThreadUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/5/8.
 */

public class ContactsPresenterImpl implements ContactsPresenter {
    private ContactsFragmentView contactsfragmentview;
    public ContactsPresenterImpl(ContactsFragmentView contactsfragmentview) {
        this.contactsfragmentview=contactsfragmentview;
    }

    @Override
    public void initContacts() {
        List<String> contacts=new ArrayList<>();
         contacts= DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
        contactsfragmentview.showContacts(contacts);
        getContactsFromServer();
    }

    @Override
    public void updateContacts() {
        getContactsFromServer();
    }

    @Override
    public void delContacts(final String contact) {
        ThreadUtils.runOnNoUIThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(contact);
                    DBUtils.delContact(contact,EMClient.getInstance().getCurrentUser());
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsfragmentview.OnGetDelContact(true,null);
                        }
                    });
                } catch (final HyphenateException e) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            contactsfragmentview.OnGetDelContact(false,e.getDescription());
                        }
                    });
                    e.printStackTrace();
                }
            }
        });

    }

    public void getContactsFromServer() {

            ThreadUtils.runOnNoUIThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final List<String> contacts= EMClient.getInstance().
                                contactManager().getAllContactsFromServer();
                        if (!contacts.isEmpty()) {
                            DBUtils.saveOrUpdate(EMClient.getInstance().getCurrentUser(), contacts);
                        }
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                contactsfragmentview.updateContacts(true,contacts);
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        ThreadUtils.runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                contactsfragmentview.updateContacts(false,null);
                            }
                        });
                    }
                }
            });
    }
}
