package com.example.testcinema.ui.home.person_page

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.entity.ApiParameters
import com.example.testcinema.App
import com.example.testcinema.R
import com.example.testcinema.ui.home.film_page.FilmFragment
import com.example.testcinema.ui.home.filmography_page.FilmographyFragment
import com.example.testcinema.ui.home.list_page.ListPageFragment
import com.example.testcinema.ui.home.person_page.compose.ActorFragmentCompose
import com.example.testcinema.ui.home.picture_page.PictureFragment
import kotlinx.coroutines.launch


class PersonFragment : Fragment() {

    val viewModel: PersonFragmentViewModel by viewModels<PersonFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.personFragmentViewModelFactoryProvide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    private val person by lazy {
        viewModel.personStateFlow
    }

    private val bestTenFilms by lazy {
        viewModel.filmsStateFlow
    }

    private val totalFilms by lazy {
        viewModel.totalFilmsStateFlow
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
        Log.d("Nik", idPerson.toString())

        lifecycleScope.launch {
            viewModel.errorStateFlow.collect { error ->
                // if (error != null)
                //Snackbar.make(R.id., "$error", Snackbar.LENGTH_LONG).show()
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

    //функция /открытия фрагмента с list_page
    private fun openFilmographyPage() {
        val bundle = Bundle()
        //загрузка данных для фрагмента list_page
        bundle.putString("idPerson", idPerson)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_personFragment_to_filmographyFragment
        )
        //передача данных в фрагмент list_page
        FilmographyFragment.newInstance(bundle)
    }

    //функция открытия фрагмента с PictureFragment
    private fun openPicturePage(posterUrl: String) {
        val bundle = Bundle()
        bundle.putString("posterUrl", posterUrl)
        //запуск фрагмента
        findNavController().navigate(R.id.action_personFragment_to_pictureFragment)

        //передача данных в фрагмент film_list
        PictureFragment.newInstance(bundle)
    }
}



