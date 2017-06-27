package com.hujing.ideamessage.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.presenter.impl.SplashPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity implements SplashView {
    @BindView(R.id.iv_splash)
    ImageView imageViewSplash;
    @BindView(R.id.activity_splash)
    LinearLayout splashLayout;
    private SplashPresenterImpl splashPresenter;
    private AlphaAnimation aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        splashPresenter = new SplashPresenterImpl(this);
        //设置动画
        splashPresenter.setAnim();
        //判断是否登录过
        splashPresenter.checkLogin();
        //创建数据路
        splashPresenter.initDataBase();
    }


    @Override
    public void onGetLoginState(final boolean isLogin) {

            //添加动画监听
            aa.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (isLogin){
                        //登录过的跳转
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //未登录过的跳转
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

    }

    @Override
    public void startAnim() {
        //splash的动画
        aa = new AlphaAnimation(0, 1);
        aa.setFillAfter(true);
        aa.setDuration(3000);
        splashLayout.startAnimation(aa);
    }
}
