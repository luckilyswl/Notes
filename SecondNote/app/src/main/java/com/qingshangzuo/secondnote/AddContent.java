package com.qingshangzuo.secondnote;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContent extends AppCompatActivity {

    private EditText mEt;
    private NoteDb mDb;
    private SQLiteDatabase mSqldb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        mEt = findViewById(R.id.edt_text);
        mDb = new NoteDb(this);
        mSqldb = mDb.getWritableDatabase();
    }

    public void save(View v){
        DbAdd();
        finish();
    }

    public void cancle(View v) {
        mEt.setText("");
        finish();
    }

    public void DbAdd() {
        ContentValues cv = new ContentValues();
        cv.put(NoteDb.CONTENT,mEt.getText().toString());
        cv.put(NoteDb.TIME,getTime());
        mSqldb.insert(NoteDb.TABLE_NAME,null,cv);
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date();
        String str = sdf.format(date);
        return str;

    }
}
