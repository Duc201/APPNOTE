package com.example.project_note.DataBase

import android.net.Uri

data class User (val email: String, val pass: String, var name : String ?= null , var image :String ?= null ) {
}