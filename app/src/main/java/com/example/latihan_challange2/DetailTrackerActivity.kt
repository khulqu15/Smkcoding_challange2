package com.example.latihan_challange2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_detail_tracker.*

class DetailTrackerActivity: AppCompatActivity() {

    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tracker)

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 4.0F

        getDataDetailTracker()
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

    private fun getDataDetailTracker() {
        var bundle: Bundle ?=intent.extras
        var procinces = bundle!!.getString("prov")
        val confirm = bundle!!.getInt("confirmed")
        val recover = bundle!!.getInt("recovered")
        val death = bundle!!.getInt("death")
        val case = bundle!!.getInt("case")
        val date = bundle!!.getString("date")
        val latitude = bundle!!.getDouble("latitude")
        val longitude = bundle!!.getDouble("longitude")
        val logo = bundle!!.getString("logo")
        txtProvinsi.text = procinces
        txtPositiveCovid.text = confirm.toString()
        txtRecoveredCovid.text = recover.toString()
        txtDeathCovid.text = death.toString()
        txtActiveCase.text = case.toString()
        txtUpdateDate.text = date
        txtLatitude.text = latitude.toString()
        txtLongitude.text = longitude.toString()
        Glide.with(this).load(logo).into(imgProvLogo)
    }

}