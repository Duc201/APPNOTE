package com.example.project_note.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDAO(): NoteDAO

    companion object {
        private const val DATABASE_NAME = "user.db"
        private lateinit var instance: NoteDatabase
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
            return instance
        }
    }
}