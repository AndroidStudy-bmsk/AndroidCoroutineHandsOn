package org.bmsk.androidcoroutinehandson.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.bmsk.androidcoroutinehandson.R
import org.bmsk.androidcoroutinehandson.databinding.ItemImageSearchBinding
import org.bmsk.androidcoroutinehandson.model.Item

open class ImageSearchViewHolder(
    private val like: (Item) -> Unit,
    private val binding: ItemImageSearchBinding
) : RecyclerView.ViewHolder(binding.root) {

    open fun bind(item: Item?) {
        item?.let { item ->
            Glide.with(binding.root)
                .load(item.thumbnail)
                .centerCrop()
                .into(binding.imageView)
            binding.imageView.setOnClickListener {
                like.invoke(item)
            }
        }
    }

    companion object {
        fun create(
            like: (Item) -> Unit,
            parent: ViewGroup
        ): ImageSearchViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_image_search, parent, false)
            val binding = ItemImageSearchBinding.bind(view)
            return ImageSearchViewHolder(like, binding)
        }
    }
}