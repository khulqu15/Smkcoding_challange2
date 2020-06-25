package com.example.latihan_challange2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.latihan_challange2.util.tampilToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var tName: TextView? = null
    private var tEmail: TextView? = null
    private var tTelp: TextView? = null
    private var tAddress: TextView? = null
    private var tVerikasi: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        btn_edit.setOnClickListener {
            val intent = Intent(applicationContext, EditUserActivity::class.java)
            startActivity(intent)
            finish()
        }

        initialize()

    }

    private fun initialize() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")

        tName = findViewById<View>(R.id.txtUserName) as TextView
        tEmail = findViewById<View>(R.id.txtUserEmail) as TextView
        tTelp = findViewById<View>(R.id.txtUserTelp) as TextView
        tAddress = findViewById<View>(R.id.txtUserAddress) as TextView
    }

    override fun onStart() {
        super.onStart()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        tEmail!!.text = mUser.email

        mUserReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                tName!!.text = p0.child("name").value as String
                tTelp!!.text = p0.child("telp").value as String
                tAddress!!.text = p0.child("address").value as String
            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_profile -> {
                tampilToast(this, "This Is Profile")
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