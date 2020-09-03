package ru.magzyumov.dogs.ui.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_images.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.model.entity.FavouritesEntity
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
    private lateinit var breedName: String

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
        setHasOptionsMenu(true)
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
                when(safeArgs.local){
                    false -> {
                        mainViewModel.getImagesForBreed(safeArgs.breedName).observe(
                            viewLifecycleOwner,
                            Observer { listOfImages ->
                                listOfImages?.let {
                                    allImages = it.images
                                    imageAdapter.swap(it.images)
                                    fragmentWorker.dataReady(true)
                                }
                            })
                        breedName = safeArgs.breedName
                        fragmentWorker.changePageTitle(breedName)
                    }
                    true -> {
                        mainViewModel.getFavouriteImages(safeArgs.breedName).observe(
                            viewLifecycleOwner,
                            Observer { listOfImages ->
                                listOfImages?.let {
                                    allImages = it
                                    imageAdapter.swap(it)
                                    fragmentWorker.dataReady(true)
                                }
                            })
                        breedName = safeArgs.breedName
                        fragmentWorker.changePageTitle(breedName)
                    }
                }
            }
            else -> {
                mainViewModel.getImagesForBreed(safeArgs.breedName, safeArgs.subBreedName).observe(
                    viewLifecycleOwner,
                    Observer { listOfImages ->
                        listOfImages?.let {
                            allImages = it.images
                            imageAdapter.swap(it.images)
                            fragmentWorker.dataReady(true)
                        }
                    })
                breedName = safeArgs.subBreedName
                fragmentWorker.changePageTitle(breedName)
            }
        }
        mainViewModel.getNetworkStatus().observe(viewLifecycleOwner, Observer { networkStatus ->
            networkStatus?.let {
                fragmentWorker.showMessage(getString(R.string.title_network_trouble), it)
            }
        })
    }

    private fun initRecyclerView() {
        recyclerViewImages.apply {
            imageAdapter = ImageAdapter(allImages, this@ImagesFragment)
            layoutManager = LinearLayoutManager(
                this@ImagesFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = imageAdapter
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewImages)
    }

    override fun onLikeSelected(position: Int, item: String) {
        when(safeArgs.local){
            false -> {
                mainViewModel.insertFavourite(FavouritesEntity(breed = breedName, picture = item))
            }
            true -> {
                mainViewModel.deleteFavouriteByPhoto(item)
            }
        }
    }

    override fun onShareSelected(item: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, item)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}