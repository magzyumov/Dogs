package ru.magzyumov.dogs.ui.main


import ru.magzyumov.dogs.ui.adapter.BreedAdapter
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sub_breeds.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.ui.adapter.SubBreedAdapter
import javax.inject.Inject


class SubBreedFragment: Fragment(), SubBreedAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var subBreedAdapter: SubBreedAdapter
    private lateinit var allSubBreeds: List<String>
    private lateinit var safeArgs: SubBreedFragmentArgs

    init {
        App.getComponent().inject(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        allSubBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_sub_breeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareArguments()
        initRecyclerView()
        observerLiveData()
    }

    private fun prepareArguments() {
        arguments?.let {
            safeArgs = SubBreedFragmentArgs.fromBundle( it )
        }
        requireActivity().toolbar.title = safeArgs.breedName.capitalize()
    }

    private fun observerLiveData() {
        mainViewModel.getSubBreeds(safeArgs.breedName).observe(viewLifecycleOwner, Observer { listOfSubBreeds ->
            listOfSubBreeds?.let {
                allSubBreeds = it.subBreeds
                subBreedAdapter.swap(it.subBreeds)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewSubBreeds.apply {
            subBreedAdapter = SubBreedAdapter(allSubBreeds, this@SubBreedFragment)
            layoutManager = LinearLayoutManager(this@SubBreedFragment.context)
            adapter = subBreedAdapter
        }
    }

    override fun onItemSelected(position: Int, item: String) {
        val navDirection = SubBreedFragmentDirections
            .actionNavigationSubBreedToNavigationImages(safeArgs.breedName, item.toLowerCase())

        findNavController().navigate(navDirection)
    }
}