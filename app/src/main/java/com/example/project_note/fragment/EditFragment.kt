package com.example.project_note.fragment


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.Note
import com.example.project_note.DataBase.NoteDatabase
import com.example.project_note.R
import com.example.project_note.RecycleView.IClickListener1
import com.example.project_note.RecycleView.ImageAdapter
import com.example.project_note.ViewModal.ViewModalImage
import com.example.project_note.ViewModal.ViewModalNote
import com.example.project_note.databinding.FragmentEditNoteBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class EditFragment : Fragment() {

    lateinit var binding_edit: FragmentEditNoteBinding
    private val binding get() = binding_edit
    lateinit var mViewModalNote: ViewModalNote
    lateinit var mViewModalImage: ViewModalImage
    lateinit var mImageAdapter: ImageAdapter
    lateinit var mProgressDialog : ProgressDialog


    var idNote = -1

    var PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_edit = FragmentEditNoteBinding.inflate(inflater, container, false)
        mViewModalNote = ViewModelProvider(requireActivity()).get(ViewModalNote::class.java)
        mViewModalImage = ViewModelProvider(requireActivity()).get(ViewModalImage::class.java)
        mProgressDialog = ProgressDialog(requireContext())
        setupRecycleView()
        checkAddorEdit()
        setupButtons()
        overrideback()
        return binding.root
    }
    private fun setupRecycleView() {
         mImageAdapter = ImageAdapter(object : IClickListener1 {
             override fun onClickImage(image: Image) {
                 mViewModalImage.updateSelectImage(image)
                 openImageFragment()
             }

         })
        binding_edit.rcyPhotos.adapter = mImageAdapter

    }

    private fun checkAddorEdit() {

        val bundle = arguments ?: Bundle()
        val a = bundle.getString("HometoEdit1")


        if (a.equals("Anh")) {
            Log.d("edtnote", " creat new note")
            binding_edit.imgbutVisible.visibility = View.GONE
            mViewModalImage.mListImage.observe(viewLifecycleOwner){
                mImageAdapter.submitList(it.toMutableList())
            }


        } else {
            Log.d("edtnote", "Old Note ${a}")
            mViewModalNote.selectNote.observe(viewLifecycleOwner) {
                showDataForEdit(it)
            }
        }
    }

    private fun showDataForEdit(note: Note) {
        binding_edit.edtTitle.setText(note.title)
        binding_edit.edtDetail.setText(note.detail)
        idNote = note.idNote
        mViewModalImage.mListImage.observe(viewLifecycleOwner){
            mImageAdapter.submitList(it.toMutableList())
        }

    }
    private fun setupButtons() {
        binding_edit.imgbutSave.setOnClickListener { openDialogSave(Gravity.CENTER) }
        binding_edit.imgBack.setOnClickListener {
            openDialogReturn(Gravity.CENTER) }
        binding_edit.imgbutVisible.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding_edit.miscellaneous.imgbutAddpic.setOnClickListener {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getImage()
            }
            activity?.let {
                if (hasPermissions(it as Context, PERMISSIONS)) {
                    getImage()
                } else {
                    permReqLauncher.launch(PERMISSIONS)
                }
            }
        }
    }

    private val permReqLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                getImage()
            }
        }
    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getImage() {

        // open glarry
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {

            // Xử lý kết quả trả về từ thư viện ảnh
            val imagedata: Intent? = result.data

            if (imagedata != null && imagedata.getData() != null) {
                // Uri của ảnh được chọn
                val selectedImageUri = imagedata.data
                try {
                    selectedImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap( requireContext().contentResolver,  selectedImageUri)
                            saveImageInternal(bitmap)

                        } else {
                            val source = ImageDecoder.createSource(requireContext().contentResolver, selectedImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                                decoder.setTargetSampleSize(1) // shrinking by
                                decoder.isMutableRequired =  true // this resolve the hardware type of bitmap problem
                            }
                            saveImageInternal(bitmap)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }
        }
    }
    fun saveImageInternal(bitmap: Bitmap) {

        // depcle
//       val directory = File(Environment.getExternalStorageDirectory(), "/MyNoteData")
        val directory = context?.getExternalFilesDir("MyNoteData")
        if (directory != null) {
            if (!directory.exists()) {
                directory.mkdirs()
            }
        }

        val time:String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        // Tạo tập tin để lưu ảnh
        val file = File(directory, time+".jpg")
        Log.d("FilePath", file.absolutePath)

        try {
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            Toast.makeText(requireContext(), "Image saved to External Storage", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }

//            mViewModalImage.addImage(Image(file.absolutePath,idNote))
        val image = Image(file.absolutePath,idNote)
        Log.d("Khancapp",image.toString())
            mViewModalImage.addImageViewModal(image)

//            mViewModalImage.getListImage(idNote)

    }


        private fun createDialog(layoutResId: Int, gravity: Int): Dialog {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(layoutResId)

            val window = dialog.window ?: throw IllegalStateException("Dialog has no window")

            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.attributes.gravity = gravity

            dialog.setCancelable(Gravity.CENTER == gravity)
            return dialog
        }

        private fun openDialogReturn(gravity: Int) {
            val dialog = createDialog(R.layout.dialog_discardsave, gravity)
            val btnKeep = dialog.findViewById<Button>(R.id.btn_keep)
            val btnDiscard = dialog.findViewById<Button>(R.id.btn_discard)

            btnKeep.setOnClickListener {
                mViewModalImage.deleteImageNotSaveDB()
                parentFragmentManager.popBackStack()
                dialog.dismiss()
            }
            btnDiscard.setOnClickListener { dialog.dismiss() }
            dialog.show()

        }

        private fun openDialogSave(gravity: Int) {
            val dialog = createDialog(R.layout.dialog_save, gravity)
            val btnSave = dialog.findViewById<Button>(R.id.btn_save)
            val btnDiscard = dialog.findViewById<Button>(R.id.btn_discard)

            btnSave.setOnClickListener {
                saveNotetoDataBase()
                dialog.dismiss()
            }
            btnDiscard.setOnClickListener { dialog.dismiss() }

            dialog.show()
        }

        private fun saveNotetoDataBase() {
            val note1 = Note(binding_edit.edtTitle.text.toString(), binding_edit.edtDetail.text.toString())
            if (idNote == -1) {
                Log.d("edt", "Thêm Note mới")
                mViewModalNote.addNote(note1)


                Toast.makeText(context, "Thêm mới Node thành công", Toast.LENGTH_SHORT).show()
                val id = NoteDatabase.getInstance(requireContext()).noteDAO().getID(binding_edit.edtTitle.text.toString(), binding_edit.edtDetail.text.toString())
                mViewModalImage.updateIdNoteForImage(id)
                mViewModalImage.InserImagetoDB()
                mViewModalNote.getListNotes()
                parentFragmentManager.popBackStack()
            }

            else {
                Log.d("edt", "Cập nhật Note cũ")
                note1.idNote = idNote
                mViewModalImage.InserImagetoDB()
                mViewModalImage.deleteImageInlistDeleteInDB()
                mViewModalNote.updateNote(note1)
                Toast.makeText(context, "Sửa đổi thành công", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            }
        }

    private fun openImageFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frag_layout, ImageFragment::class.java,null)
            .addToBackStack(null)
            .commit()
    }
        private fun overrideback() {
            requireActivity().onBackPressedDispatcher
                .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (parentFragmentManager.backStackEntryCount > 0) {
                            openDialogReturn(Gravity.CENTER)
                        } else {
                            // Không có fragment trong back stack, xử lý theo ý muốn
                            requireActivity().finish()
                        }
                    }
                })
        }

}

