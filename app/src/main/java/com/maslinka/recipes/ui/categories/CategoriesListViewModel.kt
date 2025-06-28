package com.maslinka.recipes.ui.categories


import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Category
import kotlinx.coroutines.launch


class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val recipesRepository = RecipesRepository()
    private val _categoryListState = MutableLiveData<CategoriesListState>()
    val categoryListState: LiveData<CategoriesListState>
        get() = _categoryListState

    fun loadCategories() {
        viewModelScope.launch {
            val result = recipesRepository.getCategories()
            if (result != null) {
                _categoryListState.value = CategoriesListState(
                    categoriesList = result
                )
            } else {
                Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG).show()
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
                Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun navigationReset(){
        _categoryListState.value = _categoryListState.value?.copy(navigationData = null)
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Category? = null
    )
}