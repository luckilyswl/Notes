package com.qingshangzuo.fourthnote;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.qingshangzuo.fourthnote.db.NoteDb;

import static com.qingshangzuo.fourthnote.R.drawable.ic_back;
import static com.qingshangzuo.fourthnote.db.NoteDb.IMAGE;

public class ShowActivity extends AppCompatActivity {

    private ImageView ivBack,ivDelete;
    private TextView mTextview;
    private TextView time;
    private ImageView imageView;
    private VideoView videoView;
    private NoteDb mDb;
    private SQLiteDatabase mSql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        imageView = findViewById(R.id.imageview);
        videoView = findViewById(R.id.videoview);
        mTextview = findViewById(R.id.tv_showText);
        time = findViewById(R.id.tv_showTime);
        mDb = new NoteDb(this);
        mSql = mDb.getWritableDatabase();
        mTextview.setText(getIntent().getStringExtra(NoteDb.CONTENT));
        time.setText(getIntent().getStringExtra(NoteDb.TIME));
        

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ivDelete = findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getIntent().getIntExtra(NoteDb.ID,0);
                mSql.delete(NoteDb.TABLE_NAME," _id = " + id,null);
                finish();
            }
        });
    }

    public void goBack(View v) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
