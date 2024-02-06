package com.example.testcinema.ui.home.picture_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.example.testcinema.R
import com.example.testcinema.databinding.FragmentPictureBinding

class PictureFragment: Fragment() {

    private lateinit var binding: FragmentPictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPictureBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewPicture.load(posterUrl)
    }


    companion object {
        private var posterUrl: String? = null
        @JvmStatic
        fun newInstance(bundle: Bundle){
            posterUrl = bundle.getString("posterUrl")
        }
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.VISIBLE
    }


}