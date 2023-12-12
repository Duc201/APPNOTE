package com.example.project_note.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note")
data class Note(var title: String, var detail: String) : Serializable {


    @PrimaryKey(autoGenerate = true)
     var id : Int = 0

}