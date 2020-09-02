package ru.magzyumov.dogs.di

import dagger.Component
import ru.magzyumov.dogs.ui.main.*
import ru.magzyumov.dogs.util.ResourceManager
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(context: ResourceManager)

    fun inject(viewModel: MainViewModel)

    fun inject(listFragment: ListFragment)
    fun inject(imagesFragment: ImagesFragment)
    fun inject(subBreedFragment: SubBreedFragment)
    fun inject(favouritesFragment: FavouritesFragment)
}