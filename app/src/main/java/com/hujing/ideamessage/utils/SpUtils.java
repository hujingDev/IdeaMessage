package com.hujing.ideamessage.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by acer on 2017/5/6.
 */

public class SpUtils {
    private  static SharedPreferences sharedPreferences;
    public static void putString(Context context,String string,String tag){
        if (sharedPreferences==null){
          sharedPreferences=  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(tag,string);
        editor.commit();
    }
    public static String getString(Context context,String tag,String defaultValue){
        if (sharedPreferences==null){
            sharedPreferences=  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sharedPreferences.getString(tag,defaultValue);
    }
}
