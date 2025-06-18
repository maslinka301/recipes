package com.maslinka.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maslinka.recipes.databinding.ItemMethodStepBinding

class MethodAdapter : Adapter<MethodAdapter.MethodViewHolder>() {

    var dataSet: List<String> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MethodViewHolder(val binding: ItemMethodStepBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MethodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMethodStepBinding.inflate(inflater, parent, false)
        return MethodViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: MethodViewHolder, position: Int) {
        holder.binding.tvCookingMethodStep.text = "${position + 1}. ${dataSet[position]}"
    }
}