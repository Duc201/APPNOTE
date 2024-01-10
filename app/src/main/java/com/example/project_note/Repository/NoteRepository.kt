package com.example.project_note.Repository

import android.util.Log
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDAO
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NoteRepository(private val noteDao: NoteDAO ) {

    fun getAllNotes(): Single<MutableList<Note>> {
                return Single.create {emitter ->
                        emitter.onSuccess(noteDao.getListNote())
                }
    }
     fun insert(note:Note) : Completable{
         return Completable.create {emitter->
             noteDao.insertNote(note)
             emitter.onComplete()
         }
     }
     fun delete(note:Note) : Completable = noteDao.deleteNote(note)
     fun update(note:Note) : Completable = noteDao.updateNote(note)

    fun getID(title: String , detail : String) : Single<Int>{
        return Single.create{emiter ->
            emiter.onSuccess(noteDao.getID(title,detail))
        }
    }

}