package ru.magzyumov.dogs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.item_image.view.*
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.databinding.ItemImageBinding


class ImageAdapter(images: List<String>,
                   private val interaction: Interaction): RecyclerView.Adapter<ImageAdapter.ImageHolder>() {

    private var mImages: MutableList<String> = mutableListOf()

    init {
        mImages.addAll(images)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemImageBinding = DataBindingUtil.inflate(inflater, R.layout.item_image, parent, false)
        return ImageHolder(binding, interaction)
    }

    override fun getItemCount(): Int = mImages.size

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(mImages[position])
    }

    fun swap(images: List<String>) {
        val diffCallback = DiffCallback(mImages, images)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mImages.clear()
        mImages.addAll(images)
        diffResult.dispatchUpdatesTo(this)
    }

    class ImageHolder(binding: ItemImageBinding,
                      private val interaction: Interaction): RecyclerView.ViewHolder(binding.root) {
        private val mBinding: ItemImageBinding = binding

        fun bind(image: String) {
            mBinding.image = image
            mBinding.executePendingBindings()
            mBinding.root.buttonShare.setOnClickListener{
                interaction.onShareSelected(image)
            }
            mBinding.root.buttonLike.setOnClickListener{
                interaction.onLikeSelected(adapterPosition, image)
            }
        }
    }

    class DiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface Interaction {
        fun onShareSelected(item: String)
        fun onLikeSelected(position: Int, item: String)
    }

    companion object {
        @JvmStatic
        @BindingAdapter("dogImage")
        fun loadImage(view: ImageView, image: String) {
            Glide.with(view.context)
                .load(image)
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(view)
        }
    }
}
