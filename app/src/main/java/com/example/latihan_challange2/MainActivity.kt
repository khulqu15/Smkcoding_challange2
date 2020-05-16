package com.example.latihan_challange2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.latihan_challange2.util.tampilToast
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val menuIcon = arrayOf(R.drawable.ic_home, R.drawable.ic_tracking, R.drawable.ic_hospital, R.drawable.ic_user)

    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 4.0F

        val adapter = ViewPagerAdapter(this)
        view_pager.setAdapter(adapter)
        TabLayoutMediator(tab_layout, view_pager, TabLayoutMediator.TabConfigurationStrategy() {
            tab, position ->
//            tab.text = menuText[position]
            tab.icon = ResourcesCompat.getDrawable(resources, menuIcon[position], null)
        }).attach()
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
