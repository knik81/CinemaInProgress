package com.best.nikflix.ui.home.film_page

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import com.best.nikflix.R
import com.best.nikflix.databinding.FragmentFilmPageBinding
import com.best.entity.ApiParameters
import com.best.entity.FilmAndCollectionF
import com.best.entity.SeasonsInterface
import com.best.nikflix.App
import com.best.entity.CollectionParameters
import com.best.nikflix.entity.FilmUi
import com.best.nikflix.ui.home.common.room_view_model.RoomViewModel
import com.best.nikflix.ui.home.common.xml.recycler_horizontal.CustomHorizontalRecyclerView
import com.best.nikflix.ui.home.film_page.bottom_sheet.BottomSheetDialogFilmFragment
import com.best.nikflix.ui.home.film_page.entity.RefreshFilmFragmentInterface
import com.best.nikflix.ui.home.gallery_page.GalleryFragment
import com.best.nikflix.ui.home.list_page.ListPageFragment
import com.best.nikflix.ui.home.person_page.PersonFragment
import com.best.nikflix.ui.home.picture_page.PictureFragment
import com.best.nikflix.ui.home.seasons_page.SeasonFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FilmFragment : Fragment(), RefreshFilmFragmentInterface {


    private val viewModel: FilmFragmentViewModel by viewModels<FilmFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.filmFragmentViewModelFactoryProvide()
    }

    private val viewModelRoom by viewModels<RoomViewModel> {
        (requireContext().applicationContext as App).appComponent.roomViewModelFactoryProvide()
    }

    private var firstLoad = true

    private lateinit var binding: FragmentFilmPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilmPageBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var serialName = ""
        var isCollapsedDescription = true

        //обращение к апи

        if (firstLoad) {
            viewModel.getFilm(idFilm) //данные фильма
            viewModel.getStaff(idFilm) //Персонал
            viewModel.getImages(idFilm) //картинки
            viewModel.getSimilars(idFilm) //похожие фильмы
            firstLoad = false
            viewModelRoom.getButtonsInfo(idFilm)//информация по кнопкам
            viewModelRoom.saveInteresting(idFilm)//сохранить в БД в коллекцию интересно
        }


        //Log.d("Nik", idFilm.toString())
        var webUrl = ""
        var film: FilmUi? = null

        //актеры
        val actorCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(actorCustomRecyclerHorizontalView)
        actorCustomRecyclerHorizontalView.visibility = View.GONE

        //персонал
        val staffCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(staffCustomRecyclerHorizontalView)
        staffCustomRecyclerHorizontalView.visibility = View.GONE


        //картинки
        val imagesCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(imagesCustomRecyclerHorizontalView)
        imagesCustomRecyclerHorizontalView.visibility = View.GONE

        //похожие фильмы
        val similarsCustomRecyclerHorizontalView = CustomHorizontalRecyclerView(requireContext())
        binding.linearLayoutRecycler.addView(similarsCustomRecyclerHorizontalView)
        similarsCustomRecyclerHorizontalView.visibility = View.GONE

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                //подписка на данные фильма из апи
                launch {
                    viewModel.filmStateFlow.collect { filmUi ->
                        if (filmUi != null) {
                            film = filmUi
                            //Log.d("Nik", "FilmFragment, idFilm = $idFilm")
                            binding.textViewRatingName.text = filmUi.ratingName
                            binding.textViewYearGenre.text = filmUi.yearGenre
                            binding.textViewCountryLength.text = filmUi.countryLength
                            binding.imageViewPoster.load(filmUi.posterUrl)
                            binding.textViewShortDescription.text = filmUi.shortDescription
                            if (!isCollapsedDescription)
                                binding.textViewDescription.text = filmUi.description
                            else binding.textViewDescription.text = filmUi.description250
                            webUrl = filmUi.webUrl

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

                            //Обращение в АПИ за сезонами для сериала
                            if (filmUi.type == ApiParameters.SERIES.type) {
                                serialName = filmUi.filmName
                                viewModel.getSeasons(idFilm)
                            }
                        }
                    }
                }
                //подписка на сезоны для сериала
                launch {
                    viewModel.seasonTotalStateFlow.collect { season ->
                        if (season?.total != null) {
                            binding.linearLayoutSeries.visibility = View.VISIBLE
                            repeat(season.total!!) { selectSeason ->
                                val textViewSeason = TextView(context)
                                textViewSeason.textSize = 15f
                                textViewSeason.setPadding(0, 0, 110, 0)
                                textViewSeason.text = "Сезон ${selectSeason + 1}"
                                textViewSeason.setOnClickListener {
                                    //Log.d("Nik", "${textViewSeason.text}")
                                    openSeasonPage(season, selectSeason, serialName)
                                }
                                binding.LinearLayoutSeries.addView(textViewSeason)
                            }
                        }
                    }
                }

                //подписка на актеров из апи
                launch {
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

                //подписка на персонал из апи
                launch {
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

                //подписка на картинки из апи
                launch {
                    viewModel.imagesStateFlow.collect { images ->
                        if (images != null) {
                            if (images.itemList?.isNotEmpty() == true) {
                                imagesCustomRecyclerHorizontalView.visibility = View.VISIBLE
                                imagesCustomRecyclerHorizontalView.setItem(
                                    recyclerItemList = images.itemList,
                                    name = ApiParameters.IMAGES.label,
                                    { _, posterUrl ->
                                        //Log.d("Nik", "openPicturePage")
                                        //обращение к функции открытия фрагмента  отображения картинки
                                        openPicturePage(posterUrl)

                                    },
                                    {
                                        //Log.d("Nik", "openGalleryPage")
                                        //обращение к функции открытия фрагмента с gallery_page для Галерея
                                        openGalleryPage()

                                    },
                                    recycler = images
                                )
                            }
                        }
                    }
                }

                //подписка на картинки из апи
                launch {
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
                launch {
                    viewModel.errorStateFlow.collect { error ->
                       /*
                        if (error != null)
                            Snackbar.make(binding.imageViewPoster, "$error", Snackbar.LENGTH_LONG)
                                .show()

                        */
                    }
                }

                //подписка на иконка сердечко
                launch {
                    viewModelRoom.existLikeStateFlow.collect {
                        if (it)
                            binding.like.setImageResource(R.drawable.like_active)
                        else
                            binding.like.setImageResource(R.drawable.like)
                    }
                }

                //подписка на хочу посмотреть
                launch {
                    viewModelRoom.existWantToSeeStateFlow.collect {
                        if (it)
                            binding.wanttosee.setImageResource(R.drawable.want_see_active)
                        else
                            binding.wanttosee.setImageResource(R.drawable.favorite)
                    }
                }

                //подписка на видел
                launch {
                    viewModelRoom.existAlreadySawStateFlow.collect {
                        if (it)
                            binding.alreadysaw.setImageResource(R.drawable.see_active)
                        else
                            binding.alreadysaw.setImageResource(R.drawable.see)
                    }
                }


                launch {

                }
            }
        }

        binding.imageView2.setOnClickListener {
            //заглушка, чтобы не открывался постер
        }

        /*
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

          */


        //нажатие на сердечко
        binding.like.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch(Dispatchers.IO) {
                        viewModelRoom.switch(
                            FilmAndCollectionF(
                                filmId = idFilm.toString(),
                                collection = CollectionParameters.LIKE.label
                            )
                        )
                    }
                }
            }
        }

        //нажатие на хочу посмотреть
        binding.wanttosee.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch(Dispatchers.IO) {
                        launch(Dispatchers.IO) {
                            viewModelRoom.switch(
                                FilmAndCollectionF(
                                    filmId = idFilm.toString(),
                                    collection = CollectionParameters.WANTTOSEE.label
                                )
                            )
                        }
                    }
                }
            }
        }

        //нажатие на уже видел
        binding.alreadysaw.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch(Dispatchers.IO) {
                        viewModelRoom.switch(
                            FilmAndCollectionF(
                                filmId = idFilm.toString(),
                                collection = CollectionParameters.ALREADYSAW.label
                            )
                        )
                    }
                }
            }
        }

        //нажатие на поделится
        binding.share.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                webUrl
                // "https://www.imdb.com/title/${idFilm}/"
            )
            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        //нажатие на 3точки
        binding.dots3.setOnClickListener {

            val filmPageBottomSheetDialogFragment = BottomSheetDialogFilmFragment()
            filmPageBottomSheetDialogFragment.show(
                requireActivity().supportFragmentManager, "sdf"
            )


            //передача инстанции
            //нужно для обновления кнопок.
            //при изменениях из BottomSheet
            filmPageBottomSheetDialogFragment.setInterface(this)


            val bundle = Bundle()
            bundle.putInt("id", idFilm)
            bundle.putParcelable("FilmUi", film)
            //передача данных в BottomSheet
            BottomSheetDialogFilmFragment.newInstance(bundle)
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
        //Log.d("Nik", "openGalleryPage ")
        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_galleryFragment)

        //передача данных в фрагмент film_list
        GalleryFragment.newInstance(bundle)
    }

    //функция открытия фрагмента с SeasonFragment
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openSeasonPage(seasons: SeasonsInterface, selectSeason: Int, serialName: String) {
        val bundle = Bundle()
        //Log.d("Nik", "00 ${seasons}")
        //загрузка данных для фрагмента film_list
        bundle.putParcelable("SeasonsInterface", seasons as Parcelable)
        bundle.putInt("selectSeason", selectSeason)
        bundle.putString("filmName", serialName)

        //запуск фрагмента
        findNavController().navigate(R.id.action_filmFragment_to_seasonFragment)

        //передача данных в фрагмент film_list
        SeasonFragment.newInstance(bundle)
    }


    companion object {
        private var idFilm by Delegates.notNull<Int>()

        @JvmStatic
        fun newInstance(args: Bundle) {
            idFilm = args.getInt("id")
        }
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.GONE
    }

    //загрузка данных по кнопкам для их обновления
    //при изменении из BottomSheet
    override fun reloadButtons() {
        viewModelRoom.getButtonsInfo(idFilm)
    }

}