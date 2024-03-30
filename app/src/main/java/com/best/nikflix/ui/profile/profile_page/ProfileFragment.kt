package com.best.nikflix.ui.profile.profile_page

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.best.nikflix.R
import com.best.entity.FilmAndCollectionF
import com.best.nikflix.App
import com.best.nikflix.ui.home.common.room_view_model.RoomViewModel
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.home.list_page.ListPageFragment
import com.best.nikflix.ui.profile.profile_page.compose.ProfileCompose
import kotlinx.coroutines.launch
import okhttp3.internal.filterList

class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.profileFragmentViewModelFactoryProvide()
    }

    private val viewModelRoom by viewModels<RoomViewModel> {
        (requireContext().applicationContext as App).appComponent.roomViewModelFactoryProvide()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        //подписка на с специальный список из БД
        //и обращение в апи с этим списком
        lifecycleScope.launch {
            viewModelRoom.specialListStateflow.collect { filmAndCollection ->
                //Log.d("Nik", "filmAndCollection $filmAndCollection")
                if (!filmAndCollection.isNullOrEmpty())
                    viewModel.getFilms(filmAndCollection)
            }
        }

        //запуск загрузки из БД
        lifecycleScope.launch {
            viewModelRoom.test().collect {
                viewModelRoom.getAllFilmAndCollectionF()
                //Log.d("Nik", "it $it")
            }
        }

        //список всех фильмов и коллекций из БД
        val filmAndCollectionListStateFlow = mutableListOf<FilmAndCollectionF>()
        lifecycleScope.launch {
            viewModelRoom.filmAndCollectionListStateFlow.collect {

                filmAndCollectionListStateFlow.clear()
               // Log.d("Nik", "filmAndCollectionListStateFlow  $filmAndCollectionListStateFlow")
               // Log.d("Nik", "it  $it")
                if (!it.isNullOrEmpty())
                    filmAndCollectionListStateFlow.addAll(it)
            }
        }

        val view = ComposeView(requireContext())
        view.setContent {
            //подписка на квалратные иконки из БД
            val filmAndCollectionMap = viewModelRoom.filmAndCollectionMapSateFlow.collectAsState()

            //Подписка на просмотрено из АПИ
            val alreadySawList = viewModel.alreadySawListStateFlow.collectAsState()

            //Подписка на Было интересно из АПИ
            val interestingList = viewModel.interestingListStateFlow.collectAsState()

            ProfileCompose(
                filmAndCollectionMap = filmAndCollectionMap.value,
                alreadySawList = alreadySawList.value,
                interestingList = interestingList.value,
                //удаление коллекции из БД
                { collection -> viewModelRoom.deleteCollections(collection) },
                //создание коллекции в БД с пустым фильмом
                { collectionName ->
                    viewModelRoom.insertFilmAndCollection(
                        FilmAndCollectionF(
                            filmId = "",
                            collection = collectionName
                        )
                    )
                },
                { idFilm ->
                    openFilmPage(idFilm)
                },
                { collection ->
                    //Log.d("Nik", "filmAndCollectionListStateFlow  $filmAndCollectionListStateFlow")
                    //отокрыть все фильмы этой коллекции
                    if (filmAndCollectionListStateFlow.isNotEmpty()) {

                        //получить спсиок id фильмов нужной коллекции
                        val idFilList = mutableListOf<String>()
                        filmAndCollectionListStateFlow.filterList {
                            this.collection == collection
                        }.forEach { idFilList.add(it.filmId) }

                        //открыть страницу со списком фильмов
                        if (idFilList.isNotEmpty())
                            openListPage(
                                label = collection,
                                idFilList = idFilList
                            )
                    }

                }
            )
        }
        return view
    }

    //функция /открытия фрагмента с film_page
    private fun openFilmPage(id: Int) {

        val bundle = Bundle()
        //загрузка данных для фрагмента film_page
        bundle.putInt("id", id)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_profile_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openListPage(
        label: String,
        idFilList: List<String>
        //filters: Filters? = null
    ) {
        activity?.findViewById<View>(R.id.textViewCinema)?.visibility = View.GONE

        val bundle = Bundle()
        //загрузка данных для фрагмента list_page
        bundle.putString("label", label)
        bundle.putParcelable("country", null)
        bundle.putParcelable("genre", null)
        bundle.putStringArrayList("idFilmArrayList", ArrayList(idFilList))
        bundle.putBoolean("isProfile", true)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_profile_to_filmListPageFragment
        )
        //передача данных в фрагмент list_page
        ListPageFragment.newInstance(bundle)

    }

    override fun onResume() {
        super.onResume()

        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.GONE

        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.VISIBLE
    }

}