package com.hujing.ideamessage.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.presenter.impl.RegisterPresenterImpl;
import com.hujing.ideamessage.utils.MatchUtils;
import com.hujing.ideamessage.utils.NetWorkUtils;
import com.hujing.ideamessage.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements RegisterView, View.OnClickListener {
    @BindView(R.id.btn_register)
    Button buttonRegister;
    @BindView(R.id.et_register_user_name)
    EditText editTextRegisterUserName;
    @BindView(R.id.et_register_user_pwd)
    EditText editTextRegisterPwd;
    @BindView(R.id.til_register_user_pwd)
    TextInputLayout textInputLayoutPwd;
    @BindView(R.id.til_register_user_name)
    TextInputLayout textInputLayoutName;
    private RegisterPresenterImpl registerPresenter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerPresenter = new RegisterPresenterImpl(this);
        ButterKnife.bind(this);
        //设置点击事件
        setOnClickListener();
        //初始化进度对话框
        dialog = new ProgressDialog(this);
    }

    private void setOnClickListener() {
        buttonRegister.setOnClickListener(this);
    }


    @Override
    public void onGetRegisterState(boolean isSuccess, String registerMessage,String name,String pwd) {

        if (!TextUtils.isEmpty(registerMessage)&&!isSuccess) {
           ToastUtils.showToast(RegisterActivity.this,getString(R.string.user_name_exist));
        }
        if (isSuccess){
            ToastUtils.showToast(RegisterActivity.this,getString(R.string.register_success));
            Intent intent=new Intent();
            intent.putExtra(Value.USER_NAME,name);
            intent.putExtra(Value.USER_PWD,pwd);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                final String name = editTextRegisterUserName.getText().toString().trim();
                final String pwd = editTextRegisterPwd.getText().toString();
                //判断用户名和密码是否合法
                boolean isLegal = MatchUtils.CheckTextIsIllegal(name, pwd,
                        textInputLayoutPwd, textInputLayoutName, RegisterActivity.this);
                if (isLegal){
                    showProgressDialog();
                    NetWorkUtils.isNetworkOnline(new NetWorkUtils.OnGetNewWorkState() {
                        @Override
                        public void onSuccess() {

                            //有网络连接
                            registerPresenter.registerUser(name, pwd);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailed() {
                            //无网络连接
                            dialog.dismiss();
                            ToastUtils.showToast(RegisterActivity.this,getString(R.string.no_network));
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
        dialog.setTitle(getString(R.string.registering));
        dialog.show();
    }

}
