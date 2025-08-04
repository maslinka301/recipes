package com.maslinka.recipes.ui.recipes.recipeList

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
class RecipeListViewModel @Inject constructor(private val recipesRepository:RecipesRepository) : ViewModel() {

    private val _recipeListState = MutableLiveData<RecipeListState>()
    val recipeListState: LiveData<RecipeListState>
        get() = _recipeListState

    private val _showToast = MutableLiveData<Event<Int>>()
    val showToast: LiveData<Event<Int>>
        get() = _showToast


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
                _showToast.value = Event(R.string.network_error)
            }
        }
    }


    data class RecipeListState(
        val categoryId: Int? = null,
        val categoryName: String? = null,
        val categoryImageUrl: String? = null,
        val recipeList: List<Recipe> = emptyList(),
    )
}