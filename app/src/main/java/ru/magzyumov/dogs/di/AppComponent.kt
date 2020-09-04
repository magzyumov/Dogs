package ru.magzyumov.dogs.di

import dagger.Component
import ru.magzyumov.dogs.ui.fragments.FavouritesFragment
import ru.magzyumov.dogs.ui.fragments.ImagesFragment
import ru.magzyumov.dogs.ui.fragments.ListFragment
import ru.magzyumov.dogs.ui.fragments.SubBreedFragment
import ru.magzyumov.dogs.ui.main.*
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(listFragment: ListFragment)
    fun inject(imagesFragment: ImagesFragment)
    fun inject(subBreedFragment: SubBreedFragment)
    fun inject(favouritesFragment: FavouritesFragment)
}