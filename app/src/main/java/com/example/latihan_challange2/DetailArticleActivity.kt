package com.example.latihan_challange2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.latihan_challange2.util.tampilToast
import com.example.latihan_challange2.viewmodel.ArticleViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_detail_article.*
import kotlinx.android.synthetic.main.activity_detail_article.txtTitle

class DetailArticleActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabaseArticles: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var key: String? = ""

    private val viewModel by viewModels<ArticleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_article)
        mAuth = FirebaseAuth.getInstance()

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F

        intialize()

        getDataNews()
    }

    private fun intialize() {
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        mDatabaseReference = mDatabase!!.reference.child("Users").child(user!!.uid)
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                tampilToast(this@DetailArticleActivity, "Gagal Error")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").getValue().toString()
                if(email != "admin@sidescript.com") {
                    btn_settingnews.hide()
                } else {
                    btn_settingnews.show()
                    btn_settingnews.setOnClickListener { view ->
                        val action = arrayOf("Update", "Delete")
                        val alert = AlertDialog.Builder(view.context)
                        alert.setItems(action) { dialog, i ->
                            when(i) {
                                0 -> {
                                    val title = txtTitle.text.toString()
                                    val location = txtLoc.text.toString()
                                    val created = txtCreated_at.text.toString()
                                    val description = txtDescription.text.toString()
                                    val bundle = Bundle()
                                    val bundleExtras: Bundle? = intent.extras
                                    val key = bundleExtras?.getString("key")
                                    bundle.putString("title", title)
                                    bundle.putString("location", location)
                                    bundle.putString("created", created)
                                    bundle.putString("description", description)
                                    bundle.putString("key", key)
                                    val intent = Intent(this@DetailArticleActivity, EditArticleActivity::class.java)
                                    intent.putExtras(bundle)
                                    startActivity(intent)
                                }
                                1 -> {
                                    val builder = AlertDialog.Builder(this@DetailArticleActivity)
                                    builder.setTitle("Hapus Artikel ?")
                                    builder.setMessage("Yakin artikel ini mau dihapus ?")
                                    builder.setIcon(R.drawable.ic_trash_red)
                                    builder.setPositiveButton("Delete") { dialog, which ->
                                        mDatabase = FirebaseDatabase.getInstance()
                                        val bundleExtras: Bundle? = intent.extras
                                        val key = bundleExtras?.getString("key")
                                        mDatabaseReference = mDatabase!!.reference.child("Articles").child(key!!)
                                        mDatabaseReference!!.removeValue().addOnCompleteListener {
                                            val intent = Intent(this@DetailArticleActivity, ArticleFragment::class.java)
                                            startActivity(intent)
                                            tampilToast(this@DetailArticleActivity, "Berhasil dihapus")
                                        }
                                    }
                                    builder.setNegativeButton("Cancel") { dialog, which ->  
                                        tampilToast(this@DetailArticleActivity, "Cancelled")
                                    }
                                    val alertDialog: AlertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()
                                }
                            }
                        }
                        alert.create()
                        alert.show()
                        true
                    }
                }
            }
        })
    }

    private fun getDataNews() {
        mDatabase = FirebaseDatabase.getInstance()
        val bundle: Bundle? = intent.extras
        key = bundle?.getString("key")
        mDatabaseArticles = mDatabase!!.reference.child("Articles").child(key!!)
        mDatabaseArticles!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                tampilToast(this@DetailArticleActivity, "Gagal Error")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val ArticleList = snapshot.getValue(Articles::class.java)
                if(ArticleList != null) {
                    txtTitle.text = ArticleList.title
                    txtLoc.text = ArticleList.location
                    txtCreated_at.text = ArticleList.created_at
                    txtDescription.text = ArticleList.description
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
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