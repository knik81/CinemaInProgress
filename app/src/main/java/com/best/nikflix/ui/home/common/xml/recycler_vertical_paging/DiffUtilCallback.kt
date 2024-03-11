package com.best.nikflix.ui.home.common.xml.recycler_vertical_paging

import androidx.recyclerview.widget.DiffUtil
import com.best.entity.ItemApiUniversalInterface


class DiffUtilCallback: DiffUtil.ItemCallback<ItemApiUniversalInterface>() {
    override fun areItemsTheSame(
        oldItem: ItemApiUniversalInterface,
        newItem: ItemApiUniversalInterface
    ): Boolean {
        return oldItem.filmId == newItem.filmId
    }

    override fun areContentsTheSame(
        oldItem: ItemApiUniversalInterface,
        newItem: ItemApiUniversalInterface
    ): Boolean {
        return oldItem.filmId == newItem.filmId
    }
}