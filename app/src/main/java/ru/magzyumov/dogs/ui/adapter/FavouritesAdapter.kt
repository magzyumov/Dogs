package ru.magzyumov.dogs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.databinding.ItemFavouriteBinding
import ru.magzyumov.dogs.data.entity.FavouritesEntity.*
import ru.magzyumov.dogs.ui.adapter.FavouritesAdapter.*

class FavouritesAdapter(favourites: List<FavouritesCount>,
                        private val interaction: Interaction): RecyclerView.Adapter<FavouritesHolder>() {

    private var mFavourites: MutableList<FavouritesCount> = mutableListOf()

    init {
        mFavourites.addAll(favourites)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemFavouriteBinding = DataBindingUtil.inflate(inflater, R.layout.item_favourite, parent, false)
        return FavouritesHolder(binding, interaction)
    }

    override fun getItemCount(): Int {
        return mFavourites.size
    }

    override fun onBindViewHolder(holder: FavouritesHolder, position: Int) {
        holder.bind(favourite = mFavourites[position])
    }

    fun swap(favourites: List<FavouritesCount>) {
        val diffCallback = DiffCallback(mFavourites, favourites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mFavourites.clear()
        mFavourites.addAll(favourites)
        diffResult.dispatchUpdatesTo(this)
    }

    class FavouritesHolder(binding: ItemFavouriteBinding,
                       private val interaction: Interaction): RecyclerView.ViewHolder(binding.root) {
        private val mBinding: ItemFavouriteBinding = binding

        fun bind(favourite: FavouritesCount) {
            mBinding.favourite = favourite
            mBinding.executePendingBindings()
            mBinding.root.setOnClickListener{
                interaction.onItemSelected(adapterPosition, favourite)
            }
        }
    }

    class DiffCallback(
        private val oldList: List<FavouritesCount>,
        private val newList: List<FavouritesCount>
    ): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].breed == newList[newItemPosition].breed
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].breed == newList[newItemPosition].breed
                    && oldList[oldItemPosition].count == newList[newItemPosition].count
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: FavouritesCount)
    }
}
