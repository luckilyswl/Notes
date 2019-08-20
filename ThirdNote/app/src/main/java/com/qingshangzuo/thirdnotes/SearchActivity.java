package com.qingshangzuo.thirdnotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qingshangzuo.thirdnotes.db.NoteDb;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText edtKey;
    private Button btnSearch;
    private RecyclerView rvNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();

            }
        });
    }

    private void search() {
        rvNote.setLayoutManager(new LinearLayoutManager(this));

    }
}
