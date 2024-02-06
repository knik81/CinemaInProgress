package com.example.testcinema.ui.home.list_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.entity.ApiParameters
import com.example.entity.Country
import com.example.entity.Filters
import com.example.entity.Genre
import com.example.testcinema.App
import com.example.testcinema.R
import com.example.testcinema.databinding.FragmentListPageBinding
import com.example.testcinema.ui.home.common.xml.recycler_vertical_paging.AdapterVerticalList
import com.example.testcinema.ui.home.film_page.FilmFragment
import com.example.testcinema.ui.home.person_page.PersonFragment
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListPageFragment @Inject constructor(
) : Fragment() {

    private val viewModel: ListPageFragmentViewModel by viewModels {
        (requireContext().applicationContext as App).appComponent.listPageViewModelFactoryProvide()
    }
    private lateinit var binding: FragmentListPageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdapterVerticalList { id ->
            //Log.d("Nik", "imageClick  $id")
            if (label == ApiParameters.ACTOR.label || label == ApiParameters.STAFF.label)
                openPersonPage(id.toString())
            else
                openFilmPage(id)
        }
        if (label == ApiParameters.IMAGES.label)
            binding.recyclerView.layoutManager = StaggeredGridLayoutManager(
                2,
                GridLayoutManager.VERTICAL
            )
        else
            binding.recyclerView.layoutManager = GridLayoutManager(
                requireContext(),
                2,
            )

        //получение параметра фильма по заголовку
        val apiParameter = ApiParameters.getFilmTypesByLabel(label ?: "")


        //подписка на фильмы из апи
        lifecycleScope.launch {
            //binding.progressBar.visibility = View.VISIBLE
            viewModel.getPagingData(
                type = apiParameter.type,
                filters = filters,
                id = idFilm
            ).collect { itemPagingData ->

                //binding.progressBar.visibility = View.GONE
                //Log.d("Nik", "itemPagingData  $itemPagingData")
                adapter.submitData(itemPagingData)
                //Log.d("Nik", "itemPagingData  $itemPagingData")
                //binding.progressBar.visibility = View.GONE
            }
        }



        //подписка на ошибку из PagingSourceFilmUniversal для вывода в лог
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                Log.d("Nik", "${(it.refresh as LoadState.Error).error.message}")
            }
        }

        //загрузка адаптера
        binding.recyclerView.adapter = adapter
        binding.textViewName.text = label
    }


    companion object {
        private var label: String? = null
        private var idFilm: String? = null
        private var country: Country? = null
        private var genre: Genre? = null
        private var filters: Filters? = null

        @JvmStatic
        fun newInstance(args: Bundle) =
            ListPageFragment().apply {
                arguments = Bundle().apply {
                    label = args.getString("label")
                    idFilm = args.getString("idFilm")
                    country = args.getParcelable("country", Country::class.java)
                    genre = args.getParcelable("genre", Genre::class.java)
                    filters = args.getParcelable("filters", Filters::class.java)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
    }

    private fun openFilmPage(id: Int) {
        val bundle = Bundle()
        bundle.putInt("id", id)
        if (label != ApiParameters.IMAGES.label) {
            findNavController().navigate(
                R.id.action_filmListPageFragment_to_filmFragment
            )
            FilmFragment.newInstance(bundle)
        }
    }

    //функция /открытия фрагмента с ActorPage
    private fun openPersonPage(idActor: String) {
        val bundle = Bundle()

        // Log.d("Nik","${ApiParameters.PERSON.label} idActor = $idActor")
        bundle.putString(ApiParameters.PERSON.label, idActor)
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmListPageFragment_to_personFragment)

        //передача данных в фрагмент film_list
        PersonFragment.newInstance(bundle)
    }
}