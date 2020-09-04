package ru.magzyumov.dogs.ui.fragments


import android.content.Context
import kotlinx.android.synthetic.main.fragment_list.*
import ru.magzyumov.dogs.ui.adapter.BreedAdapter

import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.data.response.BreedsResponse.*
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject


class ListFragment: Fragment(), BreedAdapter.Interaction {

    @Inject
    lateinit var mMainViewModel: MainViewModel

    private lateinit var mBreedAdapter: BreedAdapter
    private lateinit var mAllBreeds: List<Breed>
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
        mAllBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentWorker.changePageTitle(getString(R.string.app_name))
        mFragmentWorker.dataReady(false)

        initRecyclerView()
        observerLiveData()
    }


    private fun observerLiveData() {
        mMainViewModel.getAllBreeds().observe(viewLifecycleOwner, Observer { listOfBreeds ->
            listOfBreeds?.let {
                mAllBreeds = listOfBreeds
                mBreedAdapter.swap(it)
                mFragmentWorker.dataReady(true)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewBreeds.apply {
            mBreedAdapter = BreedAdapter(mAllBreeds, this@ListFragment)
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = mBreedAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Breed) {
        if (item.subBreeds.isEmpty()){
            val navDirection = ListFragmentDirections.actionNavigationListToNavigationImages(
                item.breed,
                NULL_SUB_BREED
            )
            findNavController().navigate(navDirection)
        } else {
            val navDirection =
                ListFragmentDirections.actionNavigationListToNavigationSubBreed(item.breed)
            findNavController().navigate(navDirection)
        }
    }
}