package com.example.project_note.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class, Image::class], version = 1)
//@TypeConverters(Converters::class)

abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDAO(): NoteDAO
    abstract fun ImageDAO(): ImageDAO


    companion object {
        private val DATABASE_NAME = "user.db"
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}