package com.example.project_note.Repository

import android.util.Log
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.ImageDAO
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageRepository(private val imageDao: ImageDAO) {


    fun getAllImages( idNote: Int): Single<MutableList<Image>> {
        return Single.create {emitter ->
            run {
                emitter.onSuccess(imageDao.getListImage(idNote))
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }



    fun insert(image: Image) : Completable
    {
        return imageDao.insertImage(image)
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

}