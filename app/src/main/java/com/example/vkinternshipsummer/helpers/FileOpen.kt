package com.example.vkinternshipsummer.helpers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

class FileOpen {
    companion object {
        fun openFile(context: Context, file: File) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(context,
                context.applicationContext.packageName + ".provider",file)
            try {
                when (file.toString()) {
                    ".doc" -> intent.setDataAndType(uri, "application/msword")
                    ".docx" -> intent.setDataAndType(uri, "application/msword")
                    ".pdf" -> intent.setDataAndType(uri, "application/pdf")
                    ".ppt" -> intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
                    ".pptx" -> intent.setDataAndType(uri, "application/vnd.ms-powerpoint")
                    ".xls" -> intent.setDataAndType(uri, "application/vnd.ms-excel")
                    ".xlsx" -> intent.setDataAndType(uri, "application/vnd.ms-excel")
                    ".rtf" -> intent.setDataAndType(uri, "application/rtf")
                    ".wav" -> intent.setDataAndType(uri, "audio/x-wav")
                    ".mp3" -> intent.setDataAndType(uri, "audio/x-wav")
                    ".gif" -> intent.setDataAndType(uri, "image/gif")
                    ".jpg" -> intent.setDataAndType(uri, "image/jpg")
                    ".jpeg" -> intent.setDataAndType(uri, "image/jpeg")
                    ".png" -> intent.setDataAndType(uri, "image/png")
                    ".txt" -> intent.setDataAndType(uri, "text/plain")
                    ".3gp" -> intent.setDataAndType(uri, "video/*")
                    ".mpg" -> intent.setDataAndType(uri, "video/*")
                    ".mpeg" -> intent.setDataAndType(uri, "video/*")
                    ".mpe" -> intent.setDataAndType(uri, "video/*")
                    ".mp4" -> intent.setDataAndType(uri, "video/*")
                    ".avi" -> intent.setDataAndType(uri, "video/*")
                    else -> intent.setDataAndType(uri, "*/*")
                }
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.startActivity(intent)
            }catch (e: Exception){
                Toast.makeText(context, "Failed to open file", Toast.LENGTH_SHORT).show()
            }
        }
    }
}