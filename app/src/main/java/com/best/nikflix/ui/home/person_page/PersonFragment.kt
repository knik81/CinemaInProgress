package com.best.nikflix.ui.home.person_page

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.best.nikflix.R
import com.best.entity.ApiParameters
import com.best.nikflix.App
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.home.filmography_page.FilmographyFragment
import com.best.nikflix.ui.home.filmography_page.FilmographyFragmentViewModel
import com.best.nikflix.ui.home.person_page.compose.ActorFragmentCompose
import com.best.nikflix.ui.home.picture_page.PictureFragment
import kotlinx.coroutines.launch


class PersonFragment : Fragment() {

    private val viewModel: PersonFragmentViewModel by viewModels<PersonFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.personFragmentViewModelFactoryProvide()
    }
    private val filmographyFragmentViewModel by viewModels<FilmographyFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.filmographyFragmentViewModelFactoryProvide()
    }


    //подписка на данные персоны
    private val person by lazy {
        viewModel.personStateFlow
    }

    //подписка на 10 фильмов персоны
    private val bestTenFilms by lazy {
        filmographyFragmentViewModel.filmsStateFlow
    }

    //подписка итоговую сумму фильмов
    private val totalFilms by lazy {
        viewModel.totalFilmsStateFlow
    }

    // var professionKeyList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    person.collect {
                        it?.films?.let { it1 ->
                            filmographyFragmentViewModel.getFilms(
                                itemApiUniversalList = it1,
                                takeFilms = 10,
                                professionKey = null
                            )
                        }
                    }
                }
            }

        }
        val view = ComposeView(requireContext())
        view.setContent {


            ActorFragmentCompose(
                person.collectAsState().value,
                bestTenFilms.collectAsState().value,
                totalFilms.collectAsState().value,
                {
                    //открыть list_page со всеми фильмами
                    openFilmographyPage()
                },
                { idFilm ->
                    //открыть film_page
                    openFilmPage(idFilm)
                }
            ) { posterUrl ->
                //открыть изображение в picture_page
                openPicturePage(posterUrl)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //обращение к апи за данными по персоне
        viewModel.getPerson(idPerson ?: "", 10)
        //Log.d("Nik", idPerson.toString())

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.errorStateFlow.collect {
                        // if (error != null)
                        //Snackbar.make(R.id., "$error", Snackbar.LENGTH_LONG).show()
                    }
                }
                launch {
                    filmographyFragmentViewModel.errorStateFlow.collect {
                        // if (error != null)
                        //Snackbar.make(R.id., "$error", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    companion object {
        private var idPerson: String? = null


        @JvmStatic
        fun newInstance(bundle: Bundle) =
            PersonFragment().apply {
                arguments = Bundle().apply {
                    idPerson = bundle.getString(ApiParameters.PERSON.label)
                }
            }
    }

    private fun openFilmPage(idFilm: Int) {
        val bundle = Bundle()
        //загрузка данных для фрагмента film_page
        bundle.putInt("id", idFilm)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_personFragment_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)
    }

    //функция /открытия фрагмента с filmography_page
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openFilmographyPage() {
        val bundle = Bundle()
        //загрузка данных для фрагмента filmography_page
        bundle.putParcelable("person", person.value as Parcelable)


        //запуск фрагмента
        findNavController().navigate(
            R.id.action_personFragment_to_filmographyFragment
        )
        //передача данных в фрагмент filmography_page
        FilmographyFragment.newInstance(bundle)
    }

    //функция открытия фрагмента с PictureFragment
    private fun openPicturePage(posterUrl: String) {
        val bundle = Bundle()
        bundle.putString("posterUrl", posterUrl)
        //запуск фрагмента
        findNavController().navigate(R.id.action_personFragment_to_pictureFragment)

        //передача данных в фрагмент picture_page
        PictureFragment.newInstance(bundle)
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.GONE
    }
}



