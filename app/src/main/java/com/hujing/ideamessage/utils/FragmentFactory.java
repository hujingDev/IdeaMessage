package com.hujing.ideamessage.utils;

import com.hujing.ideamessage.fragment.BaseFragment;
import com.hujing.ideamessage.fragment.ContactsFragment;
import com.hujing.ideamessage.fragment.ExitFragment;
import com.hujing.ideamessage.fragment.MsgFragment;

/**
 * Created by acer on 2017/5/7.
 */

public class FragmentFactory {
    static ContactsFragment contactsFragment;
    static MsgFragment msgFragment;
    static ExitFragment exitFragment;
    public static BaseFragment getFragment(int position){
        BaseFragment fragment=null;
        switch (position){
            case 0:
                if (msgFragment==null){
                    msgFragment=new MsgFragment();
                }
                fragment=msgFragment;
                break;
            case 1:
                if (contactsFragment==null){
                    contactsFragment=new ContactsFragment();
                }
                fragment=contactsFragment;
                break;
            case 2:
                if (exitFragment==null){
                    exitFragment=new ExitFragment();
                }
                fragment=exitFragment;
                break;
        }
        return fragment;
    }
}
