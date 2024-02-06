package com.example.testcinema.ui.home.common.xml.recycler_horizontal

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.entity.ApiParameters
import com.example.testcinema.entity.RecyclerItem
import com.example.testcinema.databinding.CustomviewHorizRecyclerBinding
import com.example.testcinema.entity.Recycler


class CustomHorizontalRecyclerView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    difStileAttr: Int = 0
) : LinearLayout(context, attrs, difStileAttr) {

    val binding = CustomviewHorizRecyclerBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }


    fun setItem(
        recyclerItemList: List<RecyclerItem>,
        name: String,
        imageClick: (Int, String) -> Unit,
        allClick: () -> Unit,
        recycler: Recycler? = null
    ) {

        //название
        binding.textViewName.text = name

        //обработка клика на надпись "все"
        binding.textViewShowAll.setOnClickListener {
            allClick()
        }

        //скрытьие надпиcи "все", если фильмов меньше 19
        if (recyclerItemList.lastIndex < 19)
            binding.textViewShowAll.visibility = View.GONE

        //замена слова "все" на кол-ва картинок
        if (recycler?.totalItemsFromApi != null)
            binding.textViewShowAll.text = recycler.totalItemsFromApi.toString()

        //создание рекуклера
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )


        val adapterHorizontalRecycler =
            AdapterHorizontalRecycler(recyclerItemList) { position, id ->
                //передача лямды-клика на элемент рекуклера
                if (position > 19)
                    allClick()
                else {
                    recyclerItemList[position].iconUri
                    imageClick(id, recyclerItemList[position].iconUri)
                }
            }

        //загрузка загрузка данных в адаптер
        binding.recyclerView.adapter = adapterHorizontalRecycler
    }

}