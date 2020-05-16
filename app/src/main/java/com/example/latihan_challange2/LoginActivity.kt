package com.example.latihan_challange2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.latihan_challange2.util.tampilToast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var edtemail: EditText? = null
    private var edtpassword: EditText? = null

    private var temail: String? = null
    private var tpassword: String? = null

    private var mProgressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()

        btnDaftar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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
                        return@addOnCompleteListener
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
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
}
