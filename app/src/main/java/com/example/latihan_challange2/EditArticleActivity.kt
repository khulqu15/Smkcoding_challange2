package com.example.latihan_challange2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import com.example.latihan_challange2.util.tampilToast
import com.example.latihan_challange2.viewmodel.ArticleUpdateViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_article.*

class EditArticleActivity : AppCompatActivity() {

    private var key: String? = ""
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    private var edtTitle: EditText? = null
    private var edtLocation: EditText? = null
    private var edtDescription: EditText? = null

    private var title: String? = ""
    private var location: String? = ""
    private var description: String? = ""
    private var created: String? = ""
    private var articleKey: String? = ""

    private val viewModel by viewModels<ArticleUpdateViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_article)

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        viewModel.init(this)
        edtTitle = findViewById(R.id.edtTitle_edit)
        edtLocation = findViewById(R.id.edtLocation_edit)
        edtDescription = findViewById(R.id.edtDescription_edit)

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
        title = intent.getStringExtra("title").toString()
        location = intent.getStringExtra("location").toString()
        description = intent.getStringExtra("description").toString()
        created = intent.getStringExtra("created").toString()
        articleKey = intent.getStringExtra("key").toString()

        edtTitle = findViewById<View>(R.id.edtTitle_edit) as EditText
        edtLocation = findViewById<View>(R.id.edtLocation_edit) as EditText
        edtDescription = findViewById<View>(R.id.edtDescription_edit) as EditText
        edtTitle?.setText(title)
        edtDescription?.setText(description)
        edtLocation?.setText(location)
        val bundle: Bundle? = intent.extras
        key = bundle?.getString("key").toString()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child("Articles").child(key!!)
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                tampilToast(this@EditArticleActivity, "Gagal Error")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                title = snapshot.child("title").value.toString()
                location = snapshot.child("location").value.toString()
                description = snapshot.child("description").value.toString()
                edtTitle!!.text = title!!.toEditable()
                edtLocation!!.text = location!!.toEditable()
                edtDescription!!.text = description!!.toEditable()

                btnUpdate.setOnClickListener {
                    prosesUpdate()
                }
            }
        })
    }

    private fun prosesUpdate() {
        if(title!!.isEmpty() && location!!.isEmpty() && description!!.isEmpty() ) {
            tampilToast(this, "Tidak boleh kosong")
        } else {
            val articleBaru = Articles(title!!, description!!, location!!, created!!, key!!)
            mDatabaseReference!!.setValue(articleBaru)
                .addOnCompleteListener {
                    viewModel.updateData(articleBaru)
                }
            mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    tampilToast(applicationContext, "Gagal Error")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val articleList = snapshot.getValue(Articles::class.java)
                    if(articleList != null) {

                        mDatabaseReference!!.child("title").setValue(edtTitle!!.text.toString())
                        mDatabaseReference!!.child("location").setValue(edtLocation!!.text.toString())
                        mDatabaseReference!!.child("description").setValue(edtDescription!!.text.toString())
                        val bundle = Bundle()
                        bundle.putString("key", articleList.article_key)
                        val intent = Intent(this@EditArticleActivity, DetailArticleActivity::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                        tampilToast(this@EditArticleActivity, "Berhasil diupdate")
                    } else {
                        tampilToast(this@EditArticleActivity, "Tidak ada data")
                    }
                }

            })
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}