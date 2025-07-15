package com.maslinka.recipes.ui.recipes.recipeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.Constants.IMAGE_URL
import kotlinx.coroutines.launch

class RecipeListViewModel(private val recipesRepository:RecipesRepository) : ViewModel() {



    private val _recipeListState = MutableLiveData<RecipeListState>()
    val recipeListState: LiveData<RecipeListState>
        get() = _recipeListState


    fun initState(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        _recipeListState.value = recipeListState.value?.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = IMAGE_URL + categoryImageUrl,
        ) ?: RecipeListState(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = IMAGE_URL + categoryImageUrl,
        )
        categoryId?.let {
            getRecipeListFromCache(it)
            getRecipeList(it)
        }
    }

    fun resetError() {
        _recipeListState.value = recipeListState.value?.copy(showNetworkError = false)
    }

    private fun getRecipeListFromCache(categoryId: Int) {
        viewModelScope.launch {
            val result = recipesRepository.getRecipesFromCache(categoryId)
            _recipeListState.value = recipeListState.value?.copy(
                recipeList = result
            )
        }
    }


    private fun getRecipeList(categoryId: Int) {
        viewModelScope.launch {
            val result = recipesRepository.getRecipesByCategoryId(categoryId)
            if (result != null) {
                _recipeListState.value = recipeListState.value?.copy(
                    recipeList = result
                )
            } else {
                _recipeListState.value = recipeListState.value?.copy(showNetworkError = true)
            }
        }
    }


    data class RecipeListState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        val recipeList: List<Recipe> = emptyList(),
        val showNetworkError: Boolean = false,
    )
}