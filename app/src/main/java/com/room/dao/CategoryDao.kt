package com.room.dao

import androidx.room.*
import com.room.entity.Category
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)

    @Query("select *from Category where categoryId = :categoryId")
    fun getCategoryById(categoryId: Int) : Category

    @Query("select *from Category")
    fun getAllCategory(): List<Category>

    @Query("select *from Category")
    fun getAllCategoryWithFlowable(): Flowable<List<Category>>

}