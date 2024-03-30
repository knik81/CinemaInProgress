package com.best.nikflix.ui.search.search_config_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.best.nikflix.R
import com.best.nikflix.App
import com.best.nikflix.ui.search.search_config_page.compose.ScreenSelectorCompose
import com.best.nikflix.ui.search.search_page.SearchFragmentViewModel

class SearchConfigFragment : Fragment() {

    private val viewModel by viewModels<SearchFragmentViewModel> {
        (requireContext().applicationContext as App).appComponent.searchFragmentViewModelFactoryProvide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())

       // val tt = viewModel.genresStateFlow.collectAsState()

        view.setContent {
            ScreenSelectorCompose(
                viewModel.filtersStateFlow.collectAsState().value
            ) { visibleArrowBack ->
                arrowBack(visibleArrowBack)
            }
            //SearchConfigCompose()
        }

        return view
    }

    private fun arrowBack(visibleArrowBack: Boolean) {
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        if (visibleArrowBack)
            arrowBack?.visibility = View.VISIBLE
        else
            arrowBack?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.VISIBLE
    }


}