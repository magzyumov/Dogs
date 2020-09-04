package ru.magzyumov.dogs.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.magzyumov.dogs.data.database.DogsDatabase
import ru.magzyumov.dogs.data.database.IDogsDao
import ru.magzyumov.dogs.repository.DogsRepository
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.IDogsRequest
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Provides
    fun providesRepository(dogsDao: IDogsDao, dogsRequest: IDogsRequest): DogsRepository {
        return DogsRepository(dogsDao, dogsRequest)
    }

    @Provides
    fun providesViewModel(dogsRepository: DogsRepository): MainViewModel {
        return MainViewModel(dogsRepository)
    }


    @Singleton
    @Provides
    fun providesDogsDao(db: DogsDatabase): IDogsDao {
        return db.iDogsDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(context: Context): DogsDatabase {
        return Room.databaseBuilder(context, DogsDatabase::class.java, "dogs_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): IDogsRequest {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(IDogsRequest::class.java)
    }

}