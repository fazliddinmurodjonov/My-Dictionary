package com.room.dao

import androidx.room.*
import com.room.entity.Words

import io.reactivex.rxjava3.core.Flowable

@Dao
interface WordsDao {
    @Insert
    fun insertWords(word: Words)

    @Update
    fun updateWords(word: Words)

    @Delete
    fun deleteWords(word: Words)

    @Query("select *from Words where wordId = :wordId")
    fun getWordsById( wordId: Int) : Words

    @Query("select *from Words")
    fun getAllWord(): List<Words>

    @Query("select *from Words")
    fun getAllWordWithFlowable(): Flowable<List<Words>>

    @Query("select *from Words where wordFavourite = 1")
    fun getFavouriteWords(): List<Words>

}