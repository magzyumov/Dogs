package ru.magzyumov.dogs

import android.app.Application
import ru.magzyumov.dogs.di.AppComponent
import ru.magzyumov.dogs.di.AppModule
import ru.magzyumov.dogs.di.DaggerAppComponent

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        component = generateAppComponent()
    }

    private fun generateAppComponent(): AppComponent{
        return DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object{
        private lateinit var component: AppComponent

        fun getComponent(): AppComponent = component
    }
}