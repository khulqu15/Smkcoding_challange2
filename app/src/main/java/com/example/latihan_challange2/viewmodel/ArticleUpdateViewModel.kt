package com.example.latihan_challange2.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.latihan_challange2.Articles
import com.example.latihan_challange2.dao.ArticlesDao
import com.example.latihan_challange2.database.ArticlesDatabase
import com.example.latihan_challange2.repo.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleUpdateViewModel() : ViewModel() {

    lateinit var repository: ArticleRepository

    fun init(context: Context) {
        val articlesDao = ArticlesDatabase.getDatabase(context).articleDao()
        repository = ArticleRepository(articlesDao)
    }

    fun updateData(articles: Articles) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(articles)
    }

}