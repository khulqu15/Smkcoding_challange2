package com.example.latihan_challange2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_detail_hospital.*
import kotlinx.android.synthetic.main.hospital_item.txtHospitalCity
import kotlinx.android.synthetic.main.hospital_item.txtHospitalName
import kotlinx.android.synthetic.main.hospital_item.txtHospitalProv

class DetailHospitalActivity : AppCompatActivity() {

    private var mAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_hospital)

        mAuth = FirebaseAuth.getInstance()

        val actionBar = supportActionBar
        actionBar!!.title = "COV ID"
        actionBar.subtitle = "Coronavirus Indonesia"
        actionBar.elevation = 0F


        getDataDetailHospital()
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

    private fun getDataDetailHospital() {
//        intent.putExtra("h_name", hospital.provinsi)
//        intent.putExtra("h_city", hospital.kotakab)
//        intent.putExtra("h_prov", hospital.provinsi)
//        intent.putExtra("h_telp", hospital.telp)
//        intent.putExtra("h_latitude", hospital.latitude)
//        intent.putExtra("h_longitude", hospital.longitude)

        var bundle: Bundle? = intent.extras
        var name = bundle!!.getString("h_name")
        var city = bundle!!.getString("h_city")
        var prov = bundle!!.getString("h_prov")
        var telp = bundle!!.getString("h_telp")
        var latitude = bundle!!.getDouble("h_latitude")
        var longitude = bundle!!.getDouble("h_longitude")
        txtHospitalName.text = name
        txtHospitalCity.text = city
        txtHospitalProv.text = prov
        btnDialHospital.text = telp
        txtLatitude.text = latitude.toString()
        txtLongitude.text = longitude.toString()

        btnDialHospital.setOnClickListener {
            DialTelp(telp)
        }
    }

    private fun DialTelp(telp: String?) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(telp)))
        startActivity(intent)
    }
}
