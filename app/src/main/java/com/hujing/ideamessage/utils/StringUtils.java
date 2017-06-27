package com.hujing.ideamessage.utils;

import android.text.TextUtils;

/**
 * Created by acer on 2017/5/7.
 */

public class StringUtils {
    public static String getFirstChar(String s){
        if (TextUtils.isEmpty(s)){
            return null;
        }else {
            return s.substring(0,1).toUpperCase();
        }
    }
}
