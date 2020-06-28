package com.example.latihan_challange2

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.example.latihan_challange2.util.getCurrentDateTime
import com.example.latihan_challange2.util.tampilToast
import com.example.latihan_challange2.util.toString
import com.example.latihan_challange2.viewmodel.ArticleViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_add_article.*
import kotlin.random.Random

class AddArticleActivity : AppCompatActivity() {
    private var edtTitle: EditText? = null
    private var edtDescription: EditText? = null
    private var edtLocation: EditText? = null

    private var title: String? = null
    private var description: String? = null
    private var location: String? = null

    private val TAG: String? = "FirebaseMassaging"

    private val viewModel by viewModels<ArticleViewModel>()

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_article)

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        viewModel.init(this)

        intialize()

        btnTambahkan.setOnClickListener {
            prosessTambah()
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
        auth!!.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun intialize() {
        edtTitle = findViewById<View>(R.id.edtTitle) as EditText
        edtDescription = findViewById<View>(R.id.edtDescription) as EditText
        edtLocation = findViewById<View>(R.id.edtLocation) as EditText

        mDatabase = FirebaseDatabase.getInstance()
    }

    private fun prosessTambah() {

        title = edtTitle?.text.toString()
        description = edtDescription?.text.toString()
        location = edtLocation?.text.toString()

        if(title!!.isEmpty() && description!!.isEmpty()) {
            tampilToast(this, "Isikan semua form")
        } else {
            auth = FirebaseAuth.getInstance()
            mDatabaseReference = mDatabase!!.reference.child("Articles")
            val ref = mDatabaseReference
            ref!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    tampilToast(applicationContext, "Gagal Error")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val date = getCurrentDateTime()
                    val dateInString = date.toString("yyyy/MM/dd")
                    mDatabase = FirebaseDatabase.getInstance()
                    mDatabaseReference = mDatabase!!.reference.child("Articles").push()
                    mDatabaseReference!!.child("title").setValue(title)
                    mDatabaseReference!!.child("description").setValue(description)
                    mDatabaseReference!!.child("location").setValue(location)
                    mDatabaseReference!!.child("created_at").setValue(dateInString)
                    mDatabaseReference!!.child("article_key").setValue(mDatabaseReference?.key.toString())
                    val articleBaru = Articles(title!!, description!!, location!!, dateInString, mDatabaseReference?.key.toString())
                    viewModel.addData(articleBaru)
//                    sendNotif()
                    updateInfoUi()
                }
            })
        }
    }

    private fun sendNotif() {
        FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener { task ->
            if(!task.isSuccessful) {
                Log.w(TAG, "getInstance ID Failed", task.exception)
                return@addOnCompleteListener
            } else {
                FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            Log.w(TAG, "getInstanceId failed", task.exception)
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        val token = task.result.token

                        // Log and toast
                        val msg = "Hai"
                        Log.d(TAG, msg)
                        Toast.makeText(this@AddArticleActivity, msg, Toast.LENGTH_SHORT).show()
                    })
                val SENDER_ID = "408669592838"
                val fm = FirebaseMessaging.getInstance()
//                val message = RemoteMessage.Builder("$SENDER_ID@fcm.googleapis.com")
//                    .setMessageId(Random.nextInt(9999).toString())
//                    .addData("Covid app", "Covid Application")
//                    .addData("Application", "Coronavirus Tracker")
//                    .build()
//                if (message.data.isNotEmpty()) {
//                    Log.e(TAG, "UpstreamData: " + message.data)
//                }
//                if (!message.messageId?.isEmpty()!!) {
//                    Log.e(TAG, "UpstreamMessageId: " + message.messageId)
//                }
//                fm.send(message)
                fm.subscribeToTopic("Article")
                    .addOnCompleteListener { task ->
                        var msg = "Complete"
                        if(!task.isSuccessful) {
                            msg  = "Failed"
                        }
                        Log.d("FirebaseSubscibe", msg)
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun updateInfoUi() {
        tampilToast(applicationContext, "Berita berhasil ditambahkan")
        val intent = Intent(this@AddArticleActivity, ArticleFragment::class.java)
        startActivity(intent)
    }
}