package com.maslinka.recipes.ui.categories

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Category
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_ID
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_IMAGE_URL
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_NAME

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
            navigationData = bundleOf(
                ARG_CATEGORY_ID to category.id,
                ARG_CATEGORY_NAME to category.title,
                ARG_CATEGORY_IMAGE_URL to category.imageUrl
            )
        )
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Bundle? = null
    )
}