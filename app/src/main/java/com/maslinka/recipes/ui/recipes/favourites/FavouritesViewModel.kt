package com.maslinka.recipes.ui.recipes.favourites

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.R
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences
import java.io.IOException

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    data class FavouritesState(
        val headerImage: Drawable? = null,
        val contentDescription: Int? = null,
        val favouritesList: List<Recipe> = emptyList(),
        val listIsEmpty: Boolean = true,
    )


    private val _favouritesState = MutableLiveData<FavouritesState>()
    val favouritesState: LiveData<FavouritesState>
        get() = _favouritesState

    init {
        _favouritesState.value = FavouritesState(
            headerImage = getImageFromAssets(),
            contentDescription = R.string.content_description_favourites_fragment,
        )
    }

    fun updateFavouritesList() {
        val favouritesList =
            STUB.getRecipesByIds(AccessToPreferences.getFavourites(appContext.applicationContext))
        _favouritesState.value = _favouritesState.value?.copy(
            favouritesList = favouritesList,
            listIsEmpty = favouritesList.isEmpty()
        )
    }

    private fun getImageFromAssets(): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(appContext.assets.open("bcg_favorites.png"), null)
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }


}