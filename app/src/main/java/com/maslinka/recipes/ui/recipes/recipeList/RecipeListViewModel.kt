package com.maslinka.recipes.ui.recipes.recipeList

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val recipesRepository = RecipesRepository()

    private val appContext = application.applicationContext

    private val _recipeListState = MutableLiveData<RecipeListState>()
    val recipeListState: LiveData<RecipeListState>
        get() = _recipeListState


    fun initState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        _recipeListState.value = recipeListState.value?.copy(
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

    private fun getRecipeList(categoryId: Int) {
        recipesRepository.getRecipesByCategoryId(categoryId, { result ->
            if(result!=null){
                Handler(Looper.getMainLooper()).post {
                    _recipeListState.value = recipeListState.value?.copy(
                        recipeList = result
                    )
                }
            }
            else{
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG).show()
                }
            }
        })
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

    data class RecipeListState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImage: Drawable? = null,
        val recipeList: List<Recipe> = emptyList(),
    )
}