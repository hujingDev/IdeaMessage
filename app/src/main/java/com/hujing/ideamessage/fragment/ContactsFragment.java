package com.hujing.ideamessage.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.stats.ConnectionEvent;
import com.hujing.ideamessage.R;
import com.hujing.ideamessage.adapter.ContactsAdapter;
import com.hujing.ideamessage.constantvalues.Value;
import com.hujing.ideamessage.event.ContactChangeEvent;
import com.hujing.ideamessage.presenter.impl.ContactsPresenterImpl;
import com.hujing.ideamessage.utils.ToastUtils;
import com.hujing.ideamessage.view.ChatActivity;
import com.hujing.ideamessage.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by acer on 2017/5/7.
 */

public class ContactsFragment extends BaseFragment implements ContactsFragmentView{
    private static final String TAG = "？？";
    private ContactLayout cl;
    private List<String> mContacts=new ArrayList<>();
    private ContactsAdapter adapter;
    private ContactsPresenterImpl contactsPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);
        cl = (ContactLayout) view.findViewById(R.id.cl);
        contactsPresenter = new ContactsPresenterImpl(this);
        contactsPresenter.initContacts();
        setRefresh();
        return view;
    }

    private void setRefresh() {
        cl.setOnSwipeRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contactsPresenter.updateContacts();
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnGetContactChanged(ContactChangeEvent event){
        contactsPresenter.updateContacts();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mContacts.clear();
    }

    @Override
    public void showContacts(List<String> contacts) {
        mContacts=contacts;
        adapter = new ContactsAdapter(mContacts);
        cl.setAdapter(adapter);
        adapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
            @Override
            public void click(View view, String contact) {
                Intent intent=new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(Value.CONTACT,contact);
                startActivity(intent);
            }

            @Override
            public void longClick(View view, final String contact) {
                Snackbar.make(view,"是否删除"+contact+"?",Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contactsPresenter.delContacts(contact);
                            }
                        }).show();
            }
        });
    }

    @Override
    public void updateContacts(boolean isUpdateSuccess,List<String> contacts) {
        if (isUpdateSuccess){
            mContacts.clear();
            mContacts.addAll(contacts);
            Collections.sort(mContacts, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            adapter.notifyDataSetChanged();

        }else {
            ToastUtils.showToast(getContext(),getString(R.string.updateFailed));
        }
        if (cl.getSwipeRefreshLayoutState()){
            cl.setRefresh(false);
        }
    }

    @Override
    public void OnGetDelContact(boolean isDelSuccess, String des) {
        if (isDelSuccess){
            ToastUtils.showToast(getContext(),"删除成功");
        }else {
            ToastUtils.showToast(getContext(),getString(R.string.del_falied)+des);
        }
    }

}
