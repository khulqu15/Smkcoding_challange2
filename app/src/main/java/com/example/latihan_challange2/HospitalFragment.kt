package com.example.latihan_challange2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihan_challange2.data.CovidTrackerService
import com.example.latihan_challange2.data.apiRequest
import com.example.latihan_challange2.util.dismissLoading
import com.example.latihan_challange2.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_hospital.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HospitalFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hospital, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiHospital()
    }

    private fun callApiHospital() {
        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<CovidTrackerService>(httpClient)

        val call = apiRequest.getHospital()
        call.enqueue(object: Callback<List<ApiHospitalItem>> {
            override fun onFailure(call: Call<List<ApiHospitalItem>>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<List<ApiHospitalItem>>,
                response: Response<List<ApiHospitalItem>>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body()?.size != 0 ->
                                displayHospital(response.body()!!)
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        tampilToast(context!!, "Gagal")
                    }
                }
            }
        })

        val callProv = apiRequest.getProvTracker()
        callProv.enqueue(object : Callback<ArrayList<ApiTrackerItem>> {
            override fun onFailure(call: Call<ArrayList<ApiTrackerItem>>, t: Throwable) {
                //
            }
            override fun onResponse(
                call: Call<ArrayList<ApiTrackerItem>>,
                response: Response<ArrayList<ApiTrackerItem>>
            ) {
                if(response.code() == 200) {
                    val data : MutableList<String> = ArrayList()
                    response.body()?.forEach {
                        data.add(0, it.provinsi)
                    }
                    data.add(0, "Pilih Provinsi")
                    SpinnerHospital.adapter = ArrayAdapter<String>(context!!, R.layout.support_simple_spinner_dropdown_item, data)

                    SpinnerHospital.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val value = parent?.getItemAtPosition(position).toString()
                            setEndpoint(apiRequest, value)
                        }
                    }
                }
            }
        })
    }

    private fun setEndpoint(api: CovidTrackerService, provinceName: String) {
        if(provinceName != "Pilih Provinsi") {
            val callSpesifik = api.getSpesificHospital(provinceName)
            callSpesifik.enqueue(object: Callback<List<ApiHospitalItem>> {
                override fun onFailure(call: Call<List<ApiHospitalItem>>, t: Throwable) {
                      //
                }

                override fun onResponse(
                    call: Call<List<ApiHospitalItem>>,
                    response: Response<List<ApiHospitalItem>>
                ) {
                    when {
                        response.isSuccessful ->
                            when {
                                response.body()?.size != 0 ->
                                    displayHospital(response.body()!!)
                                else -> {
                                    tampilToast(context!!, "Berhasil")
                                }
                            }
                        else -> {
                            tampilToast(context!!, "Gagal")
                        }
                    }
                }
            })
        } else {
            val callAll = api.getHospital()
            callAll.enqueue(object: Callback<List<ApiHospitalItem>> {
                override fun onFailure(call: Call<List<ApiHospitalItem>>, t: Throwable) {
                    //
                }

                override fun onResponse(
                    call: Call<List<ApiHospitalItem>>,
                    response: Response<List<ApiHospitalItem>>
                ) {
                    when {
                        response.isSuccessful ->
                            when {
                                response.body()?.size != 0 ->
                                    displayHospital(response.body()!!)
                                else -> {
                                    tampilToast(context!!, "Berhasil")
                                }
                            }
                        else -> {
                            tampilToast(context!!, "Gagal")
                        }
                    }
                }
            })
        }
    }

    private fun displayHospital(Hospital: List<ApiHospitalItem>) {
        rv_listHospital.layoutManager = LinearLayoutManager(context)
        rv_listHospital.adapter = HospitalAdapter(this!!.requireContext(), Hospital) {
            val hospital = it
            val intent = Intent(activity, DetailHospitalActivity::class.java)
            intent.putExtra("h_name", hospital.provinsi)
            intent.putExtra("h_city", hospital.kotakab)
            intent.putExtra("h_prov", hospital.provinsi)
            intent.putExtra("h_telp", hospital.telp)
            intent.putExtra("h_latitude", hospital.latitude)
            intent.putExtra("h_longitude", hospital.longitude)
            activity?.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}