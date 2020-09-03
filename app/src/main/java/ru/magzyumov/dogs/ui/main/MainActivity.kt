package ru.magzyumov.dogs.ui.main

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.ui.dialog.AlertDialogWindow
import javax.inject.Inject

class MainActivity: AppCompatActivity(), IFragmentWorker {
    private lateinit var alertDialog: AlertDialogWindow

    @Inject
    lateinit var mainViewModel: MainViewModel

    init {
        App.getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_list, R.id.navigation_favourites
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        observerLiveData()
        alertDialog = AlertDialogWindow(this, getString(R.string.title_understand_button))
    }

    override fun changePageTitle(title: String) {
        toolbar.title = title.capitalize()
    }

    override fun dataReady(dataReady: Boolean){
        when(dataReady){
            true -> {
                mainContentFrame.visibility = View.VISIBLE
                progressBarFrame.visibility = View.GONE
            }
            false -> {
                mainContentFrame.visibility = View.GONE
                progressBarFrame.visibility = View.VISIBLE
            }
        }
    }

    private fun observerLiveData() {
        mainViewModel.getNetworkStatus().observe(this, Observer{networkStatus ->
            networkStatus?.let {
                showMessage(getString(R.string.title_network_trouble), it)
            }
        })
    }

    private fun showMessage(title: String, message: String){
        alertDialog.show(title, message)
    }
}