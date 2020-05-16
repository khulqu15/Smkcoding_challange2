package com.example.latihan_challange2


import com.google.gson.annotations.SerializedName

data class ApiIndonesiaCovid(
    @SerializedName("Active cases")
    val activeCases: Int,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("country")
    val country: String,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("Recovered")
    val recovered: Int,
    @SerializedName("updateDate")
    val updateDate: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("updateUnix")
    val updateUnix: Long
)