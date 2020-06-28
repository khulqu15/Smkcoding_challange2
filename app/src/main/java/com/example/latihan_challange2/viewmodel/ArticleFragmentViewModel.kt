package com.example.latihan_challange2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.latihan_challange2.Articles
import com.example.latihan_challange2.database.ArticlesDatabase
import com.example.latihan_challange2.repo.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleFragmentViewModel(): ViewModel() {

    lateinit var repository: ArticleRepository
    lateinit var allArticles: LiveData<List<Articles>>

    fun init(context: Context) {
        val articleDao = ArticlesDatabase.getDatabase(context).articleDao()
        repository = ArticleRepository(articleDao)
        allArticles = repository.allArticles
    }

    fun delete(articles: Articles) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(articles)
    }

    fun insertAll(articles: List<Articles>) = viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            repository.insertAll(articles)
    }
}