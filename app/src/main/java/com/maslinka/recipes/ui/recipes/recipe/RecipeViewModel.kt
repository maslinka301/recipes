package com.maslinka.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences.getFavourites
import com.maslinka.recipes.ui.AccessToPreferences.saveFavourites


class RecipeViewModel(
    application: Application,
): AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeDrawable: Drawable? = null,
    )


    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState :LiveData<RecipeState>
        get() = _recipeState

    init {
        Log.i("!!!", "ViewModel init")
        //loadRecipe(recipe.id)
    }

    fun loadRecipe(recipeId: Int){
        //TODO load from network
        val listOfFavourites = getFavourites(appContext)

        _recipeState.value = RecipeState(STUB.getRecipeById(recipeId), isFavourite = recipeId in listOfFavourites)
    }

    fun onFavoritesClicked(recipeId: Int){
        val favouriteSet = getFavourites(appContext)
        val currentState = _recipeState.value ?: return
        if (recipeId in favouriteSet) {
            favouriteSet.remove(recipeId)
            _recipeState.value = currentState.copy(isFavourite = false)
        } else {
            favouriteSet.add(recipeId)
            _recipeState.value = currentState.copy(isFavourite = true)
        }
        saveFavourites(appContext,favouriteSet)
    }

}