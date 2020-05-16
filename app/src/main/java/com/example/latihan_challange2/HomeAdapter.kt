package com.example.latihan_challange2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_item.*

class HomeAdapter(private val context: Context, private val items: ApiIndonesiaCovid,
                  private val listener: (ApiIndonesiaCovid) -> Unit) :
        RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder( context, LayoutInflater.from(context).inflate(R.layout.home_item, parent, false))

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items, listener)
    }

    class ViewHolder(val context: Context, override val containerView: View):
    RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: ApiIndonesiaCovid, listener: (ApiIndonesiaCovid) -> Unit) {
            txtPositiveCovid.text = item.confirmed.toString()
            txtRecoveredCovid.text = item.recovered.toString()
            txtDeathCovid.text = item.deaths.toString()

            containerView.setOnClickListener { listener(item) }
        }
    }
}