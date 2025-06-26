package com.maslinka.recipes.ui.recipes.favourites

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences
import java.util.concurrent.Executors

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val recipesRepository = RecipesRepository()

    private val thread = Executors.newSingleThreadExecutor()

    private val _favouritesState = MutableLiveData<FavouritesState>()
    val favouritesState: LiveData<FavouritesState>
        get() = _favouritesState


    fun initState() {
        _favouritesState.value = FavouritesState(
            contentDescription = R.string.content_description_favourites_fragment,
        )
    }

    fun updateFavouritesList() {
        thread.execute {
            recipesRepository.getRecipesByIds(
                AccessToPreferences.getFavourites(appContext.applicationContext),
                { result ->
                    if (result != null) {
                        Handler(Looper.getMainLooper()).post {
                            _favouritesState.value = favouritesState.value?.copy(
                                favouritesList = result,
                                listIsEmpty = result.isEmpty()
                            ) ?: FavouritesState(
                                favouritesList = result,
                                listIsEmpty = result.isEmpty(),
                                headerImageUrl = R.drawable.bcg_favorites,
                                contentDescription = R.string.content_description_favourites_fragment,
                            )
                        }
                    } else {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            )
        }
    }


    data class FavouritesState(
        val headerImageUrl: Int = R.drawable.bcg_favorites,
        val contentDescription: Int? = null,
        val favouritesList: List<Recipe> = emptyList(),
        val listIsEmpty: Boolean = true,
    )
}