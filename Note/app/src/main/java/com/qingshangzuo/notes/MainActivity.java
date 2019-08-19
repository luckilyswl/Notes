package com.qingshangzuo.notes;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.qingshangzuo.notes.db.Note;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton create;
    ArrayAdapter<String> adapter;
    List<Note> noteList = new ArrayList<>();
    List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        create = findViewById(R.id.create);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = dataList.get(position);
                Intent intent = new Intent(MainActivity.this,LookNote.class);
                intent.putExtra("key",content);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WriteNote.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,dataList);
        noteList = LitePal.findAll(Note.class);
        if(noteList.size() > 0){
            dataList.clear();
            for(Note note:noteList){
                dataList.add(note.getWriteContent());
            }
        }
        listView.setAdapter(adapter);
    }
}
