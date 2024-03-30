package com.best.nikflix.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.best.nikflix.R


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val images = listOf(
        R.drawable.logo1,
        R.drawable.wellcome_2,
        R.drawable.wellcome_3,
        R.drawable.logo1
    )

    override fun getItemCount(): Int = images.size

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(images[position])
    }
}

class ImageFragment : Fragment() {

    companion object {
        private const val ARG_IMAGE_RES = "image_res"

        fun newInstance(imageRes: Int): ImageFragment {
            val args = Bundle()
            args.putInt(ARG_IMAGE_RES, imageRes)
            val fragment = ImageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val imageView = ImageView(context)
        imageView.setImageResource(requireArguments().getInt(ARG_IMAGE_RES))
        return imageView
    }
}