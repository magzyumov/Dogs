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
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject


class ListFragment: Fragment(), BreedAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var breedAdapter: BreedAdapter
    private lateinit var allBreeds: List<Breed>
    private lateinit var fragmentWorker: IFragmentWorker

    init {
        App.getComponent().inject(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFragmentWorker) fragmentWorker = context
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        allBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentWorker.changePageTitle(getString(R.string.app_name))
        fragmentWorker.dataReady(false)

        initRecyclerView()
        observerLiveData()
    }


    private fun observerLiveData() {
        mainViewModel.getAllBreeds().observe(viewLifecycleOwner, Observer { listOfBreeds ->
            listOfBreeds?.let {
                allBreeds = listOfBreeds
                breedAdapter.swap(it)
                fragmentWorker.dataReady(true)
            }
        })
        mainViewModel.getNetworkStatus().observe(viewLifecycleOwner, Observer{networkStatus ->
            networkStatus?.let {
                fragmentWorker.showMessage(getString(R.string.title_network_trouble), it)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewBreeds.apply {
            breedAdapter = BreedAdapter(allBreeds, this@ListFragment)
            layoutManager = LinearLayoutManager(this@ListFragment.context)
            adapter = breedAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Breed) {
        if (item.subBreeds.isEmpty()){
            val navDirection = ListFragmentDirections.actionNavigationListToNavigationImages(
                item.breed.toLowerCase(),
                NULL_SUB_BREED
            )
            findNavController().navigate(navDirection)
        } else {
            val navDirection =
                ListFragmentDirections.actionNavigationListToNavigationSubBreed(item.breed.toLowerCase())
            findNavController().navigate(navDirection)
        }
    }
}