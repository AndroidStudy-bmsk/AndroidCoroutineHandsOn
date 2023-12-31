package org.bmsk.androidcoroutinehandson.ui.main

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import org.bmsk.androidcoroutinehandson.model.Item

class ImageSearchAdapter(
    private val like: (Item) -> Unit,
): PagingDataAdapter<Item, ImageSearchViewHolder>(comparator) {

    override fun onBindViewHolder(holder: ImageSearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSearchViewHolder {
        return ImageSearchViewHolder.create(like, parent)
    }

    companion object {
        val comparator = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
               return oldItem.thumbnail == newItem.thumbnail
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}