package org.bmsk.androidcoroutinehandson.ui.main

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.bmsk.androidcoroutinehandson.model.Item

class FavoritesAdapter: RecyclerView.Adapter<ImageSearchViewHolder>() {
    private var items: List<Item> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder =
        ImageSearchViewHolder.create({}, parent)

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }
}