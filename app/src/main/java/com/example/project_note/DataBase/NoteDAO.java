package com.example.project_note.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface NoteDAO {

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);
    @Delete
    void deleteNote(Note note);
    @Query("SELECT *FROM note")
    List<Note> getListNote();
    @Query("SELECT *FROM note where title = :title and detail= :detail")
    int getID(String title, String detail);
//    @Query("SELECT *FROM note where  id = :id)
//    int getNote(int id);
}
