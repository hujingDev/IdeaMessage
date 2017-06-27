package com.hujing.ideamessage.utils;


import android.content.Context;
import android.widget.Toast;

/**
 * Created by acer on 2017/5/3.
 */

public class ToastUtils {
    private static Toast toast;
    public  static  void showToast(Context context,String message){
        if (toast==null){
            toast=Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_SHORT);
        }else {
            toast.setText(message);
        }
        toast.show();
    }
}
