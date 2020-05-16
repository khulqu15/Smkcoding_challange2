package com.example.latihan_challange2


import com.google.gson.annotations.SerializedName

data class ApiHospitalItem(
    @SerializedName("kotakab")
    val kotakab: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("Provinsi")
    val provinsi: String,
    @SerializedName("telp")
    val telp: String
)