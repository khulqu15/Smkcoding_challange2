package com.example.latihan_challange2.data

import com.example.latihan_challange2.ApiHospitalItem
import com.example.latihan_challange2.ApiIndonesiaCovid
import com.example.latihan_challange2.ApiTrackerItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CovidTrackerService {
    @GET("data/provinsi/all.json")
    fun getTracker(): Call<List<ApiTrackerItem>>

    @GET("data/provinsi/all.json")
    fun getProvTracker(): Call<ArrayList<ApiTrackerItem>>


    @GET("data/indonesia.json")
    fun getHomeTracker(): Call<ApiIndonesiaCovid>

    @GET("data/provinsi/{name}")
    fun getTrackerProv(@Path("name")name: String): Call<ApiTrackerItem>

    @GET("data/rumah_sakit/all.json")
    fun getHospital(): Call<List<ApiHospitalItem>>

    @GET("data/rumah_sakit/{provinceName}.json")
    fun getSpesificHospital(@Path("provinceName")provinceName: String): Call<List<ApiHospitalItem>>


}