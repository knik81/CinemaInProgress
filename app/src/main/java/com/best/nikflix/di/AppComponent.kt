package com.best.nikflix.di


import com.best.data.di.DBModule
import com.best.data.di.DataModule1
import com.best.domain.di.DomainModule
import com.best.nikflix.ui.home.common.room_view_model.RoomViewModelFactory
import com.best.nikflix.ui.home.film_page.FilmFragmentViewModelFactory
import com.best.nikflix.ui.home.filmography_page.FilmographyFragmentViewModelFactory
import com.best.nikflix.ui.home.gallery_page.GalleryFragmentViewModelFactory
import com.best.nikflix.ui.home.home_page.HomeFragmentViewModelFactory
import com.best.nikflix.ui.home.list_page.ListPageFragmentViewModelFactory
import com.best.nikflix.ui.home.person_page.PersonFragmentViewModelFactory
import com.best.nikflix.ui.profile.profile_page.ProfileFragmentViewModelFactory
import com.best.nikflix.ui.search.search_page.SearchFragmentViewModelFactory

import dagger.Component


@Component(
    modules = [
        DataModule1::class,
        DomainModule::class,
        DBModule::class
    ]
)

interface AppComponent {

    fun allFilmsViewModelFactoryProvide(): HomeFragmentViewModelFactory
    fun listPageViewModelFactoryProvide(): ListPageFragmentViewModelFactory
    fun filmFragmentViewModelFactoryProvide(): FilmFragmentViewModelFactory
    fun personFragmentViewModelFactoryProvide(): PersonFragmentViewModelFactory
    fun galleryFragmentViewModelFactoryProvide(): GalleryFragmentViewModelFactory
    fun filmographyFragmentViewModelFactoryProvide(): FilmographyFragmentViewModelFactory
    fun searchFragmentViewModelFactoryProvide(): SearchFragmentViewModelFactory
    fun profileFragmentViewModelFactoryProvide(): ProfileFragmentViewModelFactory
    fun roomViewModelFactoryProvide(): RoomViewModelFactory

    //fun filmFragmentBottomSheetDialogViewModelFactoryProvide(): FilmFragmentBottomSheetDialogViewModelFactory
}