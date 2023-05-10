package com.example.vkinternshipsummer.room

import androidx.room.*

@Dao
interface FileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFile(file: FileModel)

    @Query("SELECT * FROM files WHERE file_path = :path")
    suspend fun getAllFilesByPath(path: String): List<FileModel>

    @Query("SELECT * FROM files WHERE hash_code = :hashCode")
    suspend fun getFileByHashCode(hashCode: String): FileModel?
}