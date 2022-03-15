package com.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithWords(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "wordCategoryId"
    )
    val word: List<Words>,
)