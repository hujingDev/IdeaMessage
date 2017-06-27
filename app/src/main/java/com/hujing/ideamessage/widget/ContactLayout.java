package com.hujing.ideamessage.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hujing.ideamessage.R;

/**
 * Created by acer on 2017/5/7.
 */

public class ContactLayout extends RelativeLayout {

    private RecyclerView recyclerView;
    private TextView textView;
    private SlidBar slidBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ContactLayout(Context context) {
        super(context);
    }

    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.contact_layout, this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_contacts);
        textView = (TextView) findViewById(R.id.tv_float);
        slidBar = (SlidBar) findViewById(R.id.sb);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl);
    }

    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setAdapter(RecyclerView.Adapter adapter){
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    public void setOnSwipeRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        swipeRefreshLayout.setOnRefreshListener(listener);
    }
    public void setRefresh(boolean isRefresh){
        swipeRefreshLayout.setRefreshing(isRefresh);
    }
    public boolean getSwipeRefreshLayoutState(){
        return swipeRefreshLayout.isRefreshing();
    }
}
