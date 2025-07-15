package com.maslinka.recipes.ui.recipes.favourites

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import kotlinx.coroutines.launch

class FavouritesViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {


    private val _favouritesState = MutableLiveData<FavouritesState>()
    val favouritesState: LiveData<FavouritesState>
        get() = _favouritesState


    fun initState() {
        _favouritesState.value = FavouritesState(
            contentDescription = R.string.content_description_favourites_fragment,
        )
    }

    fun getFavouritesListFromCache() {
        viewModelScope.launch {
            Log.d("!!!", recipesRepository.getFavouritesRecipesFromCache().toString())
            val favouritesList = recipesRepository.getFavouritesRecipesFromCache()
            _favouritesState.value =
                favouritesState.value?.copy(
                    favouritesList = favouritesList,
                    listIsEmpty = favouritesList.isEmpty()
                )
        }
    }

    fun resetError() {
        _favouritesState.value = favouritesState.value?.copy(showNetworkError = false)
    }


    data class FavouritesState(
        val headerImageUrl: Int = R.drawable.bcg_favorites,
        val contentDescription: Int? = null,
        val favouritesList: List<Recipe> = emptyList(),
        val listIsEmpty: Boolean = true,
        val showNetworkError: Boolean = false,
    )
}