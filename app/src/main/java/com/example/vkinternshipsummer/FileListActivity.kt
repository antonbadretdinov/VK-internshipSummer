package com.example.vkinternshipsummer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkinternshipsummer.adapter.FilesAdapter
import com.example.vkinternshipsummer.databinding.ActivityFileListBinding
import com.example.vkinternshipsummer.helpers.FileOpen
import com.example.vkinternshipsummer.helpers.generateHashCode
import com.example.vkinternshipsummer.room.FileDatabase
import com.example.vkinternshipsummer.room.FileModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileListActivity : AppCompatActivity(), ItemListener {
    private var database: FileDatabase? = null
    private lateinit var binding: ActivityFileListBinding
    private var filesAndFolders: Array<File>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerFiles.layoutManager = LinearLayoutManager(this)
        database = FileDatabase.getDb(this)


        val path = intent.getStringExtra("path")
        val root = File(path.toString())
        filesAndFolders = root.listFiles()
        if(filesAndFolders?.isEmpty() == true)
            binding.tvWarning.visibility = View.VISIBLE
        else {
/*            val listOfFiles = ArrayList<FileModel>()
            CoroutineScope(Dispatchers.Default).launch {
                filesAndFolders?.forEachIndexed { index, file ->
                        listOfFiles.add(
                            FileModel(
                                id = null,
                                filePath = file.absolutePath,
                                hashCode = generateHashCode(file.absolutePath)?:""
                            )
                        )
                    database?.fileDao()?.insertFile(listOfFiles[index])
                }
            }*/
            binding.recyclerFiles.adapter = FilesAdapter(
                filesAndFolders?.sortedWith(naturalOrder()),
                this
            )
        }
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
/*                val selectedFile = FileModel(
                    id = null,
                    filePath = file.absolutePath,
                    hashCode = generateHashCode(file.absolutePath)?:""
                )
                CoroutineScope(Dispatchers.IO).launch {
                    database?.fileDao()?.insertFile(selectedFile)
                }*/
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.changed_files -> {
                if(filesAndFolders?.isNotEmpty() == true) {
                    val changedFilesIntent = Intent(this, LastChangesActivity::class.java)
                    changedFilesIntent.putExtra(
                        "path",
                        filesAndFolders?.get(0)?.path?.substringBeforeLast('/')
                    )
                    startActivity(changedFilesIntent)
                }else{
                    Toast.makeText(this, "No files found in folder", Toast.LENGTH_SHORT).show()
                }
            }
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