package com.best.nikflix.ui.home.film_page.dialog_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.best.entity.FilmAndCollectionF
import com.best.nikflix.App
import com.best.nikflix.ui.home.common.room_view_model.RoomViewModel
import com.best.nikflix.ui.home.film_page.bottom_sheet.BottomSheetDialogFilmFragment
import com.best.nikflix.ui.home.film_page.entity.RefreshBottomSheetDialogFilmFragmentInterface
import com.best.nikflix.ui.profile.profile_page.compose.DialogCompose
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject
import kotlin.properties.Delegates

class DialogFragment @Inject constructor() : BottomSheetDialogFragment() {


    private val viewModelRoom by viewModels<RoomViewModel> {
        (requireContext().applicationContext as App).appComponent.roomViewModelFactoryProvide()
    }

    private lateinit var bottomSheetDialogFilmFragment: RefreshBottomSheetDialogFilmFragmentInterface

    var idFilm by Delegates.notNull<Int>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())

        view.setContent {
            //для открытия диалога
            val openAlertDialog = remember { mutableStateOf(false) }

            DialogCompose(
                isOpenAlertDialog = true
            ) { collectionName ->
                openAlertDialog.value = false
                if (collectionName != "") {
                    //добавляем коллекцию с фильмом
                    viewModelRoom.insertFilmAndCollection(
                        FilmAndCollectionF(
                            filmId = idFilm.toString(),
                            collection = collectionName
                        )
                    )
                    //добавляем коллекцию без фильма
                    viewModelRoom.insertFilmAndCollection(
                        FilmAndCollectionF(
                            filmId = "",
                            collection = collectionName
                        )
                    )
                }

                openAlertDialog.value = false

                this.dismiss()
                bottomSheetDialogFilmFragment.reloadCheckBox()
            }
        }
        return view
    }


    fun setInterface(
        bottomSheetDialogFilmFragment: BottomSheetDialogFilmFragment,
        idFilm: Int
    ) {
        this.idFilm = idFilm
        this.bottomSheetDialogFilmFragment = bottomSheetDialogFilmFragment
    }


}