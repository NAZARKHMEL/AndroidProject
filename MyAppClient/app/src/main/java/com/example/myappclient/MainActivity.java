package com.example.myappclient;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_NOTE = 1;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private EditText etSearch;
    private List<Note> allNotes = new ArrayList<>();
    private NoteAdapter adapter;
    private String searchQuery = "";
    private Button btnLogout;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);
        etSearch = findViewById(R.id.etSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchQuery = charSequence.toString();
                if (adapter != null) {
                    adapter.filterNotes(searchQuery);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        loadNotes();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note noteToDelete = adapter.getNoteAtPosition(position);
                deleteNote(noteToDelete.getId(), position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        userManager = new UserManager(this);

        btnLogout.setOnClickListener(v -> {
            userManager.logoutUser();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadNotes() {
        Call<List<Note>> call = NetworkService.getInstance().getApi().getAllNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allNotes = response.body();

                    Collections.reverse(allNotes);
                    adapter = new NoteAdapter(MainActivity.this, allNotes);
                    recyclerView.setAdapter(adapter);
                    if (!searchQuery.isEmpty()) {
                        adapter.filterNotes(searchQuery);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
            }
        });
    }

    private void deleteNote(int noteId, int position) {
        Call<Void> call = NetworkService.getInstance().getApi().deleteNote(noteId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.removeNoteAtPosition(position);
                } else {
                    adapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                adapter.notifyItemChanged(position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_NOTE && resultCode == RESULT_OK && data != null) {
            Note updatedNote = data.getParcelableExtra("note");
            if (updatedNote != null) {
                updateNoteInList(updatedNote);
            }
        }
    }

    private void updateNoteInList(Note updatedNote) {
        for (int i = 0; i < allNotes.size(); i++) {
            if (allNotes.get(i).getId() == updatedNote.getId()) {
                allNotes.set(i, updatedNote);
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchQuery", searchQuery);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchQuery = savedInstanceState.getString("searchQuery", "");
        etSearch.setText(searchQuery);
        if (adapter != null) {
            adapter.filterNotes(searchQuery);
        }
    }
}
