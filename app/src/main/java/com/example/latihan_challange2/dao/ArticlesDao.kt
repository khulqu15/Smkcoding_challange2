package com.example.latihan_challange2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.latihan_challange2.Articles

@Dao
interface ArticlesDao {
    @Query("SELECT * from Articles")
    fun getAllArticles() : LiveData<List<Articles>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: Articles)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Articles>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(articles: Articles)

    @Delete()
    suspend fun delete(articles: Articles)

    @Query("DELETE FROM Articles")
    suspend fun deleteAll()
}