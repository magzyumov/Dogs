package ru.magzyumov.dogs.ui.main


import kotlinx.android.synthetic.main.fragment_images.*
import ru.magzyumov.dogs.ui.adapter.BreedAdapter

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log

import android.view.*
import androidx.fragment.app.Fragment

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.ui.adapter.ImageAdapter
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject


class ImagesFragment: Fragment(), ImageAdapter.Interaction {

    @Inject
    lateinit var mainViewModel: MainViewModel

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var allImages: List<String>
    private lateinit var safeArgs: ImagesFragmentArgs

    init {
        App.getComponent().inject(this)
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

        prepareArguments()
        initRecyclerView()
        observerLiveData()
    }

    private fun prepareArguments() {
        arguments?.let {
            safeArgs = ImagesFragmentArgs.fromBundle( it )
        }
    }

    private fun observerLiveData() {
        when(safeArgs.subBreedName){
            NULL_SUB_BREED -> {
                mainViewModel.getImagesForBreed(safeArgs.breedName).observe(viewLifecycleOwner, Observer { listOfImages ->
                    listOfImages?.let {
                        allImages = it.images
                        imageAdapter.swap(it.images)
                    }
                })
                requireActivity().toolbar.title = safeArgs.breedName.capitalize()
            }
            else -> {
                mainViewModel.getImagesForBreed(safeArgs.breedName, safeArgs.subBreedName).observe(viewLifecycleOwner, Observer { listOfImages ->
                    listOfImages?.let {
                        allImages = it.images
                        imageAdapter.swap(it.images)
                    }
                })
                requireActivity().toolbar.title = safeArgs.subBreedName.capitalize()
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