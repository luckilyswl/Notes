package com.qingshangzuo.fourthnote.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qingshangzuo.fourthnote.R;

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
        ImageView img = layout.findViewById(R.id.img);
        cursor.moveToPosition(position);
        String dbcontent = cursor.getString(cursor.getColumnIndex("content"));
        String dbtime = cursor.getString(cursor.getColumnIndex("time"));

        //TODO：添加图片
        String dbimg = cursor.getString(cursor.getColumnIndex("img"));

        content.setText(dbcontent);
        time.setText(dbtime);
        img.setImageURI(Uri.parse(dbimg));

        return layout;
    }
}
