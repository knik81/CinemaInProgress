package com.best.nikflix.ui.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.best.nikflix.R
import com.best.nikflix.databinding.FragmentOnboardingBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val FINISH_SLIDE = 3

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding


    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)

        //инициализация viewPager
        viewPager = binding.viewPager
        //инициализация tabLayout - инидкация листания в виде точек
        tabLayout = binding.tabLayout
        //загрузка пейджера в разметку
        adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //созданье шарика
        val shpref = requireActivity().getSharedPreferences(
            "SharedName",
            AppCompatActivity.MODE_PRIVATE
        )

        //чтение шарика и пропуск пейджера
        //val str = shpref.getString("skip", "-")
        if (shpref.getString("skip", "-") == "Скрыть")
            start()

        //индикатор снизу
        TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
            //tab.text = "Tab ${position + 1}"
            tab.setIcon(R.drawable.dot)
        }.attach()


        //колбэк для смены текста и перехода
        val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 0) binding.textViewSlide.text = "Узнавай о премьерах"
                if (position == 1) binding.textViewSlide.text = "Создавай коллекции"
                if (position == 2) binding.textViewSlide.text = "Делись с друзьями"
                if (position == 3) binding.textViewSlide.text = "старт"

                if (position == FINISH_SLIDE)
                    start()
            }
        }

        // Добавляем колбэк к ViewPager2
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        binding.textViewNext.setOnClickListener {
            //запись в шарик асинхронно
            val editor: SharedPreferences.Editor = shpref.edit()
            editor.putString("skip", "Скрыть")
            editor.apply()
            start()
        }
    }

    //
    private fun start() {
        findNavController().navigate(
            R.id.action_onboardingFragment_to_navigation_home
        )
    }

    override fun onResume() {
        super.onResume()
        val arrowBack = activity?.findViewById<View>(R.id.arrow_back)
        arrowBack?.visibility = View.GONE
        val label = activity?.findViewById<View>(R.id.textViewCinema)
        label?.visibility = View.VISIBLE
        val bottomNavView = activity?.findViewById<View>(R.id.nav_view)
        bottomNavView?.visibility = View.GONE
    }


}

