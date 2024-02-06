package com.example.testcinema.ui.home.common.xml.recycler_vertical_paging

import android.util.Log
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


class AdapterVerticalList(
    val clickImage: (Int) -> Unit
) :
    PagingDataAdapter<ItemApiUniversalInterface, ViewHolderUniversal>(DiffUtilCallback()) {
    override fun onBindViewHolder(holder: ViewHolderUniversal, position: Int) {

        val item: RecyclerItem? = MapperApiToUi.mapperRecyclerVerticalUi(getItem(position))

        if (item != null) {

            //Log.d("Nik", "item  $item")
            //загрузка данных в элемент рекуклера
            holder.binding.imageView.load(item.iconUri)
            holder.binding.textViewRecyclerName.text = item.name
            holder.binding.textViewRecyclerGenre.text = item.genre
            if (item.rating != null) {
                holder.binding.RatingText.text = item.rating.toString()
                holder.binding.RatingText.visibility = View.VISIBLE
                holder.binding.RatingFrame.visibility = View.VISIBLE
            }

            holder.binding.imageView.setOnClickListener {
                clickImage(item.id)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUniversal {

        val viewHolder = ViewHolderUniversal(
            CustomviewRecyclerElementBinding.inflate(LayoutInflater.from(parent.context)),
        )
        return viewHolder
    }
}