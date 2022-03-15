package com.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Category {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int? = null

    @ColumnInfo(name = "categoryName")
    var categoryName: String? = null

}