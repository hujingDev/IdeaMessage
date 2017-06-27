package com.hujing.ideamessage.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.presenter.impl.LoginPresenterImpl;
import com.hujing.ideamessage.utils.MatchUtils;
import com.hujing.ideamessage.utils.NetWorkUtils;
import com.hujing.ideamessage.utils.ToastUtils;
import com.hyphenate.util.NetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginView ,View.OnClickListener {
    @BindView(R.id.tv_register)
    TextView textViewRegister;
    @BindView(R.id.et_login_user_name)
    EditText editTextUserName;
    @BindView(R.id.et_login_user_pwd)
    EditText editTextUserPwd;
    @BindView(R.id.til_login_user_name)
    TextInputLayout textInputLayoutName;
    @BindView(R.id.til_login_user_pwd)
    TextInputLayout textInputLayoutPwd;
    @BindView(R.id.btn_login)
    Button buttonLogin;
    private static final String TAG = "LoginActivity";
    private LoginPresenterImpl loginPresenter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //绑定butterKnife
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenterImpl(this);
        //启动页面跳转
        loginPresenter.myStartActivity();
        //设置button的点击事件
        buttonLogin.setOnClickListener(this);
        //初试话进度对话框
        dialog = new ProgressDialog(this);
    }


    /**
     * 跳转到注册界面的方法
     */
    @Override
    public void startRegisterActivity() {
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    /**
     * @param isLoginSuccess 判断是否成功
     * @param code           返回的错误代码
     * @param errorMsg       返回的错误信息
     */
    @Override
    public void onGetLoginState(boolean isLoginSuccess, int code, String errorMsg) {
        if (isLoginSuccess){
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            ToastUtils.showToast(LoginActivity.this,getString(R.string.login_success));
            finish();
        }else {
            String chineseMsg=null;
            if (code==204){
                chineseMsg=getString(R.string.user_name_is_not_exist);
            }else if (code==202){
                chineseMsg=getString(R.string.pwd_is_wrong);
            }
            ToastUtils.showToast(LoginActivity.this,chineseMsg+" : "+errorMsg);
        }
        Log.d(TAG, "onGetLoginState: "+code);
    }

    /**
     * @param requestCode 请求码
     * @param resultCode    结果码
     * @param data 返回的信息
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            //返回注册成功的用户名和密码
            case 1:
                if (resultCode==RESULT_OK&&data!=null){
                    editTextUserName.setText(data.getStringExtra(Value.USER_NAME)) ;
                    editTextUserPwd.setText(data.getStringExtra(Value.USER_PWD));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                final String name = editTextUserName.getText().toString().trim();
                final String pwd = editTextUserPwd.getText().toString();
                boolean isLegal = MatchUtils.CheckTextIsIllegal(name, pwd,
                        textInputLayoutPwd, textInputLayoutName, this);
                if (isLegal){
                    showProgressDialog();
                   NetWorkUtils.isNetworkOnline(new NetWorkUtils.OnGetNewWorkState() {
                       @Override
                       public void onSuccess() {
                           //有网络连接

                           loginPresenter.login(name,pwd);

                       }

                       @Override
                       public void onFailed() {
                           //无网络连接

                           ToastUtils.showToast(LoginActivity.this,getString(R.string.no_network));
                           dialog.dismiss();
                       }
                   });
                }
                break;
            default:
                break;
        }
    }
    private void showProgressDialog() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle(getString(R.string.logining));
        dialog.show();
    }
}
