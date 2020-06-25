package com.example.latihan_challange2.util

import android.content.Context
import android.widget.Toast
import com.example.latihan_challange2.ArticleFragment

fun tampilToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}