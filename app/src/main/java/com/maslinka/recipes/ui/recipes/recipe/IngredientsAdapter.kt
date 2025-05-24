package com.maslinka.recipes.ui.recipes.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maslinka.recipes.databinding.ItemCategoryListBinding
import com.maslinka.recipes.model.Ingredient
import java.math.BigDecimal

class IngredientsAdapter:
    Adapter<IngredientsAdapter.IngredientViewHolder>() {

    private var quantity :BigDecimal = BigDecimal(1)

    var dataSet: List<Ingredient> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


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
        val amount = BigDecimal(dataSet[position].quantity) * quantity
        //stripTrailingZeros() - удаление незначащих нулей
        //scale() - возвращает кол-во знаков после запятой
        holder.binding.tvIngredientAmount.text =
            when (amount.stripTrailingZeros().scale()){
                0 -> "${amount.toInt()} ${dataSet[position].unitOfMeasure}"
                else -> "${amount.stripTrailingZeros().toPlainString()} ${dataSet[position].unitOfMeasure}"
            }
        holder.binding.tvIngredientTitle.text = dataSet[position].description
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress.toBigDecimal()
        notifyDataSetChanged()
    }

}