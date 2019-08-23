package com.qingshangzuo.fourthnote;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.qingshangzuo.fourthnote.adapter.MyAdapter;
import com.qingshangzuo.fourthnote.db.NoteDb;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefresh;
    private Intent intent;
    private MyAdapter adapter;
    private NoteDb noteDB;
    private Cursor cursor;
    private SQLiteDatabase dbreader;
    private ListView list;
    private LinearLayout layoutAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = this.findViewById(R.id.list);
        noteDB = new NoteDb(this);
        dbreader = noteDB.getReadableDatabase();
        layoutAdd = findViewById(R.id.layout_add);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(MainActivity.this,ShowActivity.class);
                intent.putExtra(NoteDb.ID,cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.CONTENT,cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                intent.putExtra(NoteDb.TIME,cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                startActivity(intent);
            }
        });

        layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }


    public void selectDb(){
        cursor = dbreader.query(NoteDb.TABLE_NAME,null,null,null,null,null,null);
        adapter = new MyAdapter(this,cursor);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDb();
    }
}
