package com.example.latihan_challange2

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
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiHomeTracker()
    }

    private fun callApiHomeTracker() {
        showLoading(context!!, swipeRefreshLayout)

        val httpClient = OkHttpClient()
        val apiRequest = apiRequest<CovidTrackerService>(httpClient)

        val call = apiRequest.getHomeTracker()
        call.enqueue(object : Callback<ApiIndonesiaCovid> {
            override fun onFailure(call: Call<ApiIndonesiaCovid>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
            }

            override fun onResponse(
                call: Call<ApiIndonesiaCovid>,
                response: Response<ApiIndonesiaCovid>
            ) {
                dismissLoading(swipeRefreshLayout)
                when {
                    response.isSuccessful ->
                        when {
                            response.body() != null ->
                                displayHomeTracker(response.body()!!)
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

    private fun displayHomeTracker(HomeTracker: ApiIndonesiaCovid) {
        rv_listHome.layoutManager = LinearLayoutManager(context)
        rv_listHome.adapter = HomeAdapter(context!!, HomeTracker) {
            val homeTracker = it
            tampilToast(context!!, homeTracker.country)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

}