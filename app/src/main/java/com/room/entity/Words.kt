package com.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Words {
    @PrimaryKey(autoGenerate = true)
    var wordId: Int? = null

    @ColumnInfo(name = "wordName")
    var wordName: String? = null

    @ColumnInfo(name = "wordTranslation")
    var wordTranslation: String? = null

    @ColumnInfo(name = "wordFavourite")
    var wordFavourite: Int? = null

    @ColumnInfo(name = "wordImage")
    var wordImage: String? = null

    @ColumnInfo(name = "wordCategoryId")
    var wordCategoryId: Int? = null


}