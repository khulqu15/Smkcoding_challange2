package com.example.latihan_challange2

import android.util.Log
import com.example.latihan_challange2.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class FCMPush {

    var JSON = MediaType.parse("application/json; charset=utf-8")
    var url = "https://fcm.googleapis.com/fcm/send"
    var serverKey = "AAAAXyabHQY:APA91bHrll_iBBRdObS_mmDBRG3f8P8CZhorvWVBxB5AJ4ub3l_FLx0SaKMoHLDosA9Ngoe83bXVSvXln3uHPgaC1gHWEjTNgzhWzQnZsL66V8zfCXr21t6AbpS4UQH6FHTX95Pbgmlw"
    var gson: Gson? = null
    var okHttpClient: OkHttpClient? = null
    companion object {
        var instance = FCMPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get().addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                var token = task?.result?.get("pushToken").toString()
                var pushDTO = PushDTO()
                pushDTO.to = token
                pushDTO.notification.title = title
                pushDTO.notification.body = message
                var body = RequestBody.create(JSON, gson?.toJson(pushDTO))
                var request = Request.Builder()
                    .addHeader("Content-Type","application/json")
                    .addHeader("Authorization", "key=$serverKey")
                    .url(url)
                    .post(body)
                    .build()

                okHttpClient?.newCall(request)?.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e("Error", e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response?.body()?.string())
                    }

                })
            }
        }
    }

}