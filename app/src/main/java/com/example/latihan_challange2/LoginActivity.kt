package com.example.latihan_challange2

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.latihan_challange2.util.tampilToast
import com.facebook.CallbackManager
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity() : AppCompatActivity(), Parcelable {

    private var edtemail: EditText? = null
    private var edtpassword: EditText? = null

    private var temail: String? = null
    private var tpassword: String? = null
    private var mProgressBar: ProgressDialog? = null

    private lateinit var callbackManager: CallbackManager

    private var auth: FirebaseAuth? = null
    private var RC_SIGN_IN = 1

    constructor(parcel: Parcel) : this() {
        temail = parcel.readString()
        tpassword = parcel.readString()
      }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        if(auth!!.currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        initialize()

        btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        google_btn.setOnClickListener {
            googleLogin()
        }

    }

    private fun googleLogin() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN) {
            if(resultCode == Activity.RESULT_OK) {
                val mAuth = FirebaseAuth.getInstance()
                val user = mAuth.currentUser
                val ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(user!!.uid)
                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        tampilToast(applicationContext, "Gagal error")
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userList = snapshot.getValue(ListUser::class.java)
                        if(userList != null) {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(applicationContext, RegisterActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                })
            } else {
                tampilToast(this, "Login dibatalkan")
            }
        }
    }

    private fun initialize() {
        edtemail = findViewById<View>(R.id.edtEmail) as EditText
        edtpassword = findViewById<View>(R.id.edtPassword) as EditText
        mProgressBar = ProgressDialog(this)

        btnMasuk.setOnClickListener { onLogin() }

    }

    private fun onLogin() {
        temail = edtemail?.text.toString()
        tpassword = edtpassword?.text.toString()
        if(!TextUtils.isEmpty(temail) && !TextUtils.isEmpty(tpassword)) {
            mProgressBar!!.setMessage("User Login...")
            mProgressBar!!.show()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(temail!!, tpassword!!)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        return@addOnCompleteListener

                    } else {
                        tampilToast(this, "Berhasil Login")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }.addOnFailureListener {
                    Log.d("Main", "Failed Login: ${it.message}")
                    tampilToast(this, "Email / Password Incorrect")
                }
        } else {
            tampilToast(this, "Isikan semua form.")
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(temail)
        parcel.writeString(tpassword)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LoginActivity> {
        override fun createFromParcel(parcel: Parcel): LoginActivity {
            return LoginActivity(parcel)
        }

        override fun newArray(size: Int): Array<LoginActivity?> {
            return arrayOfNulls(size)
        }
    }
}