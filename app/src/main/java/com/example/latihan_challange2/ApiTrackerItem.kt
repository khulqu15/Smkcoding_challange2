package com.example.latihan_challange2


import com.google.gson.annotations.SerializedName

data class ApiTrackerItem(
    @SerializedName ("country")
    val country: String,
    @SerializedName("Active cases")
    val activeCases: Int,
    @SerializedName("Confirmed")
    val confirmed: Int,
    @SerializedName("Deaths")
    val deaths: Int,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("Provinsi")
    val provinsi: String,
    @SerializedName("Recovered")
    val recovered: Int,
    @SerializedName("updateDate")
    val updateDate: String,
    @SerializedName("updateTime")
    val updateTime: String,
    @SerializedName("updateUnix")
    val updateUnix: Long
)