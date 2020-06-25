package com.example.latihan_challange2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.latihan_challange2.util.tampilToast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    val menuIcon = arrayOf(R.drawable.ic_home, R.drawable.ic_tracking, R.drawable.ic_hospital, R.drawable.ic_article)
    private val TAG: String? = "FirebaseMassaging"
    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        if(mAuth!!.currentUser == null ) {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent)
            finish()
        }

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        notifExample()
        sendNotif()
        val adapter = ViewPagerAdapter(this)
        view_pager.setAdapter(adapter)
        TabLayoutMediator(tab_layout, view_pager, TabLayoutMediator.TabConfigurationStrategy() {
            tab, position ->
//            tab.text = menuText[position]
            tab.icon = ResourcesCompat.getDrawable(resources, menuIcon[position], null)
        }).attach()
    }

    private fun sendNotif() {
//        val db = FirebaseFirestore.getInstance()
//        var users: Map<String, Any>? = HashMap<>()
//        users.put()
    }

    private fun notifExample() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w(TAG, "getInstance ID Failed", task.exception)
                    return@addOnCompleteListener
                } else {
                    val SENDER_ID = "408669592838"
                    val fm = FirebaseMessaging.getInstance()
                    val message = RemoteMessage.Builder("$SENDER_ID@fcm.googleapis.com")
                        .setMessageId(Random.nextInt(9999).toString())
                        .addData("Covid app", "Covid Application")
                        .addData("Application", "Coronavirus Tracker")
                        .build()
                    if (message.data.isNotEmpty()) {
                        Log.e(TAG, "UpstreamData: " + message.data)
                    }
                    if (!message.messageId?.isEmpty()!!) {
                        Log.e(TAG, "UpstreamMessageId: " + message.messageId)
                    }
                    fm.send(message)
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                tampilToast(this, "This Is Home")
                return true
            }
            R.id.action_profile -> {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_logout -> {
                OnLogout()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun OnLogout() {
        mAuth!!.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
