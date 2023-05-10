package com.example.vkinternshipsummer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vkinternshipsummer.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(checkPermission()){
            binding.progressBar.visibility = View.INVISIBLE
            val path = Environment.getExternalStorageDirectory().path
            CoroutineScope(Dispatchers.Default).launch {
                saveFilesToDatabase(this@MainActivity, path)
            }
            val fileScreenIntent = Intent(this, FileListActivity::class.java)
            fileScreenIntent.putExtra("path",path)
            startActivity(fileScreenIntent)
            finish()
        }else{
            requestPermission()
        }
    }

    private fun requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }catch (e: java.lang.Exception){
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)
            }
        }else{
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE)
            storageActivityResultLauncher.launch(intent)
        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if (Environment.isExternalStorageManager()){
                Toast.makeText(this, "Permission granted, restart the application",
                    Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.INVISIBLE
            }
            else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }else{
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted, restart the application",
                    Toast.LENGTH_LONG).show()
                binding.progressBar.visibility = View.INVISIBLE
            }
            else{
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            Environment.isExternalStorageManager()
        }else{
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object{
        const val STORAGE_PERMISSION_CODE = 100
    }
}