package com.example.testcinema.ui.home.gallery_page.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.entity.ItemApiUniversalInterface

class DiffUtilCallbackImage: DiffUtil.ItemCallback<ItemApiUniversalInterface>() {
    override fun areItemsTheSame(
        oldItem: ItemApiUniversalInterface,
        newItem: ItemApiUniversalInterface
    ): Boolean {
        return oldItem.posterUrl == newItem.posterUrl
    }

    override fun areContentsTheSame(
        oldItem: ItemApiUniversalInterface,
        newItem: ItemApiUniversalInterface
    ): Boolean {
        return oldItem.posterUrl == newItem.posterUrl
    }
}