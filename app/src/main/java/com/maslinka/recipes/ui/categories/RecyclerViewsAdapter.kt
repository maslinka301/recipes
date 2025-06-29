package com.maslinka.recipes.ui.categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ItemCategoryBinding
import com.maslinka.recipes.databinding.ItemRecipeBinding
import com.maslinka.recipes.model.Category
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.Constants.IMAGE_URL

class RecyclerViewsAdapter() : RecyclerView.Adapter<ViewHolder>() {
    var dataSet: List<Any> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        const val CATEGORY_TYPE = 1
        const val RECIPE_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position]) {
            is Category -> CATEGORY_TYPE
            is Recipe -> RECIPE_TYPE
            else -> throw IllegalArgumentException("Unknown type")
        }
    }


    //переменная хранит экземпляр слушателя для колбека
    private var itemClickListener: OnItemClickListener? = null

    //Интерфейс для колбека
    interface OnItemClickListener {
        fun onItemClick(itemId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class CategoryViewHolder(private val binding: ItemCategoryBinding) : ViewHolder(binding.root) {
        fun bind(category: Category, itemClickListener: OnItemClickListener?) {
            binding.tvCategoryTitle.text = category.title
            binding.tvCategoryDescription.text = category.description

            binding.ivCategoryImage.apply {
                Glide
                    .with(context)
                    .load(IMAGE_URL + category.imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(this)
                scaleType = ImageView.ScaleType.CENTER_CROP
                contentDescription = String.format(
                    binding.root.context.getString(R.string.content_description_category_item),
                    category.title.lowercase()
                )
            }

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(category.id)
            }
        }
    }

    class RecipeViewHolder(private val binding: ItemRecipeBinding) : ViewHolder(binding.root) {
        fun bind(recipe: Recipe, itemClickListener: OnItemClickListener?) {
            binding.tvRecipe.text = recipe.title

            binding.ivRecipe.apply {
                Glide
                    .with(context)
                    .load(IMAGE_URL + recipe.imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(this)
                scaleType = ImageView.ScaleType.CENTER_CROP
                contentDescription = String.format(
                    binding.root.context.getString(R.string.content_description_recipe_item),
                    recipe.title.lowercase()
                )
            }

            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(recipe.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> CategoryViewHolder(ItemCategoryBinding.inflate(inflater, parent, false))
            2 -> RecipeViewHolder(ItemRecipeBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(dataSet[position] as Category, itemClickListener)
            is RecipeViewHolder -> holder.bind(dataSet[position] as Recipe, itemClickListener)
        }
    }
}