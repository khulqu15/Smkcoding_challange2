package com.example.latihan_challange2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_friends")
data class ListUser(
    var name: String,
    var email: String,
    var telp: String,
    var address: String,
    @PrimaryKey var key: String
) {
    constructor() : this("", "", "", "", "")
}