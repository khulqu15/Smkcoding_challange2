package com.example.latihan_challange2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Articles")
data class Articles(
    var title: String,
    var description: String,
    var location: String,
    var created_at: String,
    @PrimaryKey var article_key: String
) {
    constructor() : this("", "", "", "", "")
}