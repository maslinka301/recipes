package com.maslinka.recipes.ui.categories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.R
import com.maslinka.recipes.common.Event
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CategoriesListViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {

    private val _categoryListState = MutableLiveData<CategoriesListState>()
    val categoryListState: LiveData<CategoriesListState>
        get() = _categoryListState

    private val _showToast = MutableLiveData<Event<Int>>()
    val showToast: LiveData<Event<Int>>
        get() = _showToast

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
                _showToast.value = Event(R.string.network_error)
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
                _showToast.value = Event(R.string.network_error)
            }
        }
    }

    fun navigationReset() {
        _categoryListState.value = _categoryListState.value?.copy(navigationData = null)
    }


    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Category? = null,
    )
}