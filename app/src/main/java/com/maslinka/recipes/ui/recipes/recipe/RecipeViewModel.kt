package com.maslinka.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences.getFavourites
import com.maslinka.recipes.ui.AccessToPreferences.saveFavourites
import com.maslinka.recipes.ui.Constants.IMAGE_URL
import kotlinx.coroutines.launch


class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext

    private val recipesRepository = RecipesRepository()

    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    init {
        Log.i("!!!", "ViewModel init")
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val result = recipesRepository.getRecipeById(recipeId)
            if (result != null) {
                val isFavourite = recipeId in getFavourites(appContext)
                val recipeDrawable = IMAGE_URL + result.imageUrl
                _recipeState.value = recipeState.value?.copy(
                    recipe = result,
                    isFavourite = isFavourite,
                    recipeImageUrl = recipeDrawable
                ) ?: RecipeState(
                    recipe = result,
                    isFavourite = isFavourite,
                    recipeImageUrl = recipeDrawable
                )
            } else {
                Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun updateServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfServings = servings)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favouriteSet = getFavourites(appContext)
        val currentState = _recipeState.value ?: return
        if (recipeId in favouriteSet) {
            favouriteSet.remove(recipeId)
            _recipeState.value = currentState.copy(isFavourite = false)
        } else {
            favouriteSet.add(recipeId)
            _recipeState.value = currentState.copy(isFavourite = true)
        }
        saveFavourites(appContext, favouriteSet)
    }


    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeImageUrl: String? = null,
    )

}