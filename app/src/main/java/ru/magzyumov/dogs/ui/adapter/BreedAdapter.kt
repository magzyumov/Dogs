package ru.magzyumov.dogs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.databinding.ItemDogBinding
import ru.magzyumov.dogs.model.response.BreedsResponse.*
import ru.magzyumov.dogs.ui.adapter.BreedAdapter.*

class BreedAdapter(dogs: List<Breed>,
                   private val interaction: Interaction? = null): RecyclerView.Adapter<DogHolder>() {

    private var mDogs: MutableList<Breed> = mutableListOf()

    init {
        mDogs.addAll(dogs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemDogBinding = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false)
        return DogHolder(binding, interaction)
    }

    override fun getItemCount(): Int {
        return mDogs.size
    }

    override fun onBindViewHolder(holder: DogHolder, position: Int) {
        holder.bind(dog = mDogs[position])
    }

    fun swap(dogs: List<Breed>) {
        val diffCallback = DiffCallback(mDogs, dogs)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mDogs.clear()
        mDogs.addAll(dogs)
        diffResult.dispatchUpdatesTo(this)
    }

    class DogHolder(binding: ItemDogBinding,
                       private val interaction: Interaction?): RecyclerView.ViewHolder(binding.root) {
        private val mBinding: ItemDogBinding = binding

        fun bind(dog: Breed) {
            mBinding.dog = dog
            mBinding.executePendingBindings()
            mBinding.root.setOnClickListener{
                interaction?.onItemSelected(adapterPosition, dog)
            }
        }
    }

    class DiffCallback(
        private val oldList: List<Breed>,
        private val newList: List<Breed>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].breed == newList[newItemPosition].breed
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].breed == newList[newItemPosition].breed
                    && oldList[oldItemPosition].subBreeds == newList[newItemPosition].subBreeds
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Breed)
    }
}
