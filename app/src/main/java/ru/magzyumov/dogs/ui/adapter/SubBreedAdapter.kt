package ru.magzyumov.dogs.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.magzyumov.dogs.R
import ru.magzyumov.dogs.databinding.ItemSubBreedBinding

class SubBreedAdapter(subBreeds: List<String>,
                      private val interaction: Interaction? = null): RecyclerView.Adapter<SubBreedAdapter.SubBreedHolder>() {

    private var mSubBreeds: MutableList<String> = mutableListOf()

    init {
        mSubBreeds.addAll(subBreeds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubBreedHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSubBreedBinding = DataBindingUtil.inflate(inflater, R.layout.item_sub_breed, parent, false)
        return SubBreedHolder(binding, interaction)
    }

    override fun getItemCount(): Int = mSubBreeds.size

    override fun onBindViewHolder(holder: SubBreedHolder, position: Int) {
        holder.bind(mSubBreeds[position])
    }

    fun swap(subBreeds: List<String>) {
        val diffCallback = DiffCallback(mSubBreeds, subBreeds)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        mSubBreeds.clear()
        mSubBreeds.addAll(subBreeds)
        diffResult.dispatchUpdatesTo(this)
    }

    class SubBreedHolder(binding: ItemSubBreedBinding,
                         private val interaction: Interaction?): RecyclerView.ViewHolder(binding.root) {
        private val mBinding: ItemSubBreedBinding = binding

        fun bind(subBreed: String) {
            mBinding.subBreed = subBreed
            mBinding.executePendingBindings()
            mBinding.root.setOnClickListener{
                interaction?.onItemSelected(adapterPosition, subBreed)
            }
        }
    }

    class DiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

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
        fun onItemSelected(position: Int, item: String)
    }
}
