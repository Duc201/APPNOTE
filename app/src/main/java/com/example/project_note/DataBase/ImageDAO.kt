package com.example.project_note.DataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable


@Dao
interface ImageDAO {
    @Insert
    fun insertImage(image: Image) : Completable
    @Update
    fun updateImage(image: Image) : Completable

    @Delete
    fun deleteImage(image: Image) : Completable

    @Query("DELETE FROM Image WHERE path = :path")
    fun deleteImageForPath(path: String) :Completable

//    @Query("DELETE FROM Image WHERE id_Note = :idNote")
//    fun deleteImageForIdNote(idNote: Int) :Completable
    @Query("SELECT * FROM Image WHERE id_Note = :id_Note ")
    fun getListImage( id_Note: Int): MutableList<Image>
}