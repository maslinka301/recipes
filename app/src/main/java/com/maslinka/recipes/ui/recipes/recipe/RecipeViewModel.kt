package com.maslinka.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
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
import com.maslinka.recipes.ui.AccessToPreferences.getFavourites
import com.maslinka.recipes.ui.AccessToPreferences.saveFavourites
import java.io.IOException
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RecipeViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext

    private val recipesRepository = RecipesRepository()

    private val thread = Executors.newSingleThreadExecutor()

    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState>
        get() = _recipeState

    init {
        Log.i("!!!", "ViewModel init")
    }

    fun loadRecipe(recipeId: Int) {
        thread.execute {
            recipesRepository.getRecipeById(recipeId) { result ->
                if (result != null) {
                    Handler(Looper.getMainLooper()).post {
                        val isFavourite = recipeId in getFavourites(appContext)
                        val recipeDrawable = getImageFromAssets(result.imageUrl)
                            ?: throw IllegalStateException("Image is not found")

                        _recipeState.value = recipeState.value?.copy(
                            recipe = result,
                            isFavourite = isFavourite,
                            recipeDrawable = recipeDrawable
                        ) ?: RecipeState(
                            recipe = result,
                            isFavourite = isFavourite,
                            recipeDrawable = recipeDrawable
                        )

                        Log.i("!!!", "Рецепт с id $recipeId загружен")
                    }
                } else {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(appContext, R.string.network_error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    fun updateServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(numberOfServings = servings)
    }

    fun onFavoritesClicked(recipeId: Int) {
        val favouriteSet = getFavourites(appContext)
        val currentState = _recipeState.value ?: return
        if (recipeId in favouriteSet) {
            favouriteSet.remove(recipeId)
            _recipeState.value = currentState.copy(isFavourite = false)
        } else {
            favouriteSet.add(recipeId)
            _recipeState.value = currentState.copy(isFavourite = true)
        }
        saveFavourites(appContext, favouriteSet)
    }

    fun getImageFromAssets(imageUrl: String): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(appContext.assets.open(imageUrl), null)
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeDrawable: Drawable? = null,
    )

}