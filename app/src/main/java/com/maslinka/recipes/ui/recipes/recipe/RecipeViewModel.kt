package com.maslinka.recipes.ui.recipes.recipe

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel

class RecipeViewModel: ViewModel() {
    data class RecipeFragmentState(
        val recipeId: Int? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeDrawable: Drawable? = null,
    )
}