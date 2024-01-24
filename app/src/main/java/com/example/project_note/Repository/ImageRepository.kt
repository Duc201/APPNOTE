package com.example.project_note.Repository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.ImageDAO
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Locale

class ImageRepository(private val imageDao: ImageDAO) {

    private  var database: DatabaseReference = Firebase.database.reference






    fun insert(image: Image) : Completable
    {
        return imageDao.insertImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { Log.d("AAA", "vào đây rồi")}
    }

    fun update(image: Image) : Completable
    {
        return imageDao.updateImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { Log.d("AAA", "vào đây rồi")}
    }
    fun delete(image: Image) : Completable
    {
        return imageDao.deleteImage(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun getAllImages( idNote: Int): Single<MutableList<Image>> {
        return Single.create {emitter ->
            run {
                emitter.onSuccess(imageDao.getListImage(idNote))
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    fun addImageToRealTime(image: Image) : Single<Boolean>{
        return Single.create { emittor->
            val userid = Firebase.auth.currentUser?.uid
//            database.child("users").child(userid.toString()).child("listImage").child("${image.noteCreatorId}").push().setValue(image)
            database.child("users").child(userid.toString()).child("listImage").child("${image.noteCreatorId}").child("${image.idImage}").setValue(image)
                .addOnSuccessListener {
                    emittor.onSuccess(true)
                }
                .addOnFailureListener {
                    // Write failed
                    // ...
                }
        }
    }
    fun deleteImageRT(image: Image): Completable {
        return Completable.create { emitter ->
            val userId = Firebase.auth.currentUser?.uid

          database.child("users").child(userId.toString()).child("listImage").child("${image.noteCreatorId}").child("${image.idImage}").removeValue()
              .addOnSuccessListener {
                  emitter.onComplete()
              }


        }
    }
fun deleteImageSTFB(image:Image):Completable{
    return Completable.create { emitter ->
        val storageReference = FirebaseStorage.getInstance()
        val imageRef = storageReference.getReferenceFromUrl(image.pathFB)

        imageRef.delete().addOnSuccessListener {
            emitter.onComplete()
        }.addOnFailureListener {
            emitter.onError(it)
        }

    }
}
    fun uploadImageToFirebaseStorage(file: Uri) : Single<Uri>{
        return Single.create {emitter->
            val imageName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())+".jpg"
            val storageRef = Firebase.storage.reference
            var imageRef = storageRef.child("Noteimages").child(imageName)

            val uploadTask = imageRef.putFile(file)

            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    emitter.onSuccess(it)
                    Log.d("CHeck",it.toString())
                }.addOnFailureListener{
                    emitter.onError(it)
                }
            }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }

    }
}