package com.example.project_note.ViewModal

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.Repository.ImageRepository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File

class ViewModalImage(application: Application) : AndroidViewModel(application){

    private var _mListImage = MutableLiveData<MutableList<Image>>()
    public val mListImage: LiveData<MutableList<Image>> get() = _mListImage


    private var _mListImageDelete : MutableList<Image> = mutableListOf()



    private var _mListImageAdd = MutableLiveData<MutableList<Image>>()
    public val mListImageAdd: LiveData<MutableList<Image>> get() = _mListImageAdd

    private var _checkImageSave : MutableLiveData<Boolean> = MutableLiveData()
    public val checkImageSave get() = _checkImageSave



    private var _checkdelete = MutableLiveData<Boolean>()
    public val checkdelete : LiveData<Boolean> get() = _checkdelete



    private var countRoom : Int;
    private var countRoomUpdate : Int;
    private var countFB : Int;
    private var countListAdd : Int;
    private var idNote:Int;
    private var countListDelete:Int;
    private var countFbDelete:Int;
    private var countRoomDelete:Int;
    private var countSTFBDelete:Int

    val repository: ImageRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        val dao = NoteDatabase.getInstance(application).ImageDAO()
        repository = ImageRepository(dao)
        _mListImageAdd.value= mutableListOf()

    }

    // Hàm này nhằm để gán _mListImageDElete = null
    public fun setListImageDeleteNull(){
        _mListImageDelete = mutableListOf()
    }
    public fun getListImage(idNote :Int) {
        compositeDisposable.add(
            repository.getAllImages(idNote)
                .subscribe({
                    _mListImage.value= it
                    getPathImageFirebaseStorage()
                    Log.d("Mạnh Đức","Lấy đc list Image từ Room") },
                    {Log.d("AAA",it.toString())})
        )
    }
    public fun addImageViewModal(image: Image){
        var mlist = _mListImage.value
        mlist?.add(image)
         _mListImage.value= mlist
    }
    public fun addImageViewModalAdd(image: Image){
        var mlist = _mListImageAdd.value
        mlist?.add(image)
        _mListImageAdd.value= mlist
    }
    public fun InserListImagetoDBLocal() {
        var imagesToInsert = _mListImageAdd.value!!
        countListAdd = imagesToInsert.size
        for (image in imagesToInsert) {
            if (image.idImage == 0) {
                addImageToRoom(image)
            }
        }
    }

    fun deleteImageDB(image : Image){
        compositeDisposable.add(
            repository.delete(image)
                .subscribe(
                    {Log.d("viemmodal", "Delete Image success")
                    countRoomDelete++
                    if(countRoomDelete == countListDelete && countSTFBDelete == countListDelete && countFbDelete == countListDelete){
                        _checkdelete.value = true
                        countRoomDelete = 0
                        countSTFBDelete = 0
                        countFbDelete =0
                        countListDelete = 0
                    }},
                    { throwable -> Log.e("AAA", "Update error: ${throwable.message}") }
                )
        )
    }
    fun updateImageDB(image : Image){
        compositeDisposable.add(
            repository.update(image)
                .subscribe(
                    {Log.d("viemmodal", "Update one Image To Room success")
                        countRoomUpdate ++
                        if(countListAdd == countRoomUpdate && countListAdd == countFB){
                            _checkImageSave.value = true
                            countRoomUpdate = 0
                            countListAdd = 0
                            countFB = 0
                            _mListImageAdd.value = mutableListOf()

                                deleteImageInlistDeleteInDB()
                        }
                    },
                    { throwable -> Log.e("AAA", "Update error: ${throwable.message}") }
                )
        )
    }


    fun deleImage(){
        // Xoa khoi ViewModel mlist add
        var mlist = _mListImage.value
        if(mlist?.isEmpty() == false) {
            mlist?.remove(_selectedImage.value)
            _mListImage.value = mlist
        }

        // xóa khỏi viewmidel mlist
        var mlist1 = _mListImageAdd.value
        mlist1?.remove(_selectedImage.value)
        _mListImageAdd.value = mlist1

        if(_selectedImage.value?.idImage == 0) {
            deleteInternalStorge(_selectedImage.value!!)
        }
        // Neu anh da luu save db -> them vao 1 list -> sau khi save thi se xoa han trong internal va db
        else{
            Log.d("Dức","Xóa ảnh đã lưu trong storage")
            addImageToListDelete(_selectedImage.value!!)
        }

        // TH Image chưa được lưu trong DB -> xoa trong internal storage


    }


    fun deleteInternalStorge(image: Image){
        val imageToDelete = File(image.path)
        //Kiem tra xe anh co ton tai khong
        if(imageToDelete.exists()){
            // Xóa đường dẫn trong Internal storge
            val isDeleted = imageToDelete.delete()
            // Xóa đối tượng
            if(isDeleted){
                Log.d("File","Xoa thanh cong trong Internal")
            }
            else
                Log.d("File","Khong the xoa file")
        }
        else
            Log.d("File","Flie khong ton tai")
    }

    fun addImageToListDelete(image: Image){
        _mListImageDelete.add(image)
        Log.d("ListDelte",""+_mListImageDelete.toString())
    }
    fun deleteImageInlistDeleteInDB(){
        Log.d("Manh Duc","Begin deleteImageInlistDeleteInDB ")

        countListDelete = _mListImageDelete.size
        if(countListDelete == 0)
            _checkdelete.value = true
        for(image in _mListImageDelete)
        {
            deleteInternalStorge(image)
            deleteImageDB(image)
            deleteImageRT(image)
            deleteImageSTFB(image)
        }

    }

fun setcheckdeleteFalse(){
    _checkdelete.value = false
}
    fun deleteImageInlist(){
        for(image in _mListImage.value!!)
        {
            deleteInternalStorge(image)
            deleteImageDB(image)
            deleteImageRT(image)
            deleteImageSTFB(image)
        }
    }


    public fun updateIdNoteForImage(){
        Log.d("ManhDuc ","Vào update IDNote cho Image")
        for(image in _mListImageAdd.value!!){
            image.noteCreatorId = idNote
        }
        InserListImagetoDBLocal()
        if(_mListImageAdd.value!!.isEmpty()){
            if(!_mListImageDelete.isEmpty()){
                deleteImageInlistDeleteInDB()

            }
        }
    }

    public fun getIdNoteforImage(id : Int){
        idNote = id;
    }


    public fun deleteImageNotSaveDB(){
        for(image in _mListImageAdd.value!!) {
            if (image.idImage == 0) {
                deleteInternalStorge(image)
            }
        }
        _mListImageAdd.value= mutableListOf()
    }
    fun onClearListImageLiveData(){
        _mListImage.value = mutableListOf()
        _mListImageAdd.value= mutableListOf()
    }




    fun setIdforListImageAdd(){
        for(imageadd in _mListImageAdd.value!!){
            for(image in _mListImage.value!!){
                if(imageadd.path.equals(image.path)){
                    imageadd.idImage = image.idImage
                }
            }
        }
    }
    fun getPathImageFirebaseStorage(){
        setIdforListImageAdd()

        var imagesToInsert = _mListImageAdd.value!!
        countListAdd =imagesToInsert.size;

        for (image in imagesToInsert) {
            if(image.idImage != 0){
                val file = Uri.fromFile(File(image.path))
                compositeDisposable.add(
                    repository.uploadImageToFirebaseStorage(file)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d("URI PHOTO",it.toString())
                            _mListImageAdd.value?.remove(image)
                            image.pathFB = it.toString()
                            _mListImageAdd.value?.add(image)
                            updateImageDB(image)
                            addImageToFb(image)
                        },
                            { throwable -> })
                )
            }
        }
    }

    public fun addImageToRoom(image: Image) {
        compositeDisposable.add(
            repository.insert(image)
                .subscribe(
                    {
                        countRoom++;
                        Log.d("viewModalImage", "Insert One Image success")
                        if(countRoom == countListAdd){
                            getListImage(idNote);
                            countRoom = 0;
                            countListAdd=0;
                        }
                    },
                    { throwable ->
                        Log.e("viewModalImage", "Insert error: ${throwable.message}")
                    }
                )
        )
    }

    fun addImageToFb(image: Image){
        repository.addImageToRealTime(image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { emittor->
                countFB++;
//                onComplete(emittor)
                if(countListAdd == countRoomUpdate && countListAdd == countFB){

                        deleteImageInlistDeleteInDB()

                    _checkImageSave.value = true
                    _mListImageAdd.value = mutableListOf()
                    countListAdd = 0;
                    countRoomUpdate = 0;
                    countFB = 0;
                }
                Log.d("FB","Upload One Image To Realtime FireBase succed")
            }
    }


    fun deleteImageRT(image: Image){
        compositeDisposable.add(
            repository.deleteImageRT(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    countFbDelete++
                    if(countRoomDelete == countListDelete && countSTFBDelete == countListDelete && countFbDelete == countListDelete){
                        _checkdelete.value = true
                        countRoomDelete = 0
                        countSTFBDelete = 0
                        countFbDelete =0
                        countListDelete = 0
                    }
                    Log.d("viemmodal", "Haizz Image Delete RT rồi ") })
        )
    }
    fun deleteImageSTFB(image: Image){
        compositeDisposable.add(
            repository.deleteImageSTFB(image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    countSTFBDelete++
                    if(countRoomDelete == countListDelete && countSTFBDelete == countListDelete && countFbDelete == countListDelete){
                        _checkdelete.value = true
                        countRoomDelete = 0
                        countSTFBDelete = 0
                        countFbDelete =0
                        countListDelete = 0
                    }
                    Log.d("viemmodal", "Haizz Image Delete RT rồi ") })
        )
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.dispose()
    }

}