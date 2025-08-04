package com.maslinka.recipes.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.R
import com.maslinka.recipes.common.Event
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.Constants.IMAGE_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipesRepository: RecipesRepository) : ViewModel() {


    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    private val _showToast = MutableLiveData<Event<Int>>()
    val showToast: LiveData<Event<Int>>
        get() = _showToast

    init {
        Log.i("!!!", "ViewModel init")
    }

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val result = recipesRepository.getRecipeById(recipeId)
            if (result != null) {
                val isFavourite =
                    recipesRepository.getFavouriteStatus(recipeId)
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
                _showToast.value = Event(R.string.network_error)
            }
        }
    }


    fun updateServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfServings = servings)
    }

    fun onFavoritesClicked(recipeId: Int) {
        viewModelScope.launch {
            val isFavourite = recipesRepository.getFavouriteStatus(recipeId)
            val currentState = _recipeState.value ?: return@launch
            if (isFavourite) {
                _recipeState.value = currentState.copy(isFavourite = false)
                recipesRepository.updateFavouritesInCache(recipeId, false)
            } else {
                _recipeState.value = currentState.copy(isFavourite = true)
                recipesRepository.updateFavouritesInCache(recipeId, true)
            }
        }

    }


    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeImageUrl: String? = null,
    )

}