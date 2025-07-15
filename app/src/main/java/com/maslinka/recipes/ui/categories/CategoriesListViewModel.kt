package com.maslinka.recipes.ui.categories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Category
import kotlinx.coroutines.launch


class CategoriesListViewModel(private val recipesRepository: RecipesRepository) : ViewModel() {

    private val _categoryListState = MutableLiveData<CategoriesListState>()
    val categoryListState: LiveData<CategoriesListState>
        get() = _categoryListState

    fun loadCategoriesFromCache() {
        viewModelScope.launch {
            val result = recipesRepository.getCategoriesFromCache()
            _categoryListState.value = CategoriesListState(
                categoriesList = result
            )
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            val result = recipesRepository.getCategories()
            if (result != null) {
                _categoryListState.value = CategoriesListState(
                    categoriesList = result
                )
            } else {
                _categoryListState.value = categoryListState.value?.copy(showNetworkError = true)
            }
        }
    }

    fun prepareNavigation(categoryId: Int) {
        viewModelScope.launch {
            val result = recipesRepository.getCategory(categoryId)
            if (result != null) {
                _categoryListState.value = categoryListState.value?.copy(
                    navigationData = result
                )
            } else {
                _categoryListState.value = categoryListState.value?.copy(showNetworkError = true)
            }
        }
    }

    fun navigationReset() {
        _categoryListState.value = _categoryListState.value?.copy(navigationData = null)
    }

    fun resetError() {
        _categoryListState.value = categoryListState.value?.copy(showNetworkError = false)
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Category? = null,
        val showNetworkError: Boolean = false,
    )
}