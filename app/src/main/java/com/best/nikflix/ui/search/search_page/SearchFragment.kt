package com.best.nikflix.ui.search.search_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.best.nikflix.App
import com.best.nikflix.R
import com.best.nikflix.ui.home.film_page.FilmFragment
import com.best.nikflix.ui.search.search_page.compose.SearchFragmentCompose


class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.searchFragmentViewModelFactoryProvide()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())

        //viewModel.getSearchText()
        //Log.d("Nik","setContent")
        viewModel.getAlreadySaw()
        
        view.setContent {
            //val searchText = viewModel.textListStateFlow.collectAsState().value
            val flowData by viewModel.getSearchTextFlow().collectAsState(initial = null)

            //viewModel.resultStateflowPager.
            //viewModel.alreadySaw(viewModel.resultStateflowPager.collectAsLazyPagingItems())
            SearchFragmentCompose(
                lazyPagingItems = viewModel.resultStateflowPager.collectAsLazyPagingItems(),
                searchTextList = flowData,
                alreadySaw = viewModel.alreadySawStateFlow.collectAsState().value,
                { queryParams ->
                    //запуск поиска
                    viewModel.getSearchFilms(queryParams)
                },
                { idFilm ->
                    //открыть экран с фильмом
                    openFilmPage(idFilm)
                },
                {
                    openSearchConfigPage()
                },
                {text ->

                    viewModel.insertSearchText(text)
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
            R.id.action_navigation_search_to_filmFragment
        )
        //передача данных в фрагмент film_page
        FilmFragment.newInstance(bundle)

    }

    //функция /открытия фрагмента с search_config_page
    private fun openSearchConfigPage() {
        //запуск фрагмента
        findNavController().navigate(
            R.id.action_navigation_search_to_searchConfigFragment
        )
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.GONE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.VISIBLE
    }
}