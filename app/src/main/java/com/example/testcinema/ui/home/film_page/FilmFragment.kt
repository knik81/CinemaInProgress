package com.example.testcinema.ui.home.film_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.entity.ApiParameters
import com.example.testcinema.App
import com.example.testcinema.R
import com.example.testcinema.databinding.FragmentFilmPageBinding
import com.example.testcinema.ui.home.common.xml.recycler_horizontal.CustomHorizontalRecyclerView
import com.example.testcinema.ui.home.gallery_page.GalleryFragment
import com.example.testcinema.ui.home.list_page.ListPageFragment
import com.example.testcinema.ui.home.person_page.PersonFragment
import com.example.testcinema.ui.home.picture_page.PictureFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FilmFragment : Fragment() {

    val viewModel: FilmFragmentViewModel by viewModels<FilmFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.filmFragmentViewModelFactoryProvide()
    }

    var firstLoad = true

    private lateinit var binding: FragmentFilmPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilmPageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var isCollapsedDescription = true


        //обращение к апи
        if (firstLoad) {
            viewModel.getFilm(idFilm) //данные фильма
            viewModel.getStaff(idFilm) //Персонал
            viewModel.getImages(idFilm) //картинки
            viewModel.getSimilars(idFilm) //похожие фильмы
            firstLoad = false
        }
        //Log.d("Nik", idFilm.toString())
        //подписка на данные фильма из апи
        lifecycleScope.launch {
            viewModel.filmStateFlow.collect { filmUi ->
                if (filmUi != null) {

                    binding.textViewRatingName.text = filmUi.ratingName
                    binding.textViewYearGenre.text = filmUi.yearGenre
                    binding.textViewCountryLength.text = filmUi.countryLength
                    binding.imageViewPoster.load(filmUi.posterUrl)
                    binding.textViewShortDescription.text = filmUi.shortDescription
                    if (!isCollapsedDescription)
                        binding.textViewDescription.text = filmUi.description
                    else binding.textViewDescription.text = filmUi.description250


                    //схлопывание описания фильма при нажатии на текст
                    binding.textViewDescription.setOnClickListener {
                        if (!isCollapsedDescription)
                            binding.textViewDescription.text = filmUi.description
                        else binding.textViewDescription.text = filmUi.description250
                        isCollapsedDescription = !isCollapsedDescription
                    }

                    binding.imageViewPoster.setOnClickListener {
                        openPicturePage(filmUi.posterUrl)
                    }
                }
            }
        }


        //актеры
        val actorCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(actorCustomRecyclerHorizontalView)
        actorCustomRecyclerHorizontalView.visibility = View.GONE

        //подписка на актеров из апи
        lifecycleScope.launch {
            viewModel.actorStateFlow.collect { itemList ->
                if (!itemList.isNullOrEmpty()) {
                    actorCustomRecyclerHorizontalView.visibility = View.VISIBLE
                    actorCustomRecyclerHorizontalView.setItem(
                        recyclerItemList = itemList,
                        name = ApiParameters.ACTOR.label,
                        { id, _ ->
                            //обращение к функции открытия фрагмента с person_page для снимались
                            //Log.d("Nik","id 1 = $id")
                            openPersonPage(id.toString())
                        },
                        {
                            //обращение к функции открытия фрагмента с list_page для снимались
                            openListPage(ApiParameters.ACTOR.label)
                        }
                    )
                }
                //Log.d("Nik", "$itemList")
            }
        }

        //персонал
        val staffCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(staffCustomRecyclerHorizontalView)
        staffCustomRecyclerHorizontalView.visibility = View.GONE

        //подписка на персонал из апи
        lifecycleScope.launch {
            viewModel.stuffStateFlow.collect { itemList ->
                if (!itemList.isNullOrEmpty()) {
                    staffCustomRecyclerHorizontalView.visibility = View.VISIBLE

                    staffCustomRecyclerHorizontalView.setItem(
                        recyclerItemList = itemList,
                        name = ApiParameters.STAFF.label,
                        { id, _ ->
                            //обращение к функции открытия фрагмента с person_page для снимались
                            //Log.d("Nik","id 1 = $id")
                            openPersonPage(id.toString())
                        },
                        {
                            //обращение к функции открытия фрагмента с list_page для персонал
                            openListPage(ApiParameters.STAFF.label)
                        }
                    )
                }
                //Log.d("Nik", "$itemList")
            }
        }

        //картинки
        val imagesCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(imagesCustomRecyclerHorizontalView)
        imagesCustomRecyclerHorizontalView.visibility = View.GONE

        //подписка на картинки из апи
        lifecycleScope.launch {
            viewModel.imagesStateFlow.collect { images ->
                if (images != null) {
                    if (images.itemList?.isNotEmpty() == true) {
                        imagesCustomRecyclerHorizontalView.visibility = View.VISIBLE
                        imagesCustomRecyclerHorizontalView.setItem(
                            recyclerItemList = images.itemList,
                            name = ApiParameters.IMAGES.label,
                            { id, posterUrl ->
                                //обращение к функции открытия фрагмента  отображения картинки
                                openPicturePage(posterUrl)
                            },
                            {
                                //обращение к функции открытия фрагмента с gallery_page для Галерея
                                openGalleryPage()
                            },
                            recycler = images)
                    }
                }
            }
        }

        //похожие фильмы
        val similarsCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(similarsCustomRecyclerHorizontalView)
        similarsCustomRecyclerHorizontalView.visibility = View.GONE

        //подписка на картинки из апи
        lifecycleScope.launch {
            viewModel.similarsStateFlow.collect { similarsList ->
                if (!similarsList.isNullOrEmpty()) {
                    similarsCustomRecyclerHorizontalView.visibility = View.VISIBLE
                    similarsCustomRecyclerHorizontalView.setItem(
                        recyclerItemList = similarsList,
                        name = ApiParameters.SIMILARS.label,
                        { id, _ ->
                            //обращение к функции открытия фрагмента с film_page для Похожие фильмы
                            openFilmPage(id)
                        },
                        {
                            //обращение к функции открытия фрагмента list_page для Похожие фильмы
                            openListPage(ApiParameters.SIMILARS.type)
                        })
                }
            }
        }


        //подписка на вывод ошибки
        lifecycleScope.launch {
            viewModel.errorStateFlow.collect { error ->
                if (error != null)
                    Snackbar.make(binding.imageViewPoster, "$error", Snackbar.LENGTH_LONG).show()
            }
        }

        //обновление данных свайпом вниз
        binding.swipeFilm.setOnRefreshListener {
            //Log.d("Nik","swipeFilm 1")
            viewModel.getFilm(idFilm) //данные фильма
            viewModel.getStaff(idFilm) //Персонал
            viewModel.getImages(idFilm) //картинки
            viewModel.getSimilars(idFilm) //похожие фильмы
            //firstLoad = false
            //Log.d("Nik","swipeFilm 2")
        }

    }

    //функция /открытия фрагмента с film_list
    private fun openFilmPage(id: Int) {

        val bundle = Bundle()
        //загрузка данных для фрагмента film_list
        bundle.putInt("id", id)
        bundle.putParcelable("country", null)
        bundle.putParcelable("genre", null)
        bundle.putParcelable("filters", null)
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_filmFragment)
        //передача данных в фрагмент film_list
        newInstance(bundle)
    }


    //функция /открытия фрагмента с list_page
    private fun openListPage(label: String) {
        val bundle = Bundle()
        //загрузка данных для фрагмента film_list
        bundle.putString("label", label)
        bundle.putString("idFilm", idFilm.toString())
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_filmListPageFragment)

        //передача данных в фрагмент film_list
        ListPageFragment.newInstance(bundle)
    }

    //функция /открытия фрагмента с ActorPage
    private fun openPersonPage(idActor: String) {
        val bundle = Bundle()

        // Log.d("Nik","${ApiParameters.PERSON.label} idActor = $idActor")
        bundle.putString(ApiParameters.PERSON.label, idActor)
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_personFragment)

        //передача данных в фрагмент film_list
        PersonFragment.newInstance(bundle)
    }

    //функция открытия фрагмента с PictureFragment
    private fun openPicturePage(posterUrl: String) {
        val bundle = Bundle()
        bundle.putString("posterUrl", posterUrl)
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_pictureFragment)

        //передача данных в фрагмент film_list
        PictureFragment.newInstance(bundle)
    }

    //функция открытия фрагмента с GalleryFragment
    private fun openGalleryPage() {
        val bundle = Bundle()
        //загрузка данных для фрагмента film_list
        bundle.putString("idFilm", idFilm.toString())

        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_galleryFragment)

        //передача данных в фрагмент film_list
        GalleryFragment.newInstance(bundle)
    }


    companion object {
        var idFilm by Delegates.notNull<Int>()

        @JvmStatic
        fun newInstance(args: Bundle) {
            idFilm = args.getInt("id")
        }
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
    }

}