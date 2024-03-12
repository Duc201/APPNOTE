package com.example.project_note

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.Repository.ImageRepository
import com.example.project_note.Repository.NoteRepository
import java.io.File

class SyncWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    val repository: NoteRepository
    val repositoryIma : ImageRepository

    init {
        val dao = NoteDatabase.getInstance(context).noteDAO()
        val daoIma = NoteDatabase.getInstance(context).ImageDAO()
        repository = NoteRepository(dao,context)
        repositoryIma = ImageRepository(daoIma,context)
    }
    override suspend fun doWork(): Result {
        var  listNotenotSave   = repository.getListNoteSaveFB(false)
//        var  listNoteDelete = repository.getListNoteDeleteLC(true)
        var  listImagenotSave = repositoryIma.getListImageSaveFB(false)
//        var  listImageDelete = repositoryIma.getListImageDeleteLC(true)

        for(note in listNotenotSave){
            if(note.isDelete){
//                note.isSaveFB = true
                repository.deleteToRealTime(note)
            }
            else{
                note.isSaveFB = true
                if(repository.addToRealTime(note)){
                    val timeStamp = repository.getTimeStamp(note)
                    note.timeStamp = timeStamp
                    repository.updateNoteDB(note)
                }
            }
        }

        for(image in listImagenotSave){
            if(image.isDelete){
                repositoryIma.deleteImageSTFB(image)
                image.isSaveFB = true
                repositoryIma.deleteImageRT(image)
            }
            else{
                val file = Uri.fromFile(File(image.path))
                val uri = repositoryIma.uploadImageToFirebaseStorage(file) ?: "null"
                image.pathFB = uri.toString()
                image.isSaveFB = true
                repositoryIma.addImageToRealTime(image)
                image.timeStamp = repositoryIma.getTimeStamp(image)
                repositoryIma.updateImageDB(image)
            }
        }

//        for (note in listNotenotSave) {
//            note.isSaveFB = true
//            repository.addToRealTime(note)
//            repository.updateNoteDB(note)
//        }
//        for(note in listNoteDelete){
//            repository.addToRealTime(note)
//            repository.delete(note)
//        }
//        for (image in listImagenotSave){
//                image.isSaveFB = true
//                val file = Uri.fromFile(File(image.path))
//                val uri = repositoryIma.uploadImageToFirebaseStorage(file)
//                image.pathFB = uri.toString()
//                repositoryIma.updateImageDB(image)
//                repositoryIma.addImageToRealTime(image)
//        }
//        for(image in listImageDelete){
//            repositoryIma.addImageToRealTime(image)
//            repositoryIma.deleteImageSTFB(image)
//            repositoryIma.deleteSave(image)
//        }
        // Mark the changes as synchronized
        return Result.success()
    }
}