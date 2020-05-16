package com.example.latihan_challange2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.trackers_item.*

class TrackerAdapter(private val context: Context, private val items: List<ApiTrackerItem>,
                     private val listener: (ApiTrackerItem) -> Unit) :
    RecyclerView.Adapter<TrackerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.trackers_item, parent, false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View):
            RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: ApiTrackerItem, listener: (ApiTrackerItem) -> Unit) {
            txtCountryName.text = item.provinsi
            txtDeathCovid.text = item.deaths.toString()
            txtPositiveCovid.text = item.confirmed.toString()
            txtRecoveredCovid.text = item.recovered.toString()

            Glide.with(context).load(item.logo).into(imgData)

            containerView.setOnClickListener { listener(item) }
        }

    }
}