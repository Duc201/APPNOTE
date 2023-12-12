package com.example.project_note.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDAO {
    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT *FROM note")
    fun getListNote(): List<Note>

    @Query("SELECT *FROM note where title = :title and detail= :detail")
    fun getID(title: String, detail: String): Int //    @Query("SELECT *FROM note where  id = :id)
    //    int getNote(int id);
}