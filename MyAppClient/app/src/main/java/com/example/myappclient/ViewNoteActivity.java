package com.example.myappclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewNoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnBack;
    private int noteId;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        etTitle = findViewById(R.id.tvTitle);
        etContent = findViewById(R.id.tvContent);
        btnBack = findViewById(R.id.btnBack);

        noteId = getIntent().getIntExtra("noteId", -1);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                finish();
            }
        });
        loadNote();
    }

    private void loadNote() {
        Call<Note> call = NetworkService.getInstance().getApi().getNoteById(noteId);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                if (response.isSuccessful() && response.body() != null) {
                    note = response.body();
                    etTitle.setText(note.getTitle());
                    etContent.setText(note.getContent());
                }
            }
            @Override
            public void onFailure(Call<Note> call, Throwable t) {
            }
        });
    }

    private void saveNote() {
        if (note != null) {
            note.setTitle(etTitle.getText().toString());
            note.setContent(etContent.getText().toString());

            Call<Note> call = NetworkService.getInstance().getApi().updateNote(note);
            call.enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    if (response.isSuccessful()) {
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                }
            });
        }
    }
}
