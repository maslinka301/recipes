package com.maslinka.recipes.ui.categories


import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Category

class CategoriesListViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val recipesRepository = RecipesRepository()
    private val _categoryListState = MutableLiveData<CategoriesListState>()
    val categoryListState: LiveData<CategoriesListState>
        get() = _categoryListState

    fun loadCategories() {
        recipesRepository.getCategories { result ->
            if (result != null){
                Handler(Looper.getMainLooper()).post {
                    _categoryListState.value = CategoriesListState(
                        categoriesList = result
                    )
                }
            }
            else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun prepareNavigation(categoryId: Int) {
//        val category = STUB.getCategory(categoryId)
//        _categoryListState.value = categoryListState.value?.copy(
//            navigationData = category
//        )
        recipesRepository.getCategory(categoryId,{ result ->
            if(result != null){
                Handler(Looper.getMainLooper()).post {
                    _categoryListState.value = categoryListState.value?.copy(
                        navigationData = result
                    )
                }
            }else{
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(appContext, "НЕ РОБИТ", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    data class CategoriesListState(
        val categoriesList: List<Category> = emptyList(),
        val navigationData: Category? = null
    )
}