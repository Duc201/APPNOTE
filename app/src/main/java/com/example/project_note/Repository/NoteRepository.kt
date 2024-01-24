package com.example.project_note.Repository

import android.util.Log
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDAO
import com.example.project_note.DataBase.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoteRepository(private val noteDao: NoteDAO ) {
    private lateinit var database: DatabaseReference

    init {
        database = Firebase.database.reference
    }

    fun getAllNotes(): Single<MutableList<Note>> {
                return Single.create {emitter ->
                        emitter.onSuccess(noteDao.getListNote())
                }
    }
     fun insert(note:Note) : Single<Boolean>{
         return Single.fromCallable {
             val insertResult = noteDao.insertNote(note)
             return@fromCallable insertResult > 0
         }
     }

    fun delete(note:Note) : Completable = noteDao.deleteNote(note)
     fun update(note:Note) : Completable = noteDao.updateNote(note)

    fun getID(title: String , detail : String) : Single<Int>{
        return Single.create{emiter ->
            emiter.onSuccess(noteDao.getID(title,detail))
        }
    }

    fun insertToRealTime(note: Note) : Completable{
        return Completable.create{emitter->
            val userid = Firebase.auth.currentUser?.uid
            database.child("users").child(userid.toString()).child("listnote").child("${note.idNote}").setValue(note)
                .addOnSuccessListener{
                    emitter.onComplete()
                }
                .addOnFailureListener {

                }
        }
    }
    fun deleteToRealTime(note: Note) : Completable{
        return Completable.create{emitter->
            val userid = Firebase.auth.currentUser?.uid
            database.child("users").child(userid.toString()).child("listnote").child("${note.idNote}").removeValue()
                .addOnSuccessListener{
                    emitter.onComplete()
                }
                .addOnFailureListener {

                }
        }
    }
//    fun insertRealtime(note:Note) : Completable {
//
//        return Completable.create { emitter->
//            database.ref.child()
//        }
//    }

}