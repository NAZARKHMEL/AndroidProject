package com.example.myappclient;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private Context context;
    private List<Note> notes;
    private List<Note> notesFiltered;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
        this.notesFiltered = new ArrayList<>(notes);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesFiltered.get(position);
        holder.tvNoteTitle.setText(note.getTitle());

        try {
            Date createdAtDate = inputFormat.parse(note.getCreatedAt());
            String formattedDate = outputFormat.format(createdAtDate);
            holder.tvNoteDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvNoteDate.setText("Invalid date format");
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewNoteActivity.class);
            intent.putExtra("noteId", note.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notesFiltered.size();
    }

    public void filterNotes(String query) {
        notesFiltered.clear();

        if (query.isEmpty()) {
            notesFiltered.addAll(notes);
        }
        else {
            for (Note note : notes) {
                if (note.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        note.getContent().toLowerCase().contains(query.toLowerCase())) {
                    notesFiltered.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeNoteAtPosition(int position) {
        notes.remove(position);
        notesFiltered.remove(position);
        notifyItemRemoved(position);
    }
    public Note getNoteAtPosition(int position)
    {
        return notesFiltered.get(position);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNoteTitle, tvNoteDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNoteTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvNoteDate = itemView.findViewById(R.id.tvNoteDate);
        }
    }
}
