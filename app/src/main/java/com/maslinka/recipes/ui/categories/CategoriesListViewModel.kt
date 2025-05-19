package com.maslinka.recipes.ui.categories


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Category

class CategoriesListViewModel : ViewModel() {


    private val _categoryListState = MutableLiveData<CategoriesListState>()
    val categoryListState: LiveData<CategoriesListState>
        get() = _categoryListState

    fun loadCategories() {
        _categoryListState.value = CategoriesListState(
            categoriesList = STUB.getCategories()
        )
    }

    fun prepareNavigation(categoryId: Int) {
        val category = STUB.getCategory(categoryId)
        _categoryListState.value = categoryListState.value?.copy(
            navigationData = category
        )
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Category? = null
    )
}