package com.example.testcinema.di


import com.example.data.di.DataModule
import com.example.domain.di.DomainModule
import com.example.testcinema.ui.home.film_page.FilmFragmentViewModelFactory
import com.example.testcinema.ui.home.gallery_page.GalleryFragmentViewModelFactory
import com.example.testcinema.ui.home.home_page.HomeFragmentViewModelFactory
import com.example.testcinema.ui.home.list_page.ListPageFragmentViewModelFactory
import com.example.testcinema.ui.home.person_page.PersonFragmentViewModelFactory

import dagger.Component


@Component(
    modules = [
        DataModule::class,
        DomainModule::class
    ]
)

interface AppComponent {
    fun allFilmsViewModelFactoryProvide(): HomeFragmentViewModelFactory
    fun listPageViewModelFactoryProvide(): ListPageFragmentViewModelFactory
    fun filmFragmentViewModelFactoryProvide(): FilmFragmentViewModelFactory
    fun personFragmentViewModelFactoryProvide(): PersonFragmentViewModelFactory
    fun galleryFragmentViewModelFactoryProvide(): GalleryFragmentViewModelFactory

}