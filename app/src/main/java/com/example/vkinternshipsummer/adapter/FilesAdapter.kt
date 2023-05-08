package com.example.vkinternshipsummer.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vkinternshipsummer.ItemListener
import com.example.vkinternshipsummer.R
import com.example.vkinternshipsummer.databinding.FileItemLayoutBinding
import java.io.File
import java.util.*


class FilesAdapter(private val filesAndFolders: Array<File>?, private val itemListener: ItemListener) :
    RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FileItemLayoutBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(file: File?, itemListener: ItemListener) = with(binding) {
            if((file?.name?.length ?: 0) > 13){
                tvFileName.text = file?.name.toString().substring(0,10)+"..."
            }else {
                tvFileName.text = file?.name
            }

            tvFileDate.text = file?.lastModified()?.let {Date(it).toString().substring(4,19)}

            ivIconFile.setImageResource(getImage(file))

            if(file?.isDirectory == false){
                if(file.length()/1048576f>1000){
                    tvFileSize.text = "${(file.length() / 1.07374182E9f)}".substring(0,4)+" Gb"
                }else if(file.length()/1024>1000){
                    tvFileSize.text = "${(file.length() / 1048576f)}".substring(0,4)+" Mb"
                }else {
                    tvFileSize.text = "${(file.length() / 1024)} Kb"
                }
            }else{
                tvFileSize.text = ""
            }
            itemView.setOnClickListener {
                itemListener.onItemClick(file)
            }
        }
        private fun getImage(file: File?): Int {
            return if(file?.isDirectory == true){
                R.drawable.baseline_folder_24
            }else if(file?.absolutePath?.contains(".jpg")==true||
                file?.absolutePath?.contains(".jpeg")==true){
                R.drawable.jpg_icon
            }else if(file?.absolutePath?.contains(".png")==true){
                R.drawable.png_icon
            }else if(file?.absolutePath?.contains(".txt")==true){
                R.drawable.txt_icon
            }else if(file?.absolutePath?.contains(".doc")==true||
                file?.absolutePath?.contains(".docx")==true){
                R.drawable.doc_icon
            }else if(file?.absolutePath?.contains(".mp4")==true){
                R.drawable.mp4_icon
            }else if(file?.absolutePath?.contains(".gif")==true){
                R.drawable.gif_icon
            }else if(file?.absolutePath?.contains(".pdf")==true){
                R.drawable.pdf_icon
            }else{
                return R.drawable.file_icon
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.file_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filesAndFolders?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filesAndFolders?.get(position), itemListener)
    }
}