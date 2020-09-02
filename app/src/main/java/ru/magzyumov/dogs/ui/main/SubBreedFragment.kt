package ru.magzyumov.dogs.ui.main


import kotlinx.android.synthetic.main.fragment_list.*
import ru.magzyumov.dogs.ui.adapter.BreedAdapter

import android.os.Bundle

import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.ui.adapter.SubBreedAdapter
import javax.inject.Inject


class SubBreedFragment: Fragment(), SubBreedAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var breedAdapter: BreedAdapter
    private lateinit var allBreeds: List<Breed>

    init {
        App.getComponent().inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        allBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_sub_breeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar.title = "Breeds"

        initRecyclerView()
        observerLiveData()
    }


    private fun observerLiveData() {
        mainViewModel.getAllBreeds().observe(viewLifecycleOwner, Observer { listOfBreeds ->
            listOfBreeds?.let {
                allBreeds = listOfBreeds
                breedAdapter.swap(it)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewBreeds.apply {
            breedAdapter = BreedAdapter(allBreeds, this@SubBreedFragment)
            layoutManager = LinearLayoutManager(this@SubBreedFragment.context)
            adapter = breedAdapter
        }
    }

    override fun onItemSelected(position: Int, item: Breed) {

        val navDirection = ListFragmentDirections
            .actionNavigationListToNavigationImages(item.breed.toLowerCase())

        findNavController().navigate(navDirection)
    }
}