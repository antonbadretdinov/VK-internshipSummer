package com.example.vkinternshipsummer

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkinternshipsummer.adapter.FilesAdapter
import com.example.vkinternshipsummer.databinding.ActivityLastChangesBinding
import com.example.vkinternshipsummer.helpers.generateHashCode
import com.example.vkinternshipsummer.room.FileDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class LastChangesActivity : AppCompatActivity(), ItemListener {

    private lateinit var binding: ActivityLastChangesBinding
    private var database: FileDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLastChangesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this,
            "Отображение файлов, которые были изменены с момента последнего запуска приложения, я не успел реализовать",
            Toast.LENGTH_LONG).show()
        binding.changedFilesRecycler.layoutManager = LinearLayoutManager(this)
        val path = intent.getStringExtra("path")
        database = FileDatabase.getDb(this)
        val currentHashCodes = ArrayList<String>()
        File(path.toString()).listFiles()?.forEach {
            generateHashCode(it.absolutePath)?.let { it1 -> currentHashCodes.add(it1) }
        }
        val hashCodesFromDb = ArrayList<String>()
        if (path != null) {
            CoroutineScope(Dispatchers.Default).launch {
                database?.fileDao()?.getAllFilesByPath(path)?.forEach {
                    hashCodesFromDb.add(it.hashCode)
                }
            }
        }

        val changedFiles = compareTwoLists(currentHashCodes, hashCodesFromDb)


        supportActionBar?.title = "Last modified files in ${path?.substringAfterLast('/')}"

        if(changedFiles.isNotEmpty())
            binding.changedFilesRecycler.adapter = FilesAdapter(
                filesAndFolders = changedFiles, this)
    }

    private fun compareTwoLists(currentHashCodes: List<String>, hashCodesFromDb: List<String>): List<File> {
        val differentFiles = mutableListOf<File>()
        currentHashCodes.zip(hashCodesFromDb) { currentHashCode, dbHashCode ->
            if (currentHashCode != dbHashCode) {
                differentFiles.add(File(currentHashCode))
            }
        }
        return differentFiles
    }


    override fun onItemClick(file: File?) {
        Toast.makeText(this, "The file was last modified on ${file?.lastModified()?.let {
            Date(it).toString() 
        }}", Toast.LENGTH_SHORT).show()
    }
}