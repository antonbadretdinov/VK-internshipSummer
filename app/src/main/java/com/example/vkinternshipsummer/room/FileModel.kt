package com.example.vkinternshipsummer.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "files",
    indices = [Index(value = ["hash_code"], unique = true)])
data class FileModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "file_path")
    val filePath: String?,

    @ColumnInfo(name = "hash_code")
    val hashCode: String
)