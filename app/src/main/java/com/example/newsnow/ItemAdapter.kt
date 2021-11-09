package com.example.newsnow

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ItemAdapter(
    private val listener: NewsItemClicked
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataset: ArrayList<News> = ArrayList()
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemNewsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val image: ImageView = view.findViewById(R.id.image)
        val author: TextView = view.findViewById(R.id.author)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarCard)
        val shareButton: ImageButton = view.findViewById(R.id.share)
    }

    class ItemButtonViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val topButton: Button = view.findViewById(R.id.top_button)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        if (viewType == R.layout.item_button)
        {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_button, parent, false)
            return ItemButtonViewHolder(adapterLayout)
        }
        else
        {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)

            val viewHolder = ItemNewsViewHolder(adapterLayout)
            adapterLayout.setOnClickListener{
                listener.onItemClicked(dataset[viewHolder.adapterPosition])
            }
            return viewHolder
        }
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == dataset.size)
        {
            holder as ItemButtonViewHolder
            holder.topButton.setOnClickListener{
                listener.onTopClicked()
            }
        }
        else
        {
            holder as ItemNewsViewHolder
            val item = dataset[position]
            holder.title.text = item.title
            if (item.author=="null") item.author = ""
            holder.author.text = item.author
            Glide.with(holder.itemView.context).load(item.imageUrl).listener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.progressBar.visibility = View.GONE
                    return false
                }

            }).into(holder.image)

            holder.shareButton.setOnClickListener{
                listener.onShareClicked(item)
            }
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == dataset.size) R.layout.item_button else R.layout.item_news
    }

    fun updateNews(updatedNews: ArrayList<News>)
    {
        dataset.clear()
        dataset.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
    fun onShareClicked(item: News)
    fun onTopClicked()
}