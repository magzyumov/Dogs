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
    lateinit var mMainViewModel: MainViewModel

    private lateinit var mSubBreedAdapter: SubBreedAdapter
    private lateinit var mAllSubBreeds: List<String>
    private lateinit var mSafeArgs: SubBreedFragmentArgs
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
        mAllSubBreeds = arrayListOf()
        return inflater.inflate(R.layout.fragment_sub_breeds, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFragmentWorker.dataReady(false)

        prepareArguments()
        initRecyclerView()
        observerLiveData()
    }

    private fun prepareArguments() {
        arguments?.let {
            mSafeArgs = SubBreedFragmentArgs.fromBundle(it)
        }
        mFragmentWorker.changePageTitle(mSafeArgs.breedName)
    }

    private fun observerLiveData() {
        mMainViewModel.getSubBreeds(mSafeArgs.breedName).observe(viewLifecycleOwner, Observer { listOfSubBreeds ->
            listOfSubBreeds?.let {
                mAllSubBreeds = it.subBreeds
                mSubBreedAdapter.swap(it.subBreeds)
                mFragmentWorker.dataReady(true)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewSubBreeds.apply {
            mSubBreedAdapter = SubBreedAdapter(mAllSubBreeds, this@SubBreedFragment)
            layoutManager = LinearLayoutManager(this@SubBreedFragment.context)
            adapter = mSubBreedAdapter
        }
    }

    override fun onItemSelected(position: Int, item: String) {
        val navDirection = SubBreedFragmentDirections.actionNavigationSubBreedToNavigationImages(
            mSafeArgs.breedName,
            item.toLowerCase()
        )

        findNavController().navigate(navDirection)
    }
}