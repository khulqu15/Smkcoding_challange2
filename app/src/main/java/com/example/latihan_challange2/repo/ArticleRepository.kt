package com.example.latihan_challange2.repo

import androidx.lifecycle.LiveData
import com.example.latihan_challange2.Articles
import com.example.latihan_challange2.dao.ArticlesDao

class ArticleRepository(private val articlesDao: ArticlesDao) {
    val allArticles: LiveData<List<Articles>> = articlesDao.getAllArticles()
    suspend fun insert(articles: Articles) {
        articlesDao.insert(articles)
    }
    suspend fun insertAll(articles: List<Articles>) {
        articlesDao.insertAll(articles)
    }
    suspend fun deleteAll() {
        articlesDao.deleteAll()
    }
    suspend fun update(articles: Articles) {
        articlesDao.update(articles)
    }
    suspend fun delete(articles: Articles) {
        articlesDao.delete(articles)
    }
}