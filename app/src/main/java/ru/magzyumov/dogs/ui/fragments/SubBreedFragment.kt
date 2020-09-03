package ru.magzyumov.dogs.ui.fragments


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_sub_breeds.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.ui.adapter.SubBreedAdapter
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import javax.inject.Inject


class SubBreedFragment: Fragment(), SubBreedAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var subBreedAdapter: SubBreedAdapter
    private lateinit var allSubBreeds: List<String>
    private lateinit var safeArgs: SubBreedFragmentArgs
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
        allSubBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_sub_breeds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentWorker.dataReady(false)

        prepareArguments()
        initRecyclerView()
        observerLiveData()
    }

    private fun prepareArguments() {
        arguments?.let {
            safeArgs = SubBreedFragmentArgs.fromBundle(it)
        }
        fragmentWorker.changePageTitle(safeArgs.breedName)
    }

    private fun observerLiveData() {
        mainViewModel.getSubBreeds(safeArgs.breedName).observe(viewLifecycleOwner, Observer { listOfSubBreeds ->
            listOfSubBreeds?.let {
                allSubBreeds = it.subBreeds
                subBreedAdapter.swap(it.subBreeds)
                fragmentWorker.dataReady(true)
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
        val navDirection = SubBreedFragmentDirections.actionNavigationSubBreedToNavigationImages(
            safeArgs.breedName,
            item.toLowerCase()
        )

        findNavController().navigate(navDirection)
    }
}