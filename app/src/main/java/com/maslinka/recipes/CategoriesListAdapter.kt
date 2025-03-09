package com.maslinka.recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.maslinka.recipes.databinding.ItemCategoryBinding
import java.io.IOException


class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    //переменная хранит экземпляр слушателя для колбека
    var itemClickListener: onItemClickListener? = null

    //Интерфейс для колбека
    interface onItemClickListener{
        fun onItemClick()
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategoryTitle.text = category.title
            binding.tvCategoryDescription.text = category.title
            binding.ivCategoryImage
        }

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
        holder.binding.ivCategoryImage.contentDescription = "${dataSet[position].title} ${holder.binding.root.context.getString(R.string.content_description_image_category)}"
        Log.d("!!!", "${dataSet[position].title} ${holder.binding.root.context.getString(R.string.content_description_image_category)}")

        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick()
        }
    }






}