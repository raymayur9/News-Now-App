package com.example.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.newsfresh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fetchData()
        mAdapter = ItemAdapter(this, this)
        binding.recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=17922da4d5bb4b23b8a02bb57e31a6ca"
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, url, null,
            {response ->
                val newsJsonArray = response.getJSONArray("articles")
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
            },
            {Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show() })

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
}