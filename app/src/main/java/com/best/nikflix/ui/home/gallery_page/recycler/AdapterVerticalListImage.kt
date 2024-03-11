package com.best.nikflix.ui.home.gallery_page.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.load
import com.best.nikflix.databinding.CustomviewRecyclerElementImageBinding
import com.best.nikflix.entity.RecyclerItem
import com.best.entity.ItemApiUniversalInterface
import com.best.nikflix.ui.home.common.MapperApiToUi


class AdapterVerticalListImage(
    val clickImage: (String) -> Unit
) :
    PagingDataAdapter<ItemApiUniversalInterface, ViewHolderImage>(DiffUtilCallbackImage()) {
    override fun onBindViewHolder(holder: ViewHolderImage, position: Int) {

        val item: RecyclerItem? = MapperApiToUi.mapperRecyclerVerticalUi(getItem(position))

        if (item != null) {
            //Log.d("Nik", "item  $item")
            //загрузка данных в элемент рекуклера
            holder.binding.imageView.load(item.iconUri)
            holder.binding.imageView.setOnClickListener {
                clickImage(item.iconUri)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderImage {

        val viewHolder = ViewHolderImage(
            CustomviewRecyclerElementImageBinding.inflate(LayoutInflater.from(parent.context)),
        )
        return viewHolder
    }
}