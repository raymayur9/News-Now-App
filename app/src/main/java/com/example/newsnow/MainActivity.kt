package com.example.newsnow

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsnow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
        fetchData(url)
        binding.all.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
            url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        binding.business.setOnClickListener{
//            binding.progressBar.visibility = View.VISIBLE
            url = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        binding.entertainment.setOnClickListener{
            url = "https://newsapi.org/v2/top-headlines?country=in&category=entertainment&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }

        binding.health.setOnClickListener{
            url = "https://newsapi.org/v2/top-headlines?country=in&category=health&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        binding.science.setOnClickListener{
            url = "https://newsapi.org/v2/top-headlines?country=in&category=science&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        binding.sports.setOnClickListener{
            url = "https://newsapi.org/v2/top-headlines?country=in&category=sports&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        binding.technology.setOnClickListener{
            url = "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
            fetchData(url)
        }
        mAdapter = ItemAdapter(this)
        binding.recyclerView.adapter = mAdapter
    }

    private fun fetchData(url: String) {
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            },
            {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show()
            })

        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
    override fun onShareClicked(item: News) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "${item.title}\n${item.url}")
        val chooser = Intent.createChooser(intent, "Share this news using...")
        startActivity(chooser)
    }

    override fun onTopClicked() {
//        Toast.makeText(this, "Button clicked", Toast.LENGTH_LONG).show()
        binding.recyclerView.smoothScrollToPosition(0)
    }
}