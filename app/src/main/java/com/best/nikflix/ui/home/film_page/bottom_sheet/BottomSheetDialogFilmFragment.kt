package com.best.nikflix.ui.home.film_page.bottom_sheet

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.best.nikflix.R
import com.best.nikflix.databinding.FragmentBottomSheetBinding
import com.best.entity.FilmAndCollectionF
import com.best.nikflix.App
import com.best.entity.CollectionParameters
import com.best.nikflix.entity.FilmUi
import com.best.nikflix.ui.home.common.room_view_model.RoomViewModel
import com.best.nikflix.ui.home.film_page.dialog_page.DialogFragment
import com.best.nikflix.ui.home.film_page.entity.RefreshBottomSheetDialogFilmFragmentInterface
import com.best.nikflix.ui.home.film_page.entity.RefreshFilmFragmentInterface
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


class BottomSheetDialogFilmFragment @Inject constructor(
) : BottomSheetDialogFragment(), RefreshBottomSheetDialogFilmFragmentInterface {


    private val viewModelRoom by viewModels<RoomViewModel> {
        (requireContext().applicationContext as App).appComponent.roomViewModelFactoryProvide()
    }

    lateinit var binding: FragmentBottomSheetBinding

    //загрузка инстанции FilmFragment
    //для запуска обновления кнопок в FilmFragment
    private var filmPageFragmentInterfaceReload: RefreshFilmFragmentInterface? = null
    fun setInterface(filmPageFragmentInterfaceReload: RefreshFilmFragmentInterface) {
        this.filmPageFragmentInterfaceReload = filmPageFragmentInterfaceReload
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelRoom.getButtonsInfo(idFilm)
        viewModelRoom.getCollections(idFilm.toString())

        //загрузка данных для экрана
        binding.imageViewPoster.load(filmUi?.posterUrl)
        binding.textViewName.text = filmUi?.filmName
        binding.textViewYearGenre.text = filmUi?.yearGenre

        //обработка чекбокса любимые
        binding.checkBoxLike.text = CollectionParameters.LIKE.label
        binding.checkBoxLike.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModelRoom.switch(
                    FilmAndCollectionF(
                        filmId = idFilm.toString(),
                        collection = CollectionParameters.LIKE.label
                    )
                )
                delay(100)
                // обновление кнопок в FilmFragment
                filmPageFragmentInterfaceReload?.reloadButtons()
            }
        }
        //подписка на БД для чекбокса любимые
        lifecycleScope.launch {
            viewModelRoom.existLikeStateFlow.collect {
                binding.checkBoxLike.isChecked = it
            }
        }

        //обработка чекбокса Просмотрено
        binding.checkBoxAlreadySaw.text = CollectionParameters.ALREADYSAW.label
        binding.checkBoxAlreadySaw.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModelRoom.switch(
                    FilmAndCollectionF(
                        filmId = idFilm.toString(),
                        collection = CollectionParameters.ALREADYSAW.label
                    )
                )
                delay(100)
                // обновление кнопок в FilmFragment
                filmPageFragmentInterfaceReload?.reloadButtons()
            }
        }
        //подписка на БД для чекбокса Просмотрено
        lifecycleScope.launch {
            viewModelRoom.existAlreadySawStateFlow.collect {
                binding.checkBoxAlreadySaw.isChecked = it
            }
        }


        //обработка чекбокса Хочу посмотреть
        binding.checkWantToSee.text = CollectionParameters.WANTTOSEE.label
        binding.checkWantToSee.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModelRoom.switch(
                    FilmAndCollectionF(
                        filmId = idFilm.toString(),
                        collection = CollectionParameters.WANTTOSEE.label
                    )
                )
                delay(100)
                // обновление кнопок в FilmFragment
                filmPageFragmentInterfaceReload?.reloadButtons()
            }
        }


        //подписка на БД для чекбокса Хочу посмотреть
        lifecycleScope.launch {
            viewModelRoom.existWantToSeeStateFlow.collect {
                binding.checkWantToSee.isChecked = it
            }
        }

        val lastCollectionsList = mutableListOf<String>()
        lifecycleScope.launch {
            viewModelRoom.collectionsListStateFlow.collect { collectionsList ->
                // Log.d("Nik", "collectionsList = ${collectionsList}")
                if (!collectionsList.isNullOrEmpty()) {
                    collectionsList.forEach { collection ->
                        //не добавлять на экран ранее добавленные коллекции
                        if (collection.collection !in lastCollectionsList) {

                            //создание чекбокса
                            val checkBox = CheckBox(requireContext())
                            checkBox.text = collection.collection

                            //взвести галку, если этот фильм в этой коллекции
                            if (collection.filmId != "")
                                checkBox.isChecked = true


                            checkBox.setOnClickListener {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModelRoom.switch(
                                        FilmAndCollectionF(
                                            filmId = idFilm.toString(),
                                            collection = collection.collection
                                        )
                                    )
                                }
                            }
                            //добавление чекбокса на экран
                            binding.LinearLayoutBottomSheet.addView(checkBox)

                            //покрасим чекбокс
                            val color = ContextCompat.getColor(requireContext(), R.color.purple_500)
                            checkBox.buttonDrawable?.colorFilter =
                                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)

                            //запомнить все чекбоксы-коллекции, доавленные на экран
                            lastCollectionsList.add(collection.collection)
                        }
                    }
                }
            }
        }


        //слушатель для открытия экрана с вводом названия новой коллекции
        binding.textViewCreateNewCollection.setOnClickListener {
            //открыть диалог-фрагмент
            val newCollectionDialogFragment = DialogFragment()
            newCollectionDialogFragment.show(childFragmentManager, "MyDialogFragment")
            newCollectionDialogFragment.setInterface(this, idFilm)
        }
    }

    companion object {
        private var idFilm by Delegates.notNull<Int>()
        private var filmUi: FilmUi? = null

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @JvmStatic
        fun newInstance(args: Bundle) {
            idFilm = args.getInt("id")
            filmUi = args.getParcelable("FilmUi")
            //ошибка на старых версиях
            //проверку версии делать пока не хочу
            //filmUi = args.getParcelable("FilmUi", FilmUi::class.java)
        }
    }

    //обновить чекбоксы после добавления новой коллекции
    override fun reloadCheckBox() {
        viewModelRoom.getCollections(idFilm.toString())
    }
}