package com.example.latihan_challange2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.example.latihan_challange2.util.tampilToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_user.*

class EditUserActivity : AppCompatActivity() {

    private var name: String? = null
    private var telp: String? = null
    private var address: String? = null

    private var edtName: EditText? = null
    private var edtTelp: EditText? = null
    private var edtAddress: EditText? = null

    private var mDatabaseRef: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        initialize()
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

    private fun initialize() {
        edtName = findViewById<View>(R.id.edtUserName_edit) as EditText
        edtTelp = findViewById<View>(R.id.edtUserTelp_edit) as EditText
        edtAddress = findViewById<View>(R.id.edtUserAddress_edit) as EditText

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mDatabaseRef = mDatabase!!.reference.child("Users")
        val ref = mDatabaseRef?.child(mAuth?.currentUser!!.uid)
        ref?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                tampilToast(applicationContext, "Gagal Error")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = snapshot.getValue(ListUser::class.java)
                if(userList != null) {
                    edtName?.text = userList.name.toEditable()
                    edtTelp?.text = userList.telp.toEditable()
                    edtAddress?.text = userList.address.toEditable()
                }
            }
        })
        btn_edit.setOnClickListener {
            onUpdate()
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun onUpdate() {
        name = edtName?.text.toString()
        telp = edtTelp?.text.toString()
        address = edtAddress?.text.toString()

        if (name!!.isEmpty() && telp!!.isEmpty() && address!!.isEmpty()) {
            tampilToast(this, "Form tidak boleh kosong")
        } else {
            val user = mAuth!!.currentUser
            val ref = mDatabaseRef?.child(user!!.uid)
            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    tampilToast(applicationContext, "Gagal Error")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userList = snapshot.getValue(ListUser::class.java)
                    if(userList != null) {
                        ref.child("name").setValue(name)
                        ref.child("telp").setValue(telp)
                        ref.child("address").setValue(address)
                        updateInfoUi()
                    }
                }
            })
        }
    }

    private fun updateInfoUi() {
        val intent = Intent(this, UserActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
