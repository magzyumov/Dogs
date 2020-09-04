package ru.magzyumov.dogs.ui.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_images.*
import ru.magzyumov.dogs.App
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.data.entity.FavouritesEntity
import ru.magzyumov.dogs.ui.adapter.ImageAdapter
import ru.magzyumov.dogs.ui.main.IFragmentWorker
import ru.magzyumov.dogs.ui.main.MainViewModel
import ru.magzyumov.dogs.util.NULL_SUB_BREED
import javax.inject.Inject


class ImagesFragment: Fragment(), ImageAdapter.Interaction {

    @Inject
    lateinit var mMainViewModel: MainViewModel

    private lateinit var mImageAdapter: ImageAdapter
    private lateinit var mAllImages: List<String>
    private lateinit var mSafeArgs: ImagesFragmentArgs
    private lateinit var mFragmentWorker: IFragmentWorker
    private lateinit var mBreedName: String
    private lateinit var mView: View

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
        setHasOptionsMenu(true)
        mAllImages = arrayListOf()
        mView = inflater.inflate(R.layout.fragment_images, container, false)
        return mView
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
            mSafeArgs = ImagesFragmentArgs.fromBundle(it)
        }
    }

    private fun observerLiveData() {
        when(mSafeArgs.subBreedName){
            NULL_SUB_BREED -> {
                when(mSafeArgs.local){
                    false -> { observeBreedImages (mSafeArgs.breedName) }
                    true -> { observeLocalImages(mSafeArgs.breedName)
                    }
                }
            }
            else -> { observeSubBreedImages(mSafeArgs.breedName, mSafeArgs.subBreedName) }
        }
        mMainViewModel.getNetworkStatus().observe(viewLifecycleOwner, Observer { networkStatus ->
            networkStatus?.let {
                mFragmentWorker.showMessage(getString(R.string.title_network_trouble), it)
            }
        })
    }

    private fun observeBreedImages(breed: String){
        mMainViewModel.getImagesForBreed(breed).observe(
            viewLifecycleOwner,
            Observer { listOfImages ->
                listOfImages?.let {
                    mAllImages = it.images
                    mImageAdapter.swap(it.images)
                    mFragmentWorker.dataReady(true)
                }
            })
        mBreedName = mSafeArgs.breedName
        mFragmentWorker.changePageTitle(mBreedName)
    }

    private fun observeSubBreedImages(breed: String, subBreed: String){
        mMainViewModel.getImagesForBreed(breed, subBreed).observe(
            viewLifecycleOwner,
            Observer { listOfImages ->
                listOfImages?.let {
                    mAllImages = it.images
                    mImageAdapter.swap(it.images)
                    mFragmentWorker.dataReady(true)
                }
            })
        mBreedName = mSafeArgs.subBreedName
        mFragmentWorker.changePageTitle(mBreedName)
    }

    private fun observeLocalImages(breed: String){
        mMainViewModel.getFavouriteImages(breed).observe(
            viewLifecycleOwner,
            Observer { listOfImages ->
                listOfImages?.let {
                    mAllImages = it
                    mImageAdapter.swap(it)
                    mFragmentWorker.dataReady(true)
                }
            })
        mBreedName = mSafeArgs.breedName
        mFragmentWorker.changePageTitle(mBreedName)
    }

    private fun initRecyclerView() {
        recyclerViewImages.apply {
            mImageAdapter = ImageAdapter(mAllImages, this@ImagesFragment)
            layoutManager = LinearLayoutManager(
                this@ImagesFragment.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = mImageAdapter
        }
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewImages)
    }

    override fun onLikeSelected(position: Int, item: String) {
        when(mSafeArgs.local){
            false -> {
                mMainViewModel.insertFavourite(FavouritesEntity(breed = mBreedName.capitalize(), picture = item))
                Snackbar.make(mView, getString(R.string.snake_liked), Snackbar.LENGTH_SHORT).show()
            }
            true -> {
                mMainViewModel.deleteFavouriteByPhoto(item)
                Snackbar.make(mView, getString(R.string.snake_disliked), Snackbar.LENGTH_SHORT).show()
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