package com.maslinka.recipes.ui.recipes.favourites

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.R
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences
import java.io.IOException
import java.util.concurrent.Executors

class FavouritesViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application.applicationContext

    private val recipesRepository = RecipesRepository()

    private val thread = Executors.newSingleThreadExecutor()

    private val _favouritesState = MutableLiveData<FavouritesState>()
    val favouritesState: LiveData<FavouritesState>
        get() = _favouritesState


    fun initState(){
        _favouritesState.value = FavouritesState(
            headerImage = getImageFromAssets(),
            contentDescription = R.string.content_description_favourites_fragment,
        )
    }

    fun updateFavouritesList() {
        thread.execute {
            recipesRepository.getRecipesByIds(AccessToPreferences.getFavourites(appContext.applicationContext), { result ->
                if (result != null) {
                    Handler(Looper.getMainLooper()).post {
                        _favouritesState.value = favouritesState.value?.copy(
                            favouritesList = result,
                            listIsEmpty = result.isEmpty()
                        ) ?: FavouritesState(
                            favouritesList = result,
                            listIsEmpty = result.isEmpty(),
                            headerImage = getImageFromAssets(),
                            contentDescription = R.string.content_description_favourites_fragment,
                        )
                    }
                }
                else{
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_LONG).show()
                    }
                }

            })
        }
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


    data class FavouritesState(
        val headerImage: Drawable? = null,
        val contentDescription: Int? = null,
        val favouritesList: List<Recipe> = emptyList(),
        val listIsEmpty: Boolean = true,
    )
}