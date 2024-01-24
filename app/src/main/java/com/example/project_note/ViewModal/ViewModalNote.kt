package com.example.project_note.ViewModal

import SingleLiveEvent
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.Repository.NoteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.Locale

class ViewModalNote(application: Application) : AndroidViewModel(application) {

    private var _mListNote = MutableLiveData<MutableList<Note>>()
    public val mListNote: LiveData<MutableList<Note>> get() = _mListNote


    private val _selectedNote = MutableLiveData<Note>()
    public val selectNote: LiveData<Note> get() = _selectedNote


    private lateinit var  listQuery : MutableList<Note>


    private var _getIdNoteFromRoom: MutableLiveData<Int> = MutableLiveData()
    public val getIdNoteFromRoom get() = _getIdNoteFromRoom



    private var _checkgetid: SingleLiveEvent<Boolean> = SingleLiveEvent()
    public val checkgetid get() = _checkgetid



    private lateinit var notecurrent: Note;


    val repository: NoteRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    init {
        val dao = NoteDatabase.getInstance(application).noteDAO()
        repository = NoteRepository(dao)
        getListNotes()
        _checkgetid.value=false
    }

fun checkgetidtoFalse(){
    _checkgetid.value = false
}

    fun onFilterChange(query: String){
        filterNote.filter.filter(query)
    }

    val filterNote = object : Filterable {
        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val strSearch = constraint.toString()
                    val mListFilter = if(strSearch.isEmpty()){
                        listQuery
                    } else{
                        listQuery?.filter { it.title.lowercase(Locale.getDefault()).contains(strSearch.lowercase(Locale.getDefault())) }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = mListFilter
                    return filterResults
                }
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    _mListNote.value = results?.values as MutableList<Note>?
                }
            }
        }
    }
    public fun getListNotes() {
        compositeDisposable.add(
            repository.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ lists ->
                            _mListNote.value = lists
                            listQuery = lists
                           Log.d("AAA","Lấy được getListNote từ Room")},
                    { throwable -> })
        )
    }


    public fun addNote(note: Note) {
        compositeDisposable.add(
            repository.insert(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { isInsertNote ->
//                        _checkInsertNote.value = isInsertNote
                       Log.e("ManhDuc","Insert Note trong Rom thành công")
                        getId(notecurrent.title,notecurrent.detail);
                        getListNotes();
                    },
                    { throwable -> Log.e("AAA", "Insert error: ${throwable.message}") }
                )
        )
    }
    public fun addNoteViewmodel(note: Note){
        notecurrent = note;
        addNote(note);
    }
    public fun updateNoteEdit(note: Note){
            notecurrent = note
            notecurrent.idNote = selectNote.value!!.idNote
             InsertNoteToRealTime(notecurrent)
    }


    public fun updateNote(note: Note) {
        updateSelectedNote(note)
        compositeDisposable.add(
            repository.update(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("viemmodal", "Update Note To ROOM success") },
                    { throwable -> Log.e("AAA", "Update error: ${throwable.message}") }
                )

        )
    }
    fun getId(title:String , detail:String){
        compositeDisposable.add(
            repository.getID(title,detail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { id->
                        _checkgetid.value = true
                        _getIdNoteFromRoom?.value = id
                        notecurrent.idNote = id
                        InsertNoteToRealTime(notecurrent)

                    Log.d("ManhDuc","Lấy Id thành công")},
                    { throwable -> Log.e("AAA", "Query error: ${throwable.message}") }
                )
        )
    }


    public fun deleteNote(note: Note) {
        compositeDisposable.add(
            repository.delete(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("AAA", "Delete success") },
                    { throwable -> Log.e("AAA", "Delete error: ${throwable.message}") }
                )
        )
    }

    public fun updateSelectedNote(note: Note) {
        // Xem lại
        _selectedNote.value = note

    }

    fun InsertNoteToRealTime(note:Note){
        compositeDisposable.add(
            repository.insertToRealTime(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("AAA", "Insert Note success to FireBase") },
                    { throwable -> Log.e("AAA", "Delete error: ${throwable.message}") }
                )
        )
    }
    fun DeleteNoteToRealTime(note:Note){
        compositeDisposable.add(
            repository.deleteToRealTime(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("AAA", "Delete Note success to FireBase") },
                    { throwable -> Log.e("AAA", "Delete error: ${throwable.message}") }
                )
        )
    }
     fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }

        return result
    }

    public fun onClickDeleteNote(note: Note) {
        val listData = _mListNote.value
        listData?.remove(note)
        _mListNote.value = listData
    }

    public fun onClickAddNote(index: Int, note: Note) {
        val listData = _mListNote.value
        listData?.add(index, note)
        _mListNote.value = listData
    }
//    fun InsertNoteToRealTime(){
//
//    }


}
// tao dang ky dang nhap . firebase ac
// luu du lieu realtime , van dung db
// them anh trong edit
// chon anh moi thi mo fragment moi
