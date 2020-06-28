package com.example.latihan_challange2

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.latihan_challange2.util.tampilToast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception
import kotlin.random.Random

class MyFirebaseMassagingService : FirebaseMessagingService() {

    lateinit var notificationChannel: NotificationChannel
    private lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val TAG = "FirebaseMessaging"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

//    override fun onMessageSent(msgId: String) {
//        var title: String = "New Action Detect"
//        var body: String = "New Action is detected in app"
//        Log.e(TAG, "onMessageSent: " + msgId!!)
//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this,
//            0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//        val contentView = RemoteViews(packageName,
//            R.layout.activity_main)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(
//                title, body ,NotificationManager.IMPORTANCE_HIGH)
//            notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.GREEN
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//            builder = Notification.Builder(this, title)
//                .setContent(contentView)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
//                    R.drawable.ic_launcher_background))
//                .setContentIntent(pendingIntent)
//        }else{
//            builder = Notification.Builder(this)
//                .setContent(contentView)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
//                    R.drawable.ic_launcher_background))
//                .setContentIntent(pendingIntent)
//        }
//        notificationManager.notify(1234,builder.build())
//    }

    override fun onSendError(msgId: String, e: Exception) {
        Log.e(TAG, "onSendError: " + msgId!!)
        Log.e(TAG, "Exception: " + e!!)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if(remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message Data : ${remoteMessage.data}")
        }

        if(remoteMessage.notification!!.body != null) {
            Log.d(TAG, "Message Body : ${remoteMessage.notification!!.body}")
            sendNotification(remoteMessage.notification!!.body)
        }

//        val intent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(this,
//            0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
//        val contentView = RemoteViews(packageName,
//            R.layout.activity_main)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(
//                remoteMessage.notification?.title, remoteMessage.notification?.body ,NotificationManager.IMPORTANCE_HIGH)
//            notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.GREEN
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//            builder = Notification.Builder(this, remoteMessage.notification?.title)
//                .setContent(contentView)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
//                    R.drawable.ic_launcher_background))
//        }else{
//            builder = Notification.Builder(this)
//                .setContent(contentView)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(this.resources,
//                    R.drawable.ic_launcher_background))
//        }
//        notificationManager.notify(1234,builder.build())
//        Log.d(TAG, "From: " + remoteMessage!!.from)
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.notification?.body!!)
    }

    private fun sendNotification(body: String?) {
        var intent = Intent(applicationContext, UserActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("Notification", body)
        var pendingSent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder = NotificationCompat.Builder(this, "Notification")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Push Notification FCM")
            .setContentText(body)
//            .setAutoCancel(true)
            .setSound(notificationSound)
//            .setContentIntent(pendingSent)
            .setDefaults(Notification.DEFAULT_ALL)
            .addAction(R.drawable.logo, "Action", pendingSent)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        var notificationManager: NotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}