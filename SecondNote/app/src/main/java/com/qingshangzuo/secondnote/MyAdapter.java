package com.qingshangzuo.secondnote;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private LinearLayout layout;

    public MyAdapter(Context context,Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        layout = (LinearLayout) inflater.inflate(R.layout.item,null);
        TextView content = layout.findViewById(R.id.list_content);
        TextView time = layout.findViewById(R.id.list_time);
        cursor.moveToPosition(position);
        String dbcontent = cursor.getString(cursor.getColumnIndex("content"));
        String dbtime = cursor.getString(cursor.getColumnIndex("time"));
        content.setText(dbcontent);
        time.setText(dbtime);

        return layout;
    }
}
