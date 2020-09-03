package ru.magzyumov.dogs.util

import android.content.Context
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R

import javax.inject.Inject

class ResourceManager  {
    @Inject
    lateinit var context: Context

    init {
        App.getComponent().inject(this)
    }

    fun getAppNameString(): String{ return context.getString(R.string.app_name) }
    fun getPauseString(): String{ return context.getString(R.string.app_name) }
    fun getFinishString(): String{ return context.getString(R.string.app_name) }
}