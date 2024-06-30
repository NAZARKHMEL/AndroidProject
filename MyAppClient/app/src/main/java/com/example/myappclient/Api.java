package com.example.myappclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @GET("/notes")
    Call<List<Note>> getAllNotes();
    @GET("/notes/{id}")
    Call<Note> getNoteById(@Path("id") int noteId);
    @POST("/notes")
    Call<Note> saveNote(@Body Note note);
    @PUT("/notes")
    Call<Note> updateNote(@Body Note note);
    @DELETE("/notes/{id}")
    Call<Void> deleteNote(@Path("id") int id);

}
