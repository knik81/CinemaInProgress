package com.best.nikflix.ui.home.filmography_page

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.best.nikflix.R
import com.best.entity.PersonInterface
import com.best.entity.ProfessionKey
import com.best.nikflix.App
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.home.filmography_page.compose.FilmographyFragmentCompose
import com.best.nikflix.ui.home.filmography_page.entity.ChipItem
import com.best.nikflix.ui.home.list_page.ListPageFragment


class FilmographyFragment : Fragment() {

    private val viewModel by viewModels<FilmographyFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.filmographyFragmentViewModelFactoryProvide()
    }

    //подписка на фильмы
    private val films by lazy {
        viewModel.filmsStateFlow
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())

        view.setContent {
            val temp = films.collectAsState().value

            FilmographyFragmentCompose(
                person = person,
                films = temp,
                chipList = chipItem,
                { professionKeyValue ->
                    //обращение в апи за фильмами с учетом professionKey
                    person?.let {
                        Log.d("Nik", "it ${it.films}")
                        viewModel.getFilms(
                            itemApiUniversalList = it.films,
                            takeFilms = null,
                            professionKey = professionKeyValue
                        )
                    }
                    //Log.d("Nik", professionKeyValue)
                },
                { filmId ->
                    openFilmPage(filmId)
                }
            )
        }
        return view
    }

    //список профессий по проф.участию в фильме
    private val chipItem = mutableListOf<ChipItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d("Nik", person.toString())

        //заполнить список профессий по проф.участию в фильме
        val professionKeyListTemp = mutableListOf<ProfessionKey>()
        person?.films?.forEach { film ->
            val professionKey = film.professionKey
            if (professionKey != null)
                professionKeyListTemp.add(ProfessionKey.getProfessionKeyfromValue(professionKey))
        }
        //рассчет кол-ва фильмов для каждой роли
        val professionKeyTemp2 = professionKeyListTemp.distinct()
        chipItem.clear()
        professionKeyTemp2.forEach { professionKey ->
            chipItem.add(
                ChipItem(
                    type = professionKey.value,
                    name = professionKey.valueRu + " " + professionKeyListTemp.filter { it == professionKey }.size.toString()
                )
            )
        }
    }

    companion object {
        var person: PersonInterface? = null

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @JvmStatic
        fun newInstance(bundle: Bundle) =
            ListPageFragment().apply {
                arguments = Bundle().apply {
                    //person = bundle.getParcelable("person", PersonInterface::class.java)
                    person = bundle.getParcelable("person")
                }
            }
    }

    //функция /открытия фрагмента с film_page
    private fun openFilmPage(id: Int) {
        //Log.d("Nik", id.toString())
        val bundle = Bundle()
        //загрузка данных для фрагмента film_page
        bundle.putInt("id", id)

        //запуск фрагмента
        findNavController().navigate(
            R.id.action_filmographyFragment_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)

    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.GONE
    }

}