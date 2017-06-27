package com.hujing.ideamessage.view;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.adapter.AddFriendAdapter;
import com.hujing.ideamessage.presenter.impl.AddFriendPresenterImpl;
import com.hujing.ideamessage.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddFriendActivity extends BaseActivity implements AddFriendView {
    private static final String TAG = "？";
    Button button;
    @BindView(R.id.tb_add)
    Toolbar toolbar;
    @BindView(R.id.rv_result)
    RecyclerView recyclerView;
    @BindView(R.id.iv_noData)
    ImageView  imageViewNoData;
    AddFriendAdapter adapter;
    List<String> queryList= new ArrayList<>();
    AddFriendPresenterImpl addFriendPresenter=new AddFriendPresenterImpl(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initToolbar();
    }

    private void setRecyclerItem() {
            adapter.setOnItemClickListener(new AddFriendAdapter.OnItemClickListener() {
                @Override
                public void clickButton(View v, String contact) {
                    Log.d(TAG, "clickButton: ");
                    button= (Button) v;
                    addFriendPresenter.addFriend(contact);
                }
            });

    }


    private void initToolbar() {
        toolbar.setTitle("搜索好友");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_friend_menu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_friend));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter==null){
                    adapter=new AddFriendAdapter(null,null);
                    LinearLayoutManager manager = new LinearLayoutManager(AddFriendActivity.this);
                    recyclerView.setLayoutManager(manager);
                    recyclerView.setAdapter(adapter);
                    setRecyclerItem();
                }
                addFriendPresenter.searchFriend(query);
                InputMethodManager inputMethodManager= (InputMethodManager)
                        getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void OnGetSearchFriendList(List<AVUser> list, List<String> contacts, boolean success, String msg) {
        if (success){
            queryList.clear();
            for (AVUser user:list){
                queryList.add(user.getUsername());
            }
            adapter.setList(queryList,contacts);
            adapter.notifyDataSetChanged();
            imageViewNoData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            ToastUtils.showToast(this,msg);
        }
    }

    @Override
    public void OnGetAddFriendSuccess(boolean isAdded, String msg) {
        if (isAdded){
            if (button!=null){
                button.setText("已发送添加请求");
                button.setEnabled(false);
            }
        }else {
            ToastUtils.showToast(AddFriendActivity.this,"添加失败:"+msg);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
