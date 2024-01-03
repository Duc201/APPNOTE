package com.example.project_note.DataBase

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity
data class Note(
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "detail")
    var detail: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_Note")
    var idNote : Int = 0) : Serializable{

}
@Entity
data class Image(
    var path: String,
    @ColumnInfo(name = "id_Note")
    var noteCreatorId : Int,
    @PrimaryKey(autoGenerate = true)
    var idImage: Int = 0){
}



