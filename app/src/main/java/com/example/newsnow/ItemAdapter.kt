package com.example.newsnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(
    private val listener: NewsItemClicked
): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val dataset: ArrayList<News> = ArrayList()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val image: ImageView = view.findViewById(R.id.image)
        val author: TextView = view.findViewById(R.id.author)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)

        val viewHolder = ItemViewHolder(adapterLayout)
        adapterLayout.setOnClickListener{
            listener.onItemClicked(dataset[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.title.text = item.title
        if (item.author=="null") item.author = ""
        holder.author.text = item.author
        Glide.with(holder.itemView.context).load(item.imageUrl).into(holder.image)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = dataset.size

    fun updateNews(updatedNews: ArrayList<News>)
    {
        dataset.clear()
        dataset.addAll(updatedNews)

        notifyDataSetChanged()
    }
}

interface NewsItemClicked {
    fun onItemClicked(item: News)
}