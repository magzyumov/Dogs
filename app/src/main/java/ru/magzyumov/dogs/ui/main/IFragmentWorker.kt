package ru.magzyumov.dogs.ui.main

interface IFragmentWorker {
    fun changePageTitle(title: String)
    fun dataReady(dataReady: Boolean)
    fun showMessage(title: String, message: String)
}