package com.example.vkinternshipsummer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkinternshipsummer.adapter.FilesAdapter
import com.example.vkinternshipsummer.databinding.ActivityFileListBinding
import java.io.File

class FileListActivity : AppCompatActivity(), ItemListener {
    private lateinit var binding: ActivityFileListBinding
    private var filesAndFolders: Array<File>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFiles.layoutManager = LinearLayoutManager(this)

        val path = intent.getStringExtra("path")
        val root = File(path.toString())
        filesAndFolders = root.listFiles()
        if(filesAndFolders?.isEmpty() == true)
            binding.tvWarning.visibility = View.VISIBLE
        else
            binding.recyclerFiles.adapter = FilesAdapter(filesAndFolders?.sortedWith(naturalOrder()),
                this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.sort_date -> {
                val sortedByDate = filesAndFolders?.sortedByDescending { it.lastModified() }?.toList()
                binding.recyclerFiles.adapter = FilesAdapter(sortedByDate, this)
            }
            R.id.sort_asc -> {
                val sortByAsc = filesAndFolders?.sortedByDescending { it.length() }?.asReversed()
                binding.recyclerFiles.adapter = FilesAdapter(sortByAsc, this)
            }
            R.id.sort_desc -> {
                val sortByDesc = filesAndFolders?.sortedByDescending { it.length() }
                binding.recyclerFiles.adapter = FilesAdapter(sortByDesc, this)
            }
            R.id.sort_name -> binding.recyclerFiles.adapter = FilesAdapter(filesAndFolders?.
                sortedWith(naturalOrder()), this)
            R.id.sort_type -> {
                val sortedByType = filesAndFolders?.sortedBy { it.extension }?.toList()
                binding.recyclerFiles.adapter = FilesAdapter(sortedByType, this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}