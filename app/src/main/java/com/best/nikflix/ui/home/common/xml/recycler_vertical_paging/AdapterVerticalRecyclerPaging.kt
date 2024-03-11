package com.best.nikflix.ui.home.common.xml.recycler_vertical_paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.load
import com.best.nikflix.databinding.CustomviewRecyclerElementBinding
import com.best.nikflix.entity.RecyclerItem
import com.best.entity.ItemApiUniversalInterface
import com.best.nikflix.ui.home.common.MapperApiToUi
import com.best.nikflix.ui.home.common.xml.ViewHolderUniversal


class AdapterVerticalRecyclerPaging(
    val clickImage: (Int) -> Unit
) : PagingDataAdapter<ItemApiUniversalInterface, ViewHolderUniversal>(DiffUtilCallback()) {
    override fun onBindViewHolder(holder: ViewHolderUniversal, position: Int) {

        val item: RecyclerItem? = MapperApiToUi.mapperRecyclerVerticalUi(getItem(position))

       // Log.d("Nik", "item  $item")

        if (item != null) {


            if (item.alreadySaw == true)
                holder.binding.imageAlreadySaw.visibility = View.VISIBLE
            else
                holder.binding.imageAlreadySaw.visibility = View.GONE

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

        return ViewHolderUniversal(
            CustomviewRecyclerElementBinding.inflate(LayoutInflater.from(parent.context)),
        )
    }
}