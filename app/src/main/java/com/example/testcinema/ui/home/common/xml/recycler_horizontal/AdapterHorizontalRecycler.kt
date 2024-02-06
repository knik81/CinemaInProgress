package com.example.testcinema.ui.home.common.xml.recycler_horizontal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.R
import com.example.testcinema.databinding.CustomviewRecyclerElementBinding
import com.example.testcinema.databinding.CustomviewRecyclerElementImageBinding
import com.example.testcinema.ui.home.common.xml.ViewHolderUniversal

class AdapterHorizontalRecycler(
    private val value: List<RecyclerItem>,
    private val clickImage: (Int, Int) -> Unit
) : RecyclerView.Adapter<ViewHolderUniversal>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUniversal {
        val binding = CustomviewRecyclerElementBinding.inflate(
            LayoutInflater.from(parent.context)
        )

        return ViewHolderUniversal(binding)
    }

    override fun getItemCount(): Int {
        return value.size
    }

    override fun onBindViewHolder(holder: ViewHolderUniversal, position: Int) {

        // val name =



        holder.binding.textViewRecyclerName.text = value[position].name
        if (value[position].iconUri == "R.drawable.image_go_listpage") {
            holder.binding.imageView.load(R.drawable.image_go_listpage)
            holder.binding.imageFon.visibility = View.VISIBLE
            holder.binding.RatingText.visibility = View.GONE
            holder.binding.RatingFrame.visibility = View.GONE
        } else
            holder.binding.imageView.load(value[position].iconUri)
        holder.binding.textViewRecyclerGenre.text = value[position].genre
        val rating = value[position].rating
        if (rating != null) {
            holder.binding.RatingText.text = rating.toString()
            holder.binding.RatingText.visibility = View.VISIBLE
            holder.binding.RatingFrame.visibility = View.VISIBLE
        }
        holder.binding.imageView.setOnClickListener {
            clickImage(position, value[position].id)
        }




    }
}