package com.hujing.ideamessage.utils;

import android.util.Log;

import com.hujing.ideamessage.bean.Friend;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by acer on 2017/5/8.
 */

public class DBUtils {
    public static List<String> getContacts(String user) {
        List<String> contacts = new ArrayList<>();
        List<Friend> friendList = DataSupport.where("user=?", user).find(Friend.class);
        if (friendList.size() != 0 && friendList != null) {
            for (Friend friend : friendList) {
                contacts.add(friend.getFriend());
            }
            Collections.sort(contacts, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
        }
        return contacts;
    }

    public static void saveOrUpdate(String user, List<String> contacts) {
        int i = DataSupport.deleteAll(Friend.class);
        for (String s:contacts){
            Friend friend=new Friend();
            friend.setUser(user);
            friend.setFriend(s);
            friend.save();
        }
     /*   if (friends.isEmpty()){

            for (String s : contacts) {
                friend.setUser(user);
                friend.setFriend(s);
                friend.save();
            }
        }else {
            for (String s : contacts) {
                for (Friend existFriend:friends){
                    if ((user.equals(existFriend.getUser())&&s.equals(existFriend.getFriend()))){

                    }else {
                        friend.setUser(user);
                        friend.setFriend(s);
                        friend.save();
                    }
                }
            }
        }*/

    }
    public static void delContact(String contact,String currentUser){
        DataSupport.where("friend=?",contact,"user=?",currentUser);
    }
}
