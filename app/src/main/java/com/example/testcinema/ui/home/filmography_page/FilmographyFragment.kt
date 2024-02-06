package com.example.testcinema.ui.home.filmography_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import com.example.testcinema.ui.home.filmography_page.compose.FilmographyFragmentCompose
import com.example.testcinema.ui.home.list_page.ListPageFragment


class FilmographyFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = ComposeView(requireContext())

        view.setContent {
            FilmographyFragmentCompose("label","name")
        }
        return view
    }


    companion object {
        private var idPerson: String? = null

        @JvmStatic
        fun newInstance(args: Bundle) =
            ListPageFragment().apply {
                arguments = Bundle().apply {
                    idPerson = args.getString("idPerson")
                }
            }
    }

}