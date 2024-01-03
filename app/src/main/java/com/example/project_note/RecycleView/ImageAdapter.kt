package com.example.project_note.RecycleView

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project_note.DataBase.Image
import com.example.project_note.DataBase.Note
import com.example.project_note.databinding.ItemPhotosBinding

class ImageAdapter(private val mIClickListener1: IClickListener1)
    :ListAdapter<Image,ImageAdapter.ImageViewHodel>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<Image> = object : DiffUtil.ItemCallback<Image>() {
            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.idImage == newItem.idImage
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
    inner class ImageViewHodel(val binding: ItemPhotosBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHodel {
        return ImageViewHodel(ItemPhotosBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun onBindViewHolder(holder: ImageViewHodel, position: Int) {
        val image = getItem(position)
         var bitmap = BitmapFactory.decodeFile(image.path)
        holder.binding.imgItem.setImageBitmap(bitmap)
        holder.binding.imagePic.setOnClickListener { mIClickListener1.onClickImage(image) }
    }

}

interface IClickListener1{
   fun onClickImage(image : Image)
}
// internal storage -> uri, path -> save path to daba
