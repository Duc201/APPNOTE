package com.example.project_note.Repository

import com.example.project_note.DataBase.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import io.reactivex.Completable

class AuthResponsitory {
    private lateinit var database: DatabaseReference

    init {
        database = Firebase.database.reference
    }
    fun creatPathUser(user: User) : Completable {
        return Completable.create { emitor->
            val userid = Firebase.auth.currentUser?.uid
            database.child("users").child(userid.toString()).child("email").setValue(user.email)
            database.child("users").child(userid.toString()).child("pass").setValue(user.pass)
                .addOnSuccessListener{
                    emitor.onComplete()
                }
                .addOnFailureListener {

                }
        }
    }

}