package com.room.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.room.entity.CategoryWithWords

@Dao
interface CategoryWithWords {
    @Transaction
    @Query("select *from Category where categoryId = :categoryId")
    fun getAllWordsByCategory(categoryId: Int): CategoryWithWords
}