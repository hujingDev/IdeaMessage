package com.hujing.ideamessage.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.fragment.BaseFragment;
import com.hujing.ideamessage.utils.FragmentFactory;
import com.hujing.ideamessage.utils.NetWorkUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tb)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tvTitle;
    @BindView(R.id.bnb)
    BottomNavigationBar bnb;
    private String[] titles;
    private FragmentManager fragmentManager;
    public BadgeItem badgeItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        initFragment();
        setBottomNavigationBar();
    }

    private void initFragment() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() != 0) {
            for (Fragment fragment : fragments) {
                transaction.remove(fragment);
            }
        }
        transaction.add(R.id.fl, FragmentFactory.getFragment(0), 0 + "").commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshBadgeItem(List<EMMessage> messages){
        updateBadgeItem();
    }
    private void setBottomNavigationBar() {
        BottomNavigationItem conversationItem =
                new BottomNavigationItem(R.drawable.msg, titles[0]);
        badgeItem = new BadgeItem();
        updateBadgeItem();
        conversationItem.setBadgeItem(badgeItem);
        bnb.addItem(conversationItem);
        BottomNavigationItem item = new BottomNavigationItem(R.drawable.contacts, titles[1]);
        bnb.addItem(item);
        item = new BottomNavigationItem(R.drawable.exit, titles[2]);
        bnb.addItem(item);
        bnb.setInActiveColor(R.color.colorBlue);
        bnb.setBarBackgroundColor(android.R.color.holo_blue_dark);
        bnb.setActiveColor(android.R.color.white);
        bnb.initialise();
        bnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {



            @Override
            public void onTabSelected(int position) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if (fragment.isAdded()) {
                    transaction.show(fragment).commit();
                } else {
                    transaction.add(R.id.fl, fragment, position + "").commit();
                }
                tvTitle.setText(titles[position]);
            }

            @Override
            public void onTabUnselected(int position) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                BaseFragment fragment = FragmentFactory.getFragment(position);
                transaction.hide(fragment).commit();
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    public void updateBadgeItem() {
        int unreadMessageCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (unreadMessageCount==0){
            badgeItem.hide();
        } else if (unreadMessageCount>99) {
            badgeItem.show();
            badgeItem.setText("99+");
        }else {
            badgeItem.show();
            badgeItem.setText(unreadMessageCount+"");
        }
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window=getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initToolbar() {
        titles = new String[]{getString(R.string.msg)
                , getString(R.string.contacts)
                , getString(R.string.exit)};
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        tvTitle.setText(titles[0]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent=new Intent(this,AddFriendActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        MenuBuilder menuBuilder= (MenuBuilder) menu;
        menuBuilder.setOptionalIconsVisible(true);
        return super.onCreateOptionsMenu(menu);
    }
}
