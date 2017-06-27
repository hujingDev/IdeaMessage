package com.hujing.ideamessage.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hujing.ideamessage.R;
import com.hujing.ideamessage.adapter.ContactsAdapter;
import com.hujing.ideamessage.utils.StringUtils;

import java.util.List;

/**
 * Created by acer on 2017/5/7.
 */

public class SlidBar extends View {
    private String[] selections=new String[]{"A","B","C","D","E","F","G","H","I","J","K",
            "L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private Paint paint;
    private int height;
    private int x;
    private TextView textView;
    private RecyclerView recyclerView;
    private ContactsAdapter adapter;

    public SlidBar(Context context) {
        super(context,null);
    }

    public SlidBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        x = w/2;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.GRAY);
        paint.setTextSize(50);
    }

    public SlidBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0;i<selections.length;i++){
            canvas.drawText(selections[i],x,height/selections.length*(i+1),paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        String startChar;
        ViewGroup parent = (ViewGroup) getParent();
        recyclerView = (RecyclerView) parent.findViewById(R.id.rv_contacts);
        textView= (TextView) parent.findViewById(R.id.tv_float);
        adapter = (ContactsAdapter) recyclerView.getAdapter();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.color.colorGray);
                textView.setVisibility(VISIBLE);
                startChar=selections[getIndex(event.getY())];
                textView.setText(startChar);
                scrollRecyclerView(startChar);
                break;
            case MotionEvent.ACTION_MOVE:
                startChar=selections[getIndex(event.getY())];
                textView.setText(startChar);
                scrollRecyclerView(startChar);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                textView.setVisibility(GONE);
                break;
        }
        return true;
    }

    private void scrollRecyclerView(String startChar) {
        List<String> contacts = adapter.getContacts();
        if (contacts!=null&&contacts.size()>0){
            for (int i=0;i<contacts.size();i++){
                if (StringUtils.getFirstChar(contacts.get(i)).equals(startChar)){
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        }
    }

    public int getIndex(float y) {
        int textHeight=height/selections.length;
        int result= (int) (y/textHeight);
        return result<0?0:result>selections.length-1?selections.length-1:result;
    }
}
