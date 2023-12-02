package com.example.project_note.DataBase;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "note")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String detail;

    public Note( String title, String detail) {
        this.title = title;
        this.detail = detail;
    }

//    public Note(int id, String title, String detail) {
//        this.id = id;
//        this.title = title;
//        this.detail = detail;
//    }
    //    protected Note(Parcel in) {
//        id = in.readInt();
//        title = in.readString();
//        detail = in.readString();
//    }

//    public static final Creator<Note> CREATOR = new Creator<Note>() {
//        @Override
//        public Note createFromParcel(Parcel in) {
//            return new Note(in);
//        }
//
//        @Override
//        public Note[] newArray(int size) {
//            return new Note[size];
//        }
//    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(@NonNull Parcel dest, int flags) {
//        dest.writeString(title);
//        dest.writeString(detail);
//    }
}
