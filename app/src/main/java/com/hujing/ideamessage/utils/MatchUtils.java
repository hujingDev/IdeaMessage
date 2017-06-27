package com.hujing.ideamessage.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;

import com.hujing.ideamessage.constantvalues.Regex;
import com.hujing.ideamessage.R;

/**
 * Created by acer on 2017/5/3.
 */

public class MatchUtils {
    public static boolean isAccountMatchRegex(String account){

        return account.matches(Regex.ACCOUNT_REGEX);
    }
    public static boolean isPasswordMatchRegex(String pwd){
        return pwd.matches(Regex.PWD_REGEX);
    }

    /** 用来判断用户名和密码是否为空和是否合法
     * @param name 用户名输入框的输入的内容
     * @param pwd   密码输入框的输入的内容
     * @param textInputLayoutPwd 密码输入框的布局控件
     * @param textInputLayoutName   用户名输入框的布局控件
     * @param context
     * @return true为合法，false为不合法
     */
    public static boolean CheckTextIsIllegal
            (String name, String pwd,
             TextInputLayout textInputLayoutPwd,
             TextInputLayout textInputLayoutName,
             Context context){
        if (TextUtils.isEmpty(name)) {
            //用户名为空
            textInputLayoutName.setErrorEnabled(true);
            textInputLayoutName.setError(context.getString(R.string.alert_user_name_null));
            return false;
        } else {
            //用户名不为空
            textInputLayoutName.setErrorEnabled(false);
            if (!MatchUtils.isAccountMatchRegex(name)) {
                //用户名不合法
                textInputLayoutName.setErrorEnabled(true);
                textInputLayoutName.setError(context.getString(R.string.user_name_illegal));
                return false;
            }else {
                //用户名合法
                textInputLayoutName.setErrorEnabled(false);
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            //密码为空
            textInputLayoutPwd.setErrorEnabled(true);
            textInputLayoutPwd.setError(context.getString(R.string.alert_user_pwd_null));
            return false;
        } else {
            //密码不为空
            textInputLayoutPwd.setErrorEnabled(false);
            if (!MatchUtils.isPasswordMatchRegex(pwd)){
                //密码不合法
                textInputLayoutPwd.setErrorEnabled(true);
                textInputLayoutPwd.setError(context.getString(R.string.user_pwd_illegal));
                return false;
            }else {
                //密码合法
                textInputLayoutPwd.setErrorEnabled(false);
            }
        }
        return true;
    }
}
