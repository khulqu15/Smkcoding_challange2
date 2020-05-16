package com.example.latihan_challange2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.hospital_item.*

class HospitalAdapter(private val context: Context, private val items: List<ApiHospitalItem>,
private val listener: (ApiHospitalItem) -> Unit) :
        RecyclerView.Adapter<HospitalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.hospital_item, parent, false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: HospitalAdapter.ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(item: ApiHospitalItem, listener: (ApiHospitalItem) -> Unit) {
            txtHospitalName.text = item.nama
            txtHospitalProv.text = item.provinsi
            txtHospitalCity.text = item.kotakab

            containerView.setOnClickListener { listener(item) }
        }
    }
}