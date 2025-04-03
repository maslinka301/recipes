package com.maslinka.recipes.ui.recipes.recipeList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maslinka.recipes.databinding.ItemRecipeBinding
import com.maslinka.recipes.model.Recipe
import java.io.IOException

class RecipeListAdapter(val dataSet: List<Recipe>):Adapter<RecipeListAdapter.RecipeListViewHolder>() {
    class RecipeListViewHolder(val binding: ItemRecipeBinding) : ViewHolder(binding.root) {

    }

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
        val drawable =
            try {
                Drawable.createFromStream(
                    holder.binding.root.context.assets.open(dataSet[position].imageUrl),
                    null
                )
            }
            catch (e: IOException){
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        holder.binding.ivRecipe.apply {
            setImageDrawable(drawable)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}