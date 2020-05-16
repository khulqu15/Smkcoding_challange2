package com.example.latihan_challange2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihan_challange2.data.CovidTrackerService
import com.example.latihan_challange2.data.apiRequest
import com.example.latihan_challange2.util.dismissLoading
import com.example.latihan_challange2.util.showLoading
import com.example.latihan_challange2.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_tracker.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackerFragment : Fragment() {

    companion object {
        val INTENT_PARCELABLE = "OBJECT_INTENT_TRACKER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiGetTracker()
    }

    private fun callApiGetTracker() {
        showLoading(context!!, swipeRefreshLayout)
        
        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<CovidTrackerService>(httpClient)

        val call = apiRequest.getTracker()
        call.enqueue(object : Callback<List<ApiTrackerItem>> {
            override fun onFailure(call: Call<List<ApiTrackerItem>>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<List<ApiTrackerItem>>,
                response: Response<List<ApiTrackerItem>>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body()?.size != 0 ->
                                displayTracker(response.body()!!)

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
    
    private fun displayTracker(Tracker: List<ApiTrackerItem>) {
        rv_listTracker.layoutManager = LinearLayoutManager(context)
        rv_listTracker.adapter = TrackerAdapter(context!!, Tracker) {
            val intent = Intent(activity, DetailTrackerActivity::class.java)
                intent.putExtra("prov", it.provinsi)
                intent.putExtra("confirmed", it.confirmed)
                intent.putExtra("recovered", it.recovered)
                intent.putExtra("death", it.deaths)
                intent.putExtra("case", it.activeCases)
                intent.putExtra("date", it.updateDate)
                intent.putExtra("latitude", it.latitude)
                intent.putExtra("longitude", it.longitude)
                intent.putExtra("logo", it.logo)
            activity?.startActivity(intent)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}