package com.example.project_note.ViewModal

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.Repository.ImageRepository
import com.example.project_note.Repository.NoteRepository
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class ViewModalImage(application: Application) : AndroidViewModel(application){

    private var _mListImage = MutableLiveData<MutableList<Image>>()
    public val mListImage: LiveData<MutableList<Image>> get() = _mListImage

    private var _mListImageDelete : MutableList<Image> = mutableListOf()

    private val _selectedImage = MutableLiveData<Image>()
    public val selectImage: LiveData<Image> get() = _selectedImage


    val repository: ImageRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        val dao = NoteDatabase.getInstance(application).ImageDAO()
        repository = ImageRepository(dao)
    }

    public fun updateSelectImage(image : Image){
        _selectedImage.value = image
    }

    // Hàm này nhằm để gán _mListImageDElete = null
    public fun setListImageDeleteNull(){
        _mListImageDelete = mutableListOf()
    }
    public fun getListImage(idNote :Int) {
        compositeDisposable.add(
            repository.getAllImages(idNote)
                .subscribe({ lists ->
                            _mListImage.value=lists
                           Log.d("1.2","getListImage")},
                    { throwable -> })
        )
    }
    public fun addImage(image: Image) {
        compositeDisposable.add(
            repository.insert(image)
                .subscribe(
                    { Log.d("AAA", "Insert Image success") },
                    { throwable -> Log.e("AAA", "Insert error: ${throwable.message}") }
                )
        )
    }
    public fun addImageViewModal(image: Image){
        var mlist = _mListImage.value
        mlist?.add(image)
         _mListImage.value= mlist
    }
    public fun InserImagetoDB(){
        for(image in _mListImage.value!!){
            if(image.idImage == 0)
            addImage(image)
        }
    }

    fun deleteImageDB(image : Image){
        compositeDisposable.add(
            repository.delete(image)
                .subscribe(
                    {Log.d("viemmodal", "Delete Image success")},
                    { throwable -> Log.e("AAA", "Update error: ${throwable.message}") }
                )
        )
    }

    fun deleImageViewModel(image : Image){
        var mlist = _mListImage.value
        mlist?.remove(image)
        _mListImage.value = mlist

    }


    fun deleteInternalStorge(image: Image){
        val imageToDelete = File(image.path)
        //Kiem tra xe anh co ton tai khong
        if(imageToDelete.exists()){
            // Xóa đường dẫn trong Internal storge
            val isDeleted = imageToDelete.delete()
            // Xóa đối tượng
            if(isDeleted){
                Log.d("File","Xoa thanh cong")
            }
            else
                Log.d("File","Khong the xoa file")
        }
        else
            Log.d("File","Flie khong ton tai")
    }

    fun addImageToListDelete(image: Image){
        _mListImageDelete.add(image)
    }
    fun deleteImageInlistDeleteInDB(){
        for(image in _mListImageDelete)
        {
            deleteInternalStorge(image)
            deleteImageDB(image)
        }
    }

    fun deleteImageInlistInDB(){
        for(image in _mListImage.value!!)
        {
            deleteInternalStorge(image)
            deleteImageDB(image)
        }
    }

    public fun updateIdNoteForImage(id :Int){
        for(image in _mListImage.value!!){
            image.noteCreatorId = id
        }
    }

    public fun deleteImageNotSaveDB(){
        for(image in _mListImage.value!!){
            if(image.idImage ==0 ) {
                deleteInternalStorge(image)
            }
        }
    }



    // Boi vi edit and add deu dung _mListImage de cap nhat len recycleView . Ma HomeFragment ban chat o giua Edit va Add Image
    // nen moi khi quay tro ve home thi _mListImage se bang 0
    fun onClearListImageLiveData(){
        _mListImage.value = mutableListOf()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
    }

}