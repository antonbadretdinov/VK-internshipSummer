package com.example.vkinternshipsummer

import android.content.Context
import com.example.vkinternshipsummer.helpers.generateHashCode
import com.example.vkinternshipsummer.room.FileDatabase
import com.example.vkinternshipsummer.room.FileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

suspend fun saveFilesToDatabase(context: Context, path: String) {
    val database = FileDatabase.getDb(context)
    val stack = Stack<File>()
    val root = File(path)
    stack.push(root)

    while (!stack.isEmpty()) {
        val file = stack.pop()

        if (file.isFile) {
            val fileModel = FileModel(
                id = null,
                filePath = file.absolutePath,
                hashCode = generateHashCode(file.absolutePath) ?: ""
            )

            withContext(Dispatchers.IO) {
                database.fileDao().insertFile(fileModel)
            }
        } else if (file.isDirectory) {
            val files = file.listFiles()
            files?.forEach { child ->
                stack.push(child)
            }
        }
    }
}
