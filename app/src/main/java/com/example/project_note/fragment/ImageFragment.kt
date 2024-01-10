package com.example.project_note.fragment

import android.app.Dialog
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.project_note.DataBase.Image
import com.example.project_note.R
import com.example.project_note.ViewModal.ViewModalImage
import com.example.project_note.databinding.FragmentImagedetailBinding


class ImageFragment : Fragment() {
    lateinit var binding_Image: FragmentImagedetailBinding
    private val binding get() = binding_Image!!
    private lateinit var mViewModalImage: ViewModalImage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding_Image = FragmentImagedetailBinding.inflate(inflater,container,false)
        mViewModalImage = ViewModelProvider(requireActivity()).get(ViewModalImage::class.java)


        val bundle = arguments ?: Bundle()
        val a = bundle.getString("keyvisible")
        if(a.equals("visible")){
            binding_Image.imgaDelete.visibility = View.GONE
        }
        mViewModalImage.selectImage.observe(viewLifecycleOwner){
            val bitmap = BitmapFactory.decodeFile(it.path)
            binding.imgView.setImageBitmap(bitmap)
        }
        setupButton()
        return binding.root
    }

    private fun setupButton() {
        binding.imgaBack.setOnClickListener {
           parentFragmentManager.popBackStack()
        }
        binding.imgaDelete.setOnClickListener {
           openDialogSave(Gravity.CENTER)

        }
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
    private fun openDialogSave(gravity: Int) {
        val dialog = createDialog(R.layout.dialog_save, gravity)
        val btnYes = dialog.findViewById<Button>(R.id.btn_save)
        val btnNo = dialog.findViewById<Button>(R.id.btn_discard)
        val titile = dialog.findViewById<TextView>(R.id.txt_title)
        btnYes.setText("YES")
        btnNo.setText("NO")
        titile.setText("Do You Delete Image ?")

        btnYes.setOnClickListener {
            mViewModalImage.deleImage()
            dialog.dismiss()
            parentFragmentManager.popBackStack()
        }
        btnNo.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}