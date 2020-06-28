package com.example.latihan_challange2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihan_challange2.util.tampilToast
import com.example.latihan_challange2.viewmodel.ArticleFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabaseArticles: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    var dataArticles: MutableList<Articles> = ArrayList()
    private val viewModel by viewModels<ArticleFragmentViewModel>()
    private var adapter: ArticleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init()
        viewModel.init(requireContext())
        viewModel.allArticles.observe(viewLifecycleOwner, Observer { articels ->
            articels?.let { adapter?.setData(it) }
        })


        addArticle.setOnClickListener {
            val intent = Intent(context, AddArticleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init() {
        rv_listArticles.layoutManager = LinearLayoutManager(context)
        adapter = ArticleAdapter(requireContext(), dataArticles)
        rv_listArticles.adapter = adapter
        adapter?.list = dataArticles
    }


    private fun initialize() {
//        showLoading(requireContext(), swipeRefreshLayout)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth!!.currentUser
        mDatabaseReference = mDatabase!!.reference.child("Users").child(user!!.uid)
        mDatabaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                activity?.let { tampilToast(it, "Gagal Error") }
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val email = snapshot.child("email").getValue().toString()
                if(email != "admin@sidescript.com") {
                    addArticle.hide()
                }
            }
        })
        mDatabaseArticles = mDatabase!!.reference.child("Articles")
        mDatabaseArticles!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                activity?.let { tampilToast(it, "Gagal Error") }
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                dismissLoading(swipeRefreshLayout)
                dataArticles = ArrayList<Articles>()
                for (snapshot in dataSnapshot.children) {
                    val article = snapshot.getValue(Articles::class.java)
                    dataArticles.add(article!!)
                }
                rv_listArticles.layoutManager = LinearLayoutManager(context)
                rv_listArticles.adapter = ArticleAdapter(context!!,
                    dataArticles as ArrayList<Articles>
                )

                dataArticles = ArrayList()
                for (snapshot in dataSnapshot.children) {
                    val article = snapshot.getValue(Articles::class.java)
                    article?.article_key = (snapshot.key!!)
                    dataArticles.add(article!!)
                }
                viewModel.insertAll(dataArticles)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}


