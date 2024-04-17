package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.databinding.ItemSaveBinding

class HistoryAdapter : ListAdapter<History, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var onClick: ((History) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onClick)
    }

    class MyViewHolder(private val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: History, onClick: ((History) -> Unit)?){
            binding.tvPrediction.text = currentItem.prediction
            binding.tvScore.text = currentItem.score
            binding.delete.setOnClickListener {
                onClick?.invoke(currentItem)
            }

            Glide.with(binding.root)
                .load(currentItem.image)
                .into(binding.ivImage)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}