package com.maslinka.recipes.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maslinka.recipes.model.Recipe

class RecipeViewModel: ViewModel() {
    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeDrawable: Drawable? = null,
    )


    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val mutableCurrentRecipeState = MutableLiveData<RecipeState>()
    val currentRecipeState :LiveData<RecipeState>
        get() = mutableCurrentRecipeState

    init {
        Log.i("!!!", "ViewModel init")
        mutableCurrentRecipeState.value = RecipeState(isFavourite = true)
    }
}