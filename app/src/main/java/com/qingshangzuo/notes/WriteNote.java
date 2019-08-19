package com.qingshangzuo.notes;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qingshangzuo.notes.db.Note;

public class WriteNote extends AppCompatActivity {

    EditText input;
    FloatingActionButton save;
    String content;
    Note note = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        input = findViewById(R.id.input);
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = input.getText().toString();
                note.setWriteContent(content);
                note.save();
                Toast.makeText(WriteNote.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
