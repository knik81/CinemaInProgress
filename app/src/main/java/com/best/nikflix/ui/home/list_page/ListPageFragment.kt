package com.best.nikflix.ui.home.list_page

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.best.nikflix.R
import com.best.nikflix.databinding.FragmentListPageBinding
import com.best.entity.ApiParameters
import com.best.entity.Country
import com.best.entity.Genre
import com.best.entity.QueryParams
import com.best.nikflix.App
import com.best.nikflix.ui.home.common.xml.recycler_horizontal.AdapterHorizontalRecycler
import com.best.nikflix.ui.home.common.xml.recycler_vertical_paging.AdapterVerticalRecyclerPaging
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.home.person_page.PersonFragment
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
    ): View {
        binding = FragmentListPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdapterVerticalRecyclerPaging { id ->
            //Log.d("Nik", "imageClick  $id")
            //нажатие на элемент списка
            if (label == ApiParameters.ACTOR.label || label == ApiParameters.STAFF.label)
                openPersonPage(id.toString())
            else
                openFilmPage(id)
        }

        //выбор лайаута
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

        //получение параметра по заголовку
        val apiParameter = ApiParameters.getFilmTypesByLabel(label ?: "")

        //подписка на фильмы из апи или обращение в апи за фильмами из профиля
        if (!isProfile)
            lifecycleScope.launch {
                val queryParams = QueryParams(
                    genres = genre ?: Genre(),
                    countries = country ?: Country(),
                )
                viewModel.getPagingData(
                    type = apiParameter.type,
                    queryParams = queryParams,
                    id = idFilm
                ).collect { itemPagingData ->
                    adapter.submitData(itemPagingData)
                }
            }
        else//фильмы из коллекций с закладки профиль
            viewModel.getFilms(idFilList)//

        //загрузка загрузка фильмов из профиля в адаптер
        lifecycleScope.launch {
            viewModel.filmListStateFlow.collect { itemApiUniversalList ->
                //Log.d("Nik", "itemApiUniversalList  $itemApiUniversalList")
                if (!itemApiUniversalList.isNullOrEmpty()) {
                    binding.recyclerView.adapter = AdapterHorizontalRecycler(itemApiUniversalList) { _, id ->
                        //нажатие на элемент списка
                        openFilmPage(id)
                    }
                }
            }
        }


        //подписка на ошибку из PagingSourceFilmUniversal для вывода в лог
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                Log.d("Nik", "${(it.refresh as LoadState.Error).error.message}")
            }
        }

        //загрузка адаптера
        if (!isProfile)
            binding.recyclerView.adapter = adapter
        //binding.recyclerView.adapter = filmCustomRecyclerView
        binding.textViewName.text = label
    }


    companion object {
        private var label: String? = null
        private var idFilm: String? = null
        private var country: Country? = null
        private var genre: Genre? = null
        private val idFilList = mutableListOf<String>()
        private var isProfile: Boolean = false

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @JvmStatic
        fun newInstance(args: Bundle) =
            ListPageFragment().apply {
                arguments = Bundle().apply {
                    label = args.getString("label")
                    idFilm = args.getString("idFilm")
                    country = args.getParcelable("country")
                    genre = args.getParcelable("genre")
                    /*
                    //ошибка на старых версиях
                    //проверку версии делать пока не хочу
                    country = args.getParcelable("country", Country::class.java)
                    genre = args.getParcelable("genre", Genre::class.java)
                     */
                    isProfile = args.getBoolean("isProfile")

                    val test = args.getStringArrayList("idFilmArrayList")
                    if (!test.isNullOrEmpty()) {
                        idFilList.clear()
                        idFilList.addAll(test.toList())
                    }
                }
            }
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

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.GONE
    }
}