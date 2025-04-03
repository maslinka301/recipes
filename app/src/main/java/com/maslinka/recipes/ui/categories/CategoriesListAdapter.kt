package com.maslinka.recipes.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ItemCategoryBinding
import com.maslinka.recipes.model.Category
import java.io.IOException


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    //переменная хранит экземпляр слушателя для колбека
    private var itemClickListener: OnItemClickListener? = null

    //Интерфейс для колбека
    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCategoryTitle.text = dataSet[position].title
        holder.binding.tvCategoryDescription.text = dataSet[position].description

        val drawable =
            try {
                Drawable.createFromStream(
                    holder.binding.ivCategoryImage.context.assets.open(dataSet[position].imageUrl),
                    null
                )
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        holder.binding.ivCategoryImage.setImageDrawable(drawable)
        holder.binding.ivCategoryImage.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.binding.ivCategoryImage.contentDescription = String.format(
            holder.binding.root.context.getString(R.string.content_description_category_item),
            dataSet[position].title.lowercase()
        )

        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(dataSet[position].id)
        }
    }


}