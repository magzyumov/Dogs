package ru.magzyumov.dogs.ui.fragments

import kotlinx.android.synthetic.main.fragment_favourites.*
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.magzyumov.dogs.App

import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*
import ru.magzyumov.dogs.ui.adapter.FavouritesAdapter
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject

class FavouritesFragment: Fragment(), FavouritesAdapter.Interaction {

    @Inject
    lateinit var mMainViewModel: MainViewModel

    private lateinit var mFavouritesAdapter: FavouritesAdapter
    private lateinit var mAllFavourites: List<FavouritesCount>
    private lateinit var mFragmentWorker: IFragmentWorker

    init {
        App.getComponent().inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentWorker) mFragmentWorker = context
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mAllFavourites = arrayListOf()
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentWorker.changePageTitle(getString(R.string.title_favourites))
        mFragmentWorker.dataReady(false)

        initRecyclerView()
        observerLiveData()
    }

    private fun observerLiveData() {
        mMainViewModel.getAllFavourite().observe(viewLifecycleOwner, Observer { listOfFavourites ->
            listOfFavourites?.let {
                mAllFavourites = listOfFavourites
                mFavouritesAdapter.swap(it)
                mFragmentWorker.dataReady(true)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewFavourites.apply {
            mFavouritesAdapter = FavouritesAdapter(mAllFavourites, this@FavouritesFragment)
            layoutManager = LinearLayoutManager(this@FavouritesFragment.context)
            adapter = mFavouritesAdapter
        }
    }

    override fun onItemSelected(position: Int, item: FavouritesCount) {
        val navDirection =
            FavouritesFragmentDirections.actionNavigationFavouritesToNavigationImages(item.breed, NULL_SUB_BREED, true)
        findNavController().navigate(navDirection)
    }
}