package com.example.testcinema.ui.home.gallery_page.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.load
import com.example.testcinema.entity.RecyclerItem
import com.example.entity.ItemApiUniversalInterface
import com.example.testcinema.databinding.CustomviewRecyclerElementBinding
import com.example.testcinema.databinding.CustomviewRecyclerElementImageBinding
import com.example.testcinema.ui.home.common.MapperApiToUi
import com.example.testcinema.ui.home.common.xml.ViewHolderUniversal
import com.example.testcinema.ui.home.common.xml.recycler_vertical_paging.DiffUtilCallback


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