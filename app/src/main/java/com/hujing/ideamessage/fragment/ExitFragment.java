package com.hujing.ideamessage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hujing.ideamessage.callback.MyEMCallBack;
import com.hujing.ideamessage.utils.ToastUtils;
import com.hujing.ideamessage.view.LoginActivity;
import com.hyphenate.chat.EMClient;

/**
 * Created by acer on 2017/5/7.
 */

public class ExitFragment extends BaseFragment {

    private Button button;
    private ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exit_fragment, container, false);
        button = (Button) view.findViewById(R.id.btn_exit);
        dialog = new ProgressDialog(getContext());
        setButton();
        return view;

    }

    private void setButton() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                EMClient.getInstance().logout(true, new MyEMCallBack() {
                    @Override
                    public void success() {
                        dialog.dismiss();
                        Intent intent=new Intent(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void failed(int code, String error) {
                        ToastUtils.showToast(getContext(),"退出失败，请检查网络连接");
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void showDialog() {

        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("退出中");
        dialog.show();
    }
}
