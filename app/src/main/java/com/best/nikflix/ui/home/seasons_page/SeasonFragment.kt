package com.best.nikflix.ui.home.seasons_page

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.best.nikflix.R
import com.best.entity.EpisodeInterface
import com.best.entity.SeasonsInterface
import com.best.nikflix.ui.home.seasons_page.compose.SeasonFragmentCompose
import kotlinx.coroutines.flow.MutableStateFlow


class SeasonFragment : Fragment() {

    private val episodesList = MutableStateFlow<List<EpisodeInterface>?>(null)


    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = ComposeView(requireContext())
        view.setContent {
            SeasonFragmentCompose(
                episodeList = episodesList.collectAsState().value,
                totalSeasons = seasons?.total ?: 0,
                selectSeason = selectSeason.collectAsState().value,
                filmName = filmName
            ) {
                //Log.d("Nik", "selectSeason  $selectSeason")
                selectSeason.value = it
                episodesList.value = getEpisodes(it)
            }
        }
        return view
    }

    private fun getEpisodes(season: Int) = seasons?.items?.get(season)?.episodes


    companion object {
        private var seasons: SeasonsInterface? = null
        //private var selectSeason: Int = 0
        private var filmName = ""
        private val selectSeason = MutableStateFlow(0)

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @JvmStatic
        fun newInstance(bundle: Bundle) {
            seasons =
                bundle.getParcelable("SeasonsInterface", SeasonsInterface::class.java)
            selectSeason.value = bundle.getInt("selectSeason")
            filmName = bundle.getString("filmName") ?: ""

        }
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.GONE
    }
}
