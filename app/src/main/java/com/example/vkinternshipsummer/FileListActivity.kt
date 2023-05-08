package com.example.vkinternshipsummer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkinternshipsummer.adapter.FilesAdapter
import com.example.vkinternshipsummer.databinding.ActivityFileListBinding
import java.io.File

class FileListActivity : AppCompatActivity(), ItemListener {
    private lateinit var binding: ActivityFileListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFiles.layoutManager = LinearLayoutManager(this)

        val path = intent.getStringExtra("path")
        val root = File(path.toString())
        val filesAndFolders: Array<File>? = root.listFiles()

        if(filesAndFolders?.isEmpty() == true)
            binding.tvWarning.visibility = View.VISIBLE
        else
            binding.recyclerFiles.adapter = FilesAdapter(filesAndFolders, this)
    }

    override fun onItemClick(file: File?) {
        if (file != null) {
            if (file.isDirectory) {
                val intent = Intent(this, FileListActivity::class.java)
                val path = file.absolutePath
                intent.putExtra("path", path)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                FileOpen.openFile(this,file)
            }
        }
    }
}