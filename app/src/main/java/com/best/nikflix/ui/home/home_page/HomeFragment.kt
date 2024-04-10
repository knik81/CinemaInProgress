package com.best.nikflix.ui.home.home_page

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.best.entity.ApiParameters
import com.best.entity.Country
import com.best.entity.Genre
import com.best.nikflix.App
import com.best.nikflix.MainActivity.Companion.firstStart
import com.best.nikflix.R
import com.best.nikflix.databinding.FragmentHomePageBinding
import com.best.nikflix.ui.home.common.xml.recycler_horizontal.CustomHorizontalRecyclerView
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.home.list_page.ListPageFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomePageBinding? = null
    private val binding get() = _binding!!

    private val reloadStateFlow = MutableStateFlow(false)

    private val viewModel: HomeFragmentViewModel by viewModels<HomeFragmentViewModel> {
        (requireContext().applicationContext as App)
            .appComponent.allFilmsViewModelFactoryProvide()
    }

    private var isError = false
    //private var textViewCinema: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomePageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //отображение экрана загрузки
                launch {
                    reloadStateFlow.collect {
                        //Log.d("Nik", "isError $isError")
                        //  Log.d("Nik", "firstStart = $firstStart")
                        if (!isError)
                            binding.ButtonReloading.visibility = View.GONE
                        else
                            binding.ButtonReloading.visibility = View.VISIBLE
                        if (it) {
                            if (firstStart) {
                                //отрисовка картинки загрузки
                                binding.LoadingFrame.visibility = View.VISIBLE
                                binding.LinearLayout.visibility = View.GONE
                                delay(2000)
                            }
                            loadData()
                            reloadStateFlow.value = false

                            //задержка только для красоты
                            //загрузки данных из инета идет очень быстро, а задержка поможет
                            //хоть недолго картинку загрузки показать
                            binding.LoadingFrame.visibility = View.GONE
                            binding.LinearLayout.visibility = View.VISIBLE
                            firstStart = false
                        }
                    }
                }


                //----------------------------------------

                //Премьер - подписка на данные из АПИ
                launch {
                    viewModel.premieresStateFlow.collect { itemListFilm ->
                        if (itemListFilm != null) {
                            viewModel.checkAlreadySaw(itemListFilm, ApiParameters.PREMIERS)
                        }
                    }
                }
                launch {
                    viewModel.premieresRecyclerItemStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
                            premiersCustomView.visibility = View.VISIBLE
                            premiersCustomView.setItem(
                                recyclerItemList = itemList,
                                name = com.best.entity.ApiParameters.PREMIERS.label,
                                { id, _ ->
                                    // Log.d("Nik", "imageClick  $id")
                                    //обращение к функции открытия фрагмента с film для Премьеры
                                    openFilmPage(id)
                                },
                                {
                                    //обращение к функции открытия фрагмента с film_list для Премьеры
                                    openListPage(com.best.entity.ApiParameters.PREMIERS.label)
                                })
                        }
                    }
                }

                //Популярные - подписка на данные из АПИ
                launch {
                    viewModel.popularStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty())
                            viewModel.checkAlreadySaw(itemList, ApiParameters.POPULAR)
                    }
                }
                launch {
                    viewModel.popularRecyclerItemStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
                            popularsCustomView.visibility = View.VISIBLE
                            popularsCustomView.setItem(
                                recyclerItemList = itemList,
                                name = com.best.entity.ApiParameters.POPULAR.label,
                                { id, _ ->
                                    //Log.d("Nik", "imageClick  $id")
                                    //обращение к функции открытия фрагмента с film для Популярное
                                    openFilmPage(id)
                                },
                                {
                                    //обращение к функции открытия фрагмента с film_list для Популярное
                                    openListPage(com.best.entity.ApiParameters.POPULAR.label)
                                }
                            )
                        }
                    }
                }


                //топ250 - подписка на данные из АПИ
                launch {
                    viewModel.top250StateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty())
                            viewModel.checkAlreadySaw(itemList, ApiParameters.TOP250)
                    }
                }
                launch {
                    viewModel.top250RecyclerItemStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
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
                launch {
                    //Случайный 1
                    viewModel.randomFilm1StateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
                            viewModel.checkAlreadySaw(
                                itemList,
                                ApiParameters.RANDOM_FILMS,
                                first = true
                            )
                        }
                    }
                }
                launch {
                    viewModel.randomFilm1RecyclerItemStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
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
                                        genre = viewModel.randomFilters1?.first,
                                        country = viewModel.randomFilters1?.second,
                                        // filters = viewModel.randomFilters1
                                    )
                                }
                            )
                        }
                    }

                }


                //Случайный 2 - подписка на данные из АПИ
                launch {
                    viewModel.randomFilm2StateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
                            viewModel.checkAlreadySaw(
                                itemList,
                                ApiParameters.RANDOM_FILMS,
                                first = false
                            )
                        }
                    }

                }
                launch {
                    //Случайный 2 - получение
                    viewModel.randomFilm2RecyclerItemStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
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
                                        genre = viewModel.randomFilters2?.first,
                                        country = viewModel.randomFilters2?.second,
                                        // filters = viewModel.randomFilters2
                                    )
                                }
                            )
                        }
                    }
                }

                //Сериалы - подписка на данные из АПИ
                launch {
                    viewModel.seriesStateFlow.collect { itemList ->
                        if (!itemList.isNullOrEmpty()) {
                            viewModel.checkAlreadySaw(itemList, ApiParameters.SERIES)
                        }
                    }
                }
                launch {
                    viewModel.seriesRecyclerItemStateFlow.collect { itemList ->
                        if (itemList != null) {
                            serialsCustomView.visibility = View.VISIBLE
                            serialsCustomView.setItem(
                                recyclerItemList = itemList,
                                name = ApiParameters.SERIES.label,
                                { id, _ ->
                                    Log.d("Nik", "imageClick  $id")
                                    //обращение к функции открытия фрагмента с film для случ.фильм2
                                    openFilmPage(id)
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
                launch {
                    viewModel.errorStateFlow.collect { error ->
                        if (error != null) {
                            Log.d("Nik", "$error")
                            Snackbar.make(binding.MainFrame, "$error", Snackbar.LENGTH_LONG)
                                .show()

                            if (error.contains("java.net.UnknownHostException: Unable to resolve host")
                                && !viewModel.stateLoadStateFlow.value
                            ) {
                                isError = true
                                delay(2000)
                                Log.d("Nik", "error = $error")
                                Log.d(
                                    "Nik",
                                    "state = ${!viewModel.stateLoadStateFlow.value}"
                                )
                                binding.ButtonReloading.visibility = View.VISIBLE

                            }
                        }
                    }
                }
            }
        }

        if (firstStart)
            reloadStateFlow.value = true

        //одновить данные
        binding.ButtonReloading.setOnClickListener {
            firstStart = true
            isError = false
            reloadStateFlow.value = true
            //viewLogic()

        }

        /*
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
         */
    }

    private fun loadData() {
        if (firstStart) {
            binding.ButtonReloading.visibility = View.GONE

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
            //firstStart = false
            //viewModel.firstStart = false

        }
    }

    //функция /открытия фрагмента с list_page
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openListPage(
        label: String,
        country: Country? = null,
        genre: Genre? = null,
        //filters: Filters? = null
    ) {

        val bundle = Bundle()
        //загрузка данных для фрагмента list_page
        bundle.putString("label", label)
        bundle.putParcelable("country", country)
        bundle.putParcelable("genre", genre)
        //bundle.putParcelable("filters", filters)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_home_to_filmListPageFragment
        )
        //передача данных в фрагмент list_page
        ListPageFragment.newInstance(bundle)

    }

    //функция /открытия фрагмента с film_page
    private fun openFilmPage(id: Int) {
        // textViewCinema?.visibility = View.GONE

        val bundle = Bundle()
        //загрузка данных для фрагмента film_page
        bundle.putInt("id", id)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_home_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)

    }


    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.GONE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.VISIBLE
        val bottomNavView = activity?.findViewById<View>(R.id.nav_view)
        bottomNavView?.visibility = View.VISIBLE
    }
}