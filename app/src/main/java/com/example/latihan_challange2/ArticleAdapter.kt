package com.example.latihan_challange2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.article_item.*

class ArticleAdapter(private val context: Context, var list: ArrayList<Articles>):
            RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.article_item, parent, false))

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ArticleAdapter.ViewHolder, position: Int) {
        holder.bindItem(list.get(position), list)
        lateinit var ref: DatabaseReference
        lateinit var auth: FirebaseAuth

        holder.containerView.setOnClickListener { view ->
            val title: String = list[position].title
            val description: String = list[position].description
            val location: String = list[position].location
            val created: String = list[position].created_at
            val key: String = list[position].article_key
            val bundle = Bundle()
            bundle.putString("title", title)
            bundle.putString("description", description)
            bundle.putString("location", location)
            bundle.putString("created", created)
            bundle.putString("key", key)
            val intent = Intent(view.context, DetailArticleActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    fun setData(it: List<Articles>) {

    }

    class ViewHolder(val context: Context, override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(
            item: Articles,
            list: ArrayList<Articles>
        ) {
            txtTitle.text = item.title
            txtLocation_news.text = item.location
            txtCreated.text = item.created_at
        }
    }

}