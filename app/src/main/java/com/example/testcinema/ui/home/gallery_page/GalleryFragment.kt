package com.example.testcinema.ui.home.gallery_page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.load.ImageHeaderParser.ImageType
import com.example.entity.ApiParameters
import com.example.testcinema.App
import com.example.testcinema.R
import com.example.testcinema.databinding.FragmentGalleryBinding
import com.example.testcinema.ui.home.gallery_page.recycler.AdapterVerticalListImage
import com.example.testcinema.ui.home.list_page.ListPageFragment
import com.example.testcinema.ui.home.picture_page.PictureFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject


class GalleryFragment @Inject constructor() : Fragment() {

    private lateinit var binding: FragmentGalleryBinding

    private val viewModel by viewModels<GalleryFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.galleryFragmentViewModelFactoryProvide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater)
        return binding.root
    }

    private val adapter = AdapterVerticalListImage { posterUrl ->
        openPicturePage(posterUrl)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Создание чипов
        var firstChip: Chip? = null//первая чип/кнопка

        ApiParameters.getImagesTypeParameters().forEach { filter, name ->
            val chip = Chip(requireContext())
            chip.text = name

            //нажатие на чип
            chip.setOnClickListener {
                getImageFromApi(filter)
            }

            val drawable = ChipDrawable.createFromAttributes(
                requireContext(),
                null,
                0,
                com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
            )
            chip.setChipDrawable(drawable)
            binding.ChipGroup.addView(chip)


            //запомнить первую чип/кнопку
            if (firstChip == null) {
                firstChip = chip
                //нажатие на первую кнопку чтобы
                firstChip?.performClick()
            }
        }

        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
            2,
            GridLayoutManager.VERTICAL
        )


        //подписка на ошибку из PagingSourceFilmUniversal для вывода в лог
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                Log.d("Nik", "${(it.refresh as LoadState.Error).error.message}")
            }
        }

        //загрузка адаптера
        binding.recyclerView.adapter = adapter
        binding.textViewLabel.text = label
    }

    //функция открытия фрагмента с PictureFragment
    private fun openPicturePage(posterUrl: String) {
        val bundle = Bundle()
        bundle.putString("posterUrl", posterUrl)
        //запуск фрагмента
        findNavController().navigate(R.id.action_galleryFragment_to_pictureFragment)

        //передача данных в фрагмент film_list
        PictureFragment.newInstance(bundle)
    }

    //подписка на фото из апи
    fun getImageFromApi(imageType: String) {
        lifecycleScope.launch {
            //Log.d("Nik", "launch ")
            viewModel.getPagingData(
                type = ApiParameters.IMAGES.type,
                filters = null,
                id = idFilm,
                imageType = imageType
            ).collect { itemPagingData ->
            }
        }
    }

    companion object {
        private var label: String = ApiParameters.IMAGES.label
        private var idFilm: String? = null

        @JvmStatic
        fun newInstance(args: Bundle) =
            ListPageFragment().apply {
                arguments = Bundle().apply {
                    idFilm = args.getString("idFilm")
                }
            }

    }
}