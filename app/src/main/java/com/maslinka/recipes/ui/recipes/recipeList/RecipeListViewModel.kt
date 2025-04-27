package com.maslinka.recipes.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Recipe

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    val appContext = application.applicationContext

    data class RecipeListState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        val recipeList: List<Recipe> = emptyList(),
    )

    private val _recipeListState = MutableLiveData<RecipeListState>()
    val recipeListState: LiveData<RecipeListState>
        get() = _recipeListState

    fun getRecipeList(categoryId: Int) {
        _recipeListState.value = _recipeListState.value?.copy(
            recipeList = STUB.getRecipesByCategoryId(categoryId)
        )
    }

    fun initState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        _recipeListState.value = _recipeListState.value?.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImage = getImageFromAssets(categoryImageUrl),
        ) ?: RecipeListState(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImage = getImageFromAssets(categoryImageUrl),
        )
        categoryId?.let { getRecipeList(it) }
    }

    private fun getImageFromAssets(categoryImageUrl: String?): Drawable? {
        val drawable =
            try {
                categoryImageUrl?.let {
                    Drawable.createFromStream(
                        appContext.assets.open(it),
                        null
                    )
                }
            } catch (e: Exception) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }
}