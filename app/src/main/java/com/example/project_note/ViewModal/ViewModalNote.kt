package com.example.project_note.ViewModal

import android.app.Application
import android.util.Log
import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.Repository.ImageRepository
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

    val repository: NoteRepository
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private lateinit var  listQuery : MutableList<Note>



    init {
        val dao = NoteDatabase.getInstance(application).noteDAO()
        repository = NoteRepository(dao)
        getListNotes()
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
                    Log.d("AAA","getListNotestart")
                            _mListNote.value = lists
                            listQuery = lists
                           Log.d("AAA","getListNoteend")},
                    { throwable -> })
        )
    }


    public fun addNote(note: Note) {
        compositeDisposable.add(
            repository.insert(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("AAA", "Insert success") },
                    { throwable -> Log.e("AAA", "Insert error: ${throwable.message}") }
                )
        )
    }


    public fun updateNote(note: Note) {
        updateSelectedNote(note)
        compositeDisposable.add(
            repository.update(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { Log.d("viemmodal", "Update success") },
                    { throwable -> Log.e("AAA", "Update error: ${throwable.message}") }
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

//        val listData = _mListNote.value
//        val index = listData?.indexOf(note)
//        _mListNote.value = listData
//
//        if (index != null) {
//            listData?.set(index, note)
//        }
//        _mListNote.value = listData
    }

//    public fun getID(title:String, detail:String) : Int{
//        compositeDisposable.add(
//            repository.getID(title,detail)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { id -> handleID(id) },
//                    { throwable -> Log.e("AAA", "Delete error: ${throwable.message}") }
//                )
//        )
//
//    }


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


}
// tao dang ky dang nhap . firebase ac
// luu du lieu realtime , van dung db
// them anh trong edit
// chon anh moi thi mo fragment moi
