package com.example.latihan_challange2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.latihan_challange2.Articles
import com.example.latihan_challange2.dao.ArticlesDao

@Database(entities = [Articles::class], version = 1, exportSchema = false)
public abstract class ArticlesDatabase : RoomDatabase() {
    abstract fun articleDao() : ArticlesDao
    companion object {
        @Volatile
        private var INSTANCE: ArticlesDatabase? = null
        fun getDatabase(context: Context): ArticlesDatabase {
            val tempIntance = INSTANCE
            if (tempIntance != null) {
                return tempIntance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesDatabase::class.java,
                    "Articles_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}