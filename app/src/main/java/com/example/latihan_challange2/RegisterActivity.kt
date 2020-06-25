package com.example.latihan_challange2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.latihan_challange2.util.tampilToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private val Tag = "RegisterActivity"

    private var edtname: EditText? = null
    private var edtemail: EditText? = null
    private var edtpassword: EditText? = null
    private var edttelp: EditText? = null
    private var edtaddress: EditText? = null
    private var mProgressBar: ProgressDialog? = null

    private var name: String? = ""
    private var email: String? = ""
    private var password: String? = ""
    private var telp: String? = ""
    private var address: String? = ""

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        if(user != null) {
            edtEmail!!.text = user?.email!!.toEditable()
            edtEmail.isEnabled = false
        }

        initialize()

        btnMasuk.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun initialize() {
        edtname = findViewById<View>(R.id.edtUsername) as EditText
        edtemail = findViewById<View>(R.id.edtEmail) as EditText
        edtpassword = findViewById<View>(R.id.edtPassword) as EditText
        edttelp = findViewById<View>(R.id.edtTelp) as EditText
        edtaddress = findViewById<View>(R.id.edtAddress) as EditText

        mProgressBar = ProgressDialog(this)

        btnDaftar!!.setOnClickListener { onRegister() }
    }

    private fun onRegister() {

        mAuth = FirebaseAuth.getInstance()
        name = edtname?.text.toString()
        password = edtpassword?.text.toString()
        telp = edttelp?.text.toString()
        address = edtaddress?.text.toString()
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if(user != null) {
            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase!!.reference.child("Users")
            mProgressBar!!.setMessage("Mendaftarkan User...")
            mProgressBar!!.show()
            email = user.email
            val userId = mAuth!!.currentUser!!.uid
            verifyEmail()
            mAuth!!.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    if(task.isComplete) {
                        val userId = mAuth!!.currentUser!!.uid

                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("name").setValue(name)
                        currentUserDb.child("email").setValue(email)
                        currentUserDb.child("telp").setValue(telp)
                        currentUserDb.child("address").setValue(address)
                        updateUserInfoAndUI()
                    } else if(task.isCanceled) {
                        tampilToast(this, "Pendaftaran dibatalkan")
                    } else {
                        tampilToast(this, "Pendaftaran Gagal")
                    }
                }
        } else {
            mDatabase = FirebaseDatabase.getInstance()
            mDatabaseReference = mDatabase!!.reference.child("Users")
            email = edtemail?.text.toString()
            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(telp) && !TextUtils.isEmpty(address)) {
                mProgressBar!!.setMessage("Mendaftarkan User...")
                mProgressBar!!.show()
                mAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) { task ->
                        mProgressBar!!.hide()
                        if(task.isSuccessful) {
                            Log.d(Tag, "createUserWithEmail:success")
                            val userId = mAuth!!.currentUser!!.uid

                            verifyEmail()

                            val currentUserDb = mDatabaseReference!!.child(userId)
                            currentUserDb.child("name").setValue(name)
                            currentUserDb.child("email").setValue(email)
                            currentUserDb.child("telp").setValue(telp)
                            currentUserDb.child("address").setValue(address)

                            updateUserInfoAndUI()
                        } else {
                            Log.w(Tag, "createUserWithEmail:failure", task.exception)
                            tampilToast(this, "Authentification Fail")
                        }
                    }
            } else {
                tampilToast(this, "Isikan semua form.")
            }
        }

    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    tampilToast(this, "Verification email sent to " + mUser.email)
                } else {
                    tampilToast(this, "Failed to sent verification email")
                }
            }
    }

    private fun updateUserInfoAndUI() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
