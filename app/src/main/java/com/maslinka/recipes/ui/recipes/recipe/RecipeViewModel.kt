package com.maslinka.recipes.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.AccessToPreferences.getFavourites
import com.maslinka.recipes.ui.AccessToPreferences.saveFavourites
import java.io.IOException


class RecipeViewModel(
    application: Application,
): AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext

    data class RecipeState(
        val recipe: Recipe? = null,
        val numberOfServings: Int = 1,
        val isFavourite: Boolean = false,
        val recipeDrawable: Drawable? = null,
    )


    //используется backing property
    //mutableCurrentRecipeState - для внутреннего использования
    //currentRecipeState - для внешних наблюдателей
    private val _recipeState = MutableLiveData<RecipeState>()
    val recipeState :LiveData<RecipeState>
        get() = _recipeState

    init {
        Log.i("!!!", "ViewModel init")
        //loadRecipe(recipe.id)
    }

    fun loadRecipe(recipeId: Int){
        //TODO load from network
        val listOfFavourites = getFavourites(appContext)
        _recipeState.value = RecipeState(
            recipe = STUB.getRecipeById(recipeId),
            isFavourite = recipeId in listOfFavourites,
            recipeDrawable = getImageFromAssets(STUB.getRecipeById(recipeId).imageUrl) ?: throw IllegalStateException("Image is not found"),)
        //Не оч понятно, почему не работает закомменченый код, вылетает ошибка IllegalStateException: Recipe is null при попытке вызвать initRecycler во фрагменте
        //ведь мы в первую очередь в initUI вызываем метод loadRecipe и устанавливаем значение рецепта
//        _recipeState.value = _recipeState.value?.copy(
//            recipe = STUB.getRecipeById(recipeId),
//            isFavourite = recipeId in listOfFavourites,
//            recipeDrawable = getImageFromAssets(STUB.getRecipeById(recipeId).imageUrl) ?: throw IllegalStateException("Image is not found"),
//            recipeImage = STUB.getRecipeById(recipeId).imageUrl)
    }

    fun updateServings(servings: Int){
        _recipeState.value = _recipeState.value?.copy(numberOfServings = servings)
    }

    fun onFavoritesClicked(recipeId: Int){
        val favouriteSet = getFavourites(appContext)
        val currentState = _recipeState.value ?: return
        if (recipeId in favouriteSet) {
            favouriteSet.remove(recipeId)
            _recipeState.value = currentState.copy(isFavourite = false)
        } else {
            favouriteSet.add(recipeId)
            _recipeState.value = currentState.copy(isFavourite = true)
        }
        saveFavourites(appContext,favouriteSet)
    }

    private fun getImageFromAssets(imageUrl: String): Drawable? {
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

}