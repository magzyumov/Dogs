package ru.magzyumov.dogs.ui.fragments


import android.content.Context
import kotlinx.android.synthetic.main.fragment_images.*

import android.os.Bundle

import android.util.Log

import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.ui.adapter.ImageAdapter
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject


class ImagesFragment: Fragment(), ImageAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var allImages: List<String>
    private lateinit var safeArgs: ImagesFragmentArgs
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
        allImages = arrayListOf()
        return inflater.inflate(R.layout.fragment_images, container, false)
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
            safeArgs = ImagesFragmentArgs.fromBundle(it)
        }
    }

    private fun observerLiveData() {
        when(safeArgs.subBreedName){
            NULL_SUB_BREED -> {
                mainViewModel.getImagesForBreed(safeArgs.breedName).observe(viewLifecycleOwner, Observer { listOfImages ->
                    listOfImages?.let {
                        allImages = it.images
                        imageAdapter.swap(it.images)
                        fragmentWorker.dataReady(true)
                    }
                })
                fragmentWorker.changePageTitle(safeArgs.breedName)
            }
            else -> {
                mainViewModel.getImagesForBreed(safeArgs.breedName, safeArgs.subBreedName).observe(viewLifecycleOwner, Observer { listOfImages ->
                    listOfImages?.let {
                        allImages = it.images
                        imageAdapter.swap(it.images)
                        fragmentWorker.dataReady(true)
                    }
                })
                fragmentWorker.changePageTitle(safeArgs.subBreedName)
            }
        }

    }

    private fun initRecyclerView() {
        recyclerViewImages.apply {
            imageAdapter = ImageAdapter(allImages, this@ImagesFragment)
            layoutManager = LinearLayoutManager(this@ImagesFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }

    override fun onItemSelected(position: Int, item: String) {
        Log.e("Position", "$position")
        Log.e("Item", item)
    }
}