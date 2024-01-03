package com.example.project_note.DataBase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NoteDAO {
    @Insert
    fun insertNote(note: Note) : Completable

    @Update
    fun updateNote(note: Note) : Completable
    @Delete
    fun deleteNote(note: Note) : Completable

    @Query("SELECT * FROM Note")
    fun getListNote(): MutableList<Note>

    @Query("SELECT id_Note FROM Note WHERE title = :title AND detail= :detail")
    fun getID(title: String, detail: String): Int
}