package com.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.room.dao.CategoryDao
import com.room.dao.CategoryWithWords
import com.room.dao.WordsDao
import com.room.entity.Category
import com.room.entity.Words

@Database(entities = [Words::class, Category::class], version = 1)
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun wordsDao(): WordsDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categoryWithWords(): CategoryWithWords

    companion object {
        private var instance: DictionaryDatabase? = null

        @Synchronized
        fun getInstance(context: Context): DictionaryDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,
                    DictionaryDatabase::class.java,
                    "DictionaryDatabase")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}