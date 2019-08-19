package com.qingshangzuo.secondnote;

import android.content.Intent;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ListView list;
    private Intent intent;
    private MyAdapter adapter;
    private NoteDb noteDB;
    private Cursor cursor;
    private SQLiteDatabase dbreader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = this.findViewById(R.id.list);
        noteDB = new NoteDb(this);
        dbreader = noteDB.getReadableDatabase();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent intent = new Intent(MainActivity.this,ShowContent.class);
                intent.putExtra(NoteDb.ID,cursor.getInt(cursor.getColumnIndex(NoteDb.ID)));
                intent.putExtra(NoteDb.CONTENT,cursor.getString(cursor.getColumnIndex(NoteDb.CONTENT)));
                intent.putExtra(NoteDb.TIME,cursor.getString(cursor.getColumnIndex(NoteDb.TIME)));
                startActivity(intent);
            }
        });
    }

    public void add(View v){
        intent = new Intent(MainActivity.this,AddContent.class);
        startActivity(intent);
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
