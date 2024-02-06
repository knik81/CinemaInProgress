package com.example.testcinema.ui.home.home_page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.entity.ApiParameters
import com.example.entity.Country
import com.example.entity.Filters
import com.example.entity.Genre
import com.example.testcinema.App
import com.example.testcinema.R
import com.example.testcinema.databinding.FragmentHomePageBinding
import com.example.testcinema.ui.home.common.xml.recycler_horizontal.CustomHorizontalRecyclerView
import com.example.testcinema.ui.home.film_page.FilmFragment
import com.example.testcinema.ui.home.list_page.ListPageFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class HomeFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!


    private val viewModel: HomeFargmentViewModel by viewModels<HomeFargmentViewModel> {
        (requireContext().applicationContext as App)
            .appComponent.allFilmsViewModelFactoryProvide()
    }

    var textViewCinema: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.GONE
        textViewCinema = activity?.findViewById(R.id.textViewCinema)
        textViewCinema?.visibility = View.VISIBLE

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //создание кастомных элементов - рекуклеров
        //добавление их на экран
        //пермьеры
        val premiersCustomView = CustomHorizontalRecyclerView(requireContext())
        premiersCustomView.visibility = View.GONE
        binding.LinearLayout.addView(premiersCustomView)

        //популярные
        val popularsCustomView = CustomHorizontalRecyclerView(requireContext())
        popularsCustomView.visibility = View.GONE
        binding.LinearLayout.addView(popularsCustomView)

        //случайный 1
        val random1CustomView = CustomHorizontalRecyclerView(requireContext())
        random1CustomView.visibility = View.GONE
        binding.LinearLayout.addView(random1CustomView)

        //случайный 2
        val random2CustomView = CustomHorizontalRecyclerView(requireContext())
        random2CustomView.visibility = View.GONE
        binding.LinearLayout.addView(random2CustomView)

        //Топ 250
        val top250CustomView = CustomHorizontalRecyclerView(requireContext())
        top250CustomView.visibility = View.GONE
        binding.LinearLayout.addView(top250CustomView)

        //Сериалы
        val serialsCustomView = CustomHorizontalRecyclerView(requireContext())
        serialsCustomView.visibility = View.GONE
        binding.LinearLayout.addView(serialsCustomView)

        //-------------------------------------------------
        if (viewModel.firstStart) {
            //Премьер - обращение к АПИ
            viewModel.getPremiers()
            //Случайный 1 и 2 - обращение к АПИ
            viewModel.getRandom(first = true)
            viewModel.getRandom(first = false)
            //Поплурные - обращение к АПИ
            viewModel.getPopular()
            //топ250 - обращение к АПИ
            viewModel.getTop250()
            //Сериалы
            viewModel.getSeries()

            viewModel.firstStart = false
        }
        textViewCinema?.visibility = View.GONE
        //----------------------------------------
        //Премьер - подписка на данные из АПИ
        lifecycleScope.launch {
            viewModel.premieresStateFlow.collect { itemListFilm ->
                if (itemListFilm != null) {
                    premiersCustomView.visibility = View.VISIBLE
                    premiersCustomView.setItem(
                        recyclerItemList = itemListFilm,
                        name = com.example.entity.ApiParameters.PREMIERS.label,
                        { id, _ ->
                           // Log.d("Nik", "imageClick  $id")
                            //обращение к функции открытия фрагмента с film для Премьеры
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для Премьеры
                            openListPage(com.example.entity.ApiParameters.PREMIERS.label)
                        })
                }
            }
        }

        //Популярные - подписка на данные из АПИ
        lifecycleScope.launch {
            viewModel.popularStateFlow.collect { itemList ->
                if (itemList != null) {
                    popularsCustomView.visibility = View.VISIBLE
                    popularsCustomView.setItem(
                        recyclerItemList = itemList,
                        name = com.example.entity.ApiParameters.POPULAR.label,
                        { id, _ ->
                            //Log.d("Nik", "imageClick  $id")
                            //обращение к функции открытия фрагмента с film для Популярное
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для Популярное
                            openListPage(com.example.entity.ApiParameters.POPULAR.label)
                        }
                    )
                }
            }
        }


        //топ250 - подписка на данные из АПИ
        lifecycleScope.launch {
            viewModel.top250StateFlow.collect { itemList ->
                if (itemList != null) {
                    top250CustomView.visibility = View.VISIBLE
                    top250CustomView.setItem(
                        recyclerItemList = itemList,
                        name = ApiParameters.TOP250.label,
                        { id, _ ->
                            //Log.d("Nik", "imageClick  $id")
                            //обращение к функции открытия фрагмента с film для ТОП250
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для ТОП250
                            openListPage(ApiParameters.TOP250.label)
                        }
                    )
                }
            }
        }


        //Случайный 1 - подписка на данные из АПИ
        lifecycleScope.launch {
            //Случайный 1
            viewModel.randomFilm1StateFlow.collect { itemList ->
                if (itemList != null) {
                    random1CustomView.visibility = View.VISIBLE
                    random1CustomView.setItem(
                        recyclerItemList = itemList,
                        name = viewModel.labelRandomFilm1,
                        { id, _ ->
                            //Log.d("Nik", "imageClick  $id")
                            //обращение к функции открытия фрагмента с film для случ.фильм1
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для случ.фильм1
                            openListPage(
                                label = viewModel.labelRandomFilm1,
                                filters = viewModel.randomFilters1
                            )
                        }
                    )
                }
            }
        }

        //Случайный 2 - подписка на данные из АПИ
        lifecycleScope.launch {
            //Случайный 2 - получение
            viewModel.randomFilm2StateFlow.collect { itemList ->
                if (itemList != null) {
                    random2CustomView.visibility = View.VISIBLE
                    random2CustomView.setItem(
                        recyclerItemList = itemList,
                        name = viewModel.labelRandomFilm2,
                        { id, _ ->
                            //Log.d("Nik", "imageClick  $id")
                            //обращение к функции открытия фрагмента с film для случ.фильм2
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для случ.фильм2
                            openListPage(
                                label = viewModel.labelRandomFilm2,
                                filters = viewModel.randomFilters2
                            )
                        }
                    )
                }
            }
        }

        //Сериалы - подписка на данные из АПИ
        lifecycleScope.launch {
            viewModel.seriesStateFlow.collect { itemList ->
                if (itemList != null) {
                    serialsCustomView.visibility = View.VISIBLE
                    serialsCustomView.setItem(
                        recyclerItemList = itemList,
                        name = ApiParameters.SERIES.label,
                        { id, _ ->
                            Log.d("Nik", "imageClick  $id")
                        },
                        {
                            //обращение к функции открытия фрагмента с film_list для Серииалы
                            openListPage(ApiParameters.SERIES.label)
                        }
                    )
                }
            }


        }

        //-------------------------------------------------
        //подписка на вывод ошибки
        lifecycleScope.launch {
            viewModel.errorStateFlow.collect { error ->
                if (error != null)
                    Snackbar.make(binding.swipeHome, "$error", Snackbar.LENGTH_LONG).show()
            }
        }

        //-------------------------------------------------
        //обновление данных свайпом вниз
        binding.swipeHome.setOnRefreshListener {
            viewModel.getPremiers()
            viewModel.getPopular()
            viewModel.getTop250()
            viewModel.getRandom(first = true)
            viewModel.getRandom(first = false)
            viewModel.getSeries()
            binding.swipeHome.isRefreshing = false
        }

    }

    //функция /открытия фрагмента с list_page
    private fun openListPage(
        label: String,
        country: Country? = null,
        genre: Genre? = null,
        filters: Filters? = null
    ) {
        textViewCinema?.visibility = View.GONE

        val bundle = Bundle()
        //загрузка данных для фрагмента list_page
        bundle.putString("label", label)
        bundle.putParcelable("country", country)
        bundle.putParcelable("genre", genre)
        bundle.putParcelable("filters", filters)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_home_to_filmListPageFragment
        )
        //передача данных в фрагмент list_page
        ListPageFragment.newInstance(bundle)

    }

    //функция /открытия фрагмента с film_page
    private fun openFilmPage(id: Int) {
        textViewCinema?.visibility = View.GONE

        val bundle = Bundle()
        //загрузка данных для фрагмента film_page
        bundle.putInt("id",id)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_home_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)

    }

}