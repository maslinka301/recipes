package com.maslinka.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maslinka.recipes.databinding.ItemCategoryListBinding

class IngredientsAdapter(val dataSet: List<Ingredient>) :
    Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var quantity = 1

    class IngredientViewHolder(val binding: ItemCategoryListBinding) : ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryListBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val amount = (dataSet[position].quantity).toDouble() * quantity
        holder.binding.tvIngredientAmount.text =
            if (amount % 1 == 0.0) {
                "${amount.toInt()} ${dataSet[position].unitOfMeasure}"
            } else {
                "${amount.toString().format("%.1f")} ${dataSet[position].unitOfMeasure}"
            }

        holder.binding.tvIngredientTitle.text = dataSet[position].description
//        holder.binding.tvIngredientAmount.text =
//            "${amount} ${dataSet[position].unitOfMeasure}"
    }

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}