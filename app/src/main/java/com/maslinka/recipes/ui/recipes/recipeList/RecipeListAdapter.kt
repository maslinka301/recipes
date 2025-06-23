package com.maslinka.recipes.ui.recipes.recipeList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ItemRecipeBinding
import com.maslinka.recipes.model.Recipe

class RecipeListAdapter() : Adapter<RecipeListAdapter.RecipeListViewHolder>() {

    var dataSet: List<Recipe> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val imagesUrl = "https://recipes.androidsprint.ru/api/images/"

    class RecipeListViewHolder(val binding: ItemRecipeBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return RecipeListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        holder.binding.tvRecipe.text = dataSet[position].title

        holder.binding.ivRecipe.apply {
            Glide
                .with(context)
                .load(imagesUrl + dataSet[position].imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(this)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}