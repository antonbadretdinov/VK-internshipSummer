package com.example.vkinternshipsummer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FileModel::class],
    version = 1
)
abstract class FileDatabase: RoomDatabase() {

    abstract fun fileDao(): FileDao

    companion object{
        fun getDb(context: Context): FileDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                FileDatabase::class.java,
                "files-database"
            ).build()
        }
    }
}